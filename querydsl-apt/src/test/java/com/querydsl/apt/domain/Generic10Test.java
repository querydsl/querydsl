package com.querydsl.apt.domain;

import static org.junit.Assert.assertNotNull;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import org.junit.Test;

import com.querydsl.core.domain.*;

public class Generic10Test extends AbstractTest {

    public interface Tradable {}

    public interface Market<T extends Tradable> {}

    @Entity
    public static class FutureTrade implements Tradable {}

    @MappedSuperclass
    public static abstract class AbstractTradingMarket<T extends Tradable> implements Market<T> {

        @OneToOne
        private TradingMarketLedger<AbstractTradingMarket<T>> ledger;
    }

    @Entity
    public static abstract class AbstractFuturesMarket extends AbstractTradingMarket<FutureTrade> {}

    @Entity
    public static class CommonFuturesMarket extends AbstractFuturesMarket {}

    @Entity
    public static class TradingMarketLedger<M extends Market<? extends Tradable>> {}

    @Test
    public void test() {
        assertNotNull(QGeneric10Test_FutureTrade.futureTrade);

        start(QGeneric10Test_AbstractTradingMarket.class, QGeneric10Test_AbstractTradingMarket.abstractTradingMarket);
        assertPresent("ledger");

        start(QGeneric10Test_AbstractFuturesMarket.class, QGeneric10Test_AbstractFuturesMarket.abstractFuturesMarket);
        assertPresent("ledger");

        start(QGeneric10Test_CommonFuturesMarket.class, QGeneric10Test_CommonFuturesMarket.commonFuturesMarket);
        assertPresent("ledger");

        start(QGeneric10Test_TradingMarketLedger.class, QGeneric10Test_TradingMarketLedger.tradingMarketLedger);
    }
}
