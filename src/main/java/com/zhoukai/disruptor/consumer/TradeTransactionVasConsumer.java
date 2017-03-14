package com.zhoukai.disruptor.consumer;

/**
 * Created by zhoukai on 2017/3/14.
 */

import com.lmax.disruptor.EventHandler;
import com.zhoukai.disruptor.po.TradeTransaction;

public class TradeTransactionVasConsumer implements EventHandler<TradeTransaction> {

    public void onEvent(TradeTransaction event, long sequence,
                        boolean endOfBatch) throws Exception {
        System.out.println("consumer-normal -> C1");
    }

}