package com.zhoukai.disruptor.consumer;

/**
 * Created by zhoukai on 2017/3/14.
 */

import com.lmax.disruptor.EventHandler;
import com.zhoukai.disruptor.po.TradeTransaction;


public class TradeTransactionInDBHandler implements EventHandler<TradeTransaction> {

    public void onEvent(TradeTransaction event, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(event);
    }

    public void onEvent(TradeTransaction event) throws Exception {
        System.out.println("consumer-db -> C2");
    }

}