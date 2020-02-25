package pl.platform.trading.rates;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;

class ExchnageRatesProviderTest {

    @Test
    void jsonToExchangeRates() {
        ExchangeRate rate = new ExchnageRatesProvider().jsonToExchangeRates("[{\"Ask\":1.37012,"
                + "\"Bid\":1.40991,\"Min\":1.42034,\"Max\":1.36235,"
                + "\"Time\":\"2020-01-07T16:22:48\","
                + "\"Name\":\"GBPCHF\",\"ChartDirection\":0}]").get(0);

        Assertions.assertEquals(new BigDecimal("1.37012"), rate.getAsk());
        Assertions.assertEquals(new BigDecimal("1.40991"), rate.getBid());
        Assertions.assertEquals(new Timestamp(1578414168000L), rate.getTimestamp());
        Assertions.assertEquals("GBPCHF", rate.getCurrencyPair());
    }
}