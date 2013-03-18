package com.mysema.query.domain;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import org.junit.Ignore;

@Ignore
public class Generic10Test {
    
    public interface Tradable {}

    public interface Market<T extends Tradable> {}
    
    @Entity
    public static class FutureTrade implements Tradable {}

    @MappedSuperclass
    public static abstract class AbstractTradingMarket<T extends Tradable> implements Market<T>{

        // XXX
        @OneToOne
        private TradingMarketLedger<AbstractTradingMarket<T>> ledger;
    }

    @Entity
    public static abstract class AbstractFuturesMarket extends AbstractTradingMarket<FutureTrade> {}

    // XXX
    @Entity
    public static class CommonFuturesMarket extends AbstractFuturesMarket {}

    @Entity
    public static class TradingMarketLedger<M extends Market<? extends Tradable>> {}
    
}
