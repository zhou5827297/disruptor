package com.zhoukai.disruptor.producer;

/**
 * Created by zhoukai on 2017/3/14.
 */

import java.util.Random;
import java.util.UUID;

import com.lmax.disruptor.EventTranslator;
import com.zhoukai.disruptor.po.TradeTransaction;

public class TradeTransactionEventTranslator implements EventTranslator<TradeTransaction> {

    private Random random = new Random();

    public void translateTo(TradeTransaction event, long sequence) {
        this.generateTradeTransaction(event);
    }

    private TradeTransaction generateTradeTransaction(TradeTransaction trade) {
        trade.setPrice(random.nextDouble() * 9999);
        trade.setId(UUID.randomUUID().toString());
        return trade;
    }
}