package com.zhoukai.disruptor;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import com.zhoukai.disruptor.consumer.TradeTransactionInDBHandler;
import com.zhoukai.disruptor.consumer.TradeTransactionJMSNotifyHandler;
import com.zhoukai.disruptor.consumer.TradeTransactionVasConsumer;
import com.zhoukai.disruptor.po.TradeTransaction;
import com.zhoukai.disruptor.producer.TradeTransactionEventTranslator;
import com.zhoukai.disruptor.producer.TradeTransactionPublisher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class Main {

    private static int LOOP = 10000000;//模拟一千万次交易的发生


    public static void main(String[] args) throws Exception {


        long beginTime = System.currentTimeMillis();

        int bufferSize = 1024;

        Disruptor<TradeTransaction> disruptor = new Disruptor<TradeTransaction>(new EventFactory<TradeTransaction>() {
            public TradeTransaction newInstance() {
                return new TradeTransaction();
            }
        }, bufferSize, Executors.defaultThreadFactory(), ProducerType.SINGLE, new BusySpinWaitStrategy());
//        EventHandlerGroup<TradeTransaction> handlerGroup = disruptor.handleEventsWith(new TradeTransactionVasConsumer(), new TradeTransactionInDBHandler());
//
//        TradeTransactionJMSNotifyHandler jmsConsumer = new TradeTransactionJMSNotifyHandler();
//        //声明在C1,C2完事之后执行JMS消息发送操作 也就是流程走到C3
//        handlerGroup.then(jmsConsumer);


        EventHandlerGroup<TradeTransaction> handlerGroup = disruptor.handleEventsWith(new TradeTransactionVasConsumer());
        handlerGroup.then(new TradeTransactionInDBHandler());
        handlerGroup.then(new TradeTransactionJMSNotifyHandler());
        disruptor.start();

        Collection<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();
        for (int i = 0; i < LOOP; i++) {
            tasks.add(new TradeTransactionPublisher(disruptor));
//            TradeTransactionEventTranslator tradeTransloator = new TradeTransactionEventTranslator();
//            disruptor.publishEvent(tradeTransloator);
        }
        ExecutorService executor = Executors.newFixedThreadPool(100);
        executor.invokeAll(tasks, 2, TimeUnit.SECONDS);
        disruptor.shutdown();
        executor.shutdown();

        System.out.println("总耗时:" + (System.currentTimeMillis() - beginTime));
    }
}
