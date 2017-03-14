package com.zhoukai.disruptor.producer;

import java.util.concurrent.Callable;

import com.lmax.disruptor.dsl.Disruptor;
import com.zhoukai.disruptor.po.TradeTransaction;

public class TradeTransactionPublisher implements Callable<Boolean> {

    Disruptor<TradeTransaction> disruptor;

    public TradeTransactionPublisher(Disruptor<TradeTransaction> disruptor) {
        this.disruptor = disruptor;
    }

    public Boolean call() throws Exception {
        TradeTransactionEventTranslator tradeTransloator = new TradeTransactionEventTranslator();
        disruptor.publishEvent(tradeTransloator);
        return true;
    }
}