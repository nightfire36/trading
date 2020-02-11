package pl.platform.trading;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.codec.Hex;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import org.springframework.test.util.ReflectionTestUtils;
import pl.platform.trading.rates.ExchangeRate;
import pl.platform.trading.rates.ExchnageRatesProvider;
import pl.platform.trading.config.Sha256Hash;
import pl.platform.trading.controller.MvcController;
import pl.platform.trading.resolver.TransactionResolver;
import pl.platform.trading.sql.user.UserRepository;
import pl.platform.trading.sql.closedposition.ClosedPosition;
import pl.platform.trading.sql.closedposition.ClosedPositionRepository;
import pl.platform.trading.sql.openedposition.OpenedPosition;
import pl.platform.trading.sql.openedposition.OpenedPositionRepository;
import pl.platform.trading.sql.user.User;

@RunWith(SpringRunner.class)
@WebAppConfiguration
public class TradingApplicationTests {

    @Mock
    UserRepository usersDao;

    @Mock
    OpenedPositionRepository openedPositionsDao;

    @Mock
    ClosedPositionRepository closedPositionsDao;

    @Mock
    ExchnageRatesProvider rates;

    @InjectMocks
    MvcController controller;

    @InjectMocks
    TransactionResolver resolver;

    @Before
    public void testSetUp() {
        User testUser = new User();
        testUser.setFirstName("ExampleFirstName");
        testUser.setLastName("ExampleLastName");
        testUser.setEmail("example_user@mail.com");
        testUser.setAccountBalance(new BigDecimal("20500"));
        Mockito.when(usersDao.findByEmail("example_user@mail.com")).thenReturn(testUser);
        Mockito.when(usersDao.findByUid(3L)).thenReturn(testUser);
        Mockito.when(usersDao.save(Mockito.any(User.class))).then(AdditionalAnswers
                .returnsFirstArg());

        OpenedPosition testOpened = new OpenedPosition();
        testOpened.setCurrencyPair("GBPCAD");
        testOpened.setLongPosition(false);
        testOpened.setUid(3L);
        testOpened.setAmount(new BigDecimal("3500.85"));
        testOpened.setOpeningPrice(new BigDecimal("1.4529"));
        Mockito.when(openedPositionsDao.findByTid(15L)).thenReturn(testOpened);

        ExchangeRate testRate = new ExchangeRate();
        ReflectionTestUtils.setField(testRate, "currencyPair", "GBPCAD");
        ReflectionTestUtils.setField(testRate, "ask", new BigDecimal("1.5841"));
        ReflectionTestUtils.setField(testRate, "bid", new BigDecimal("1.5824"));

        Mockito.when(rates.getPairRate("GBPCAD")).thenReturn(testRate);
    }

    @Test
    public void SHA_256_hash() {
        byte[] hash = new Sha256Hash().getSHA256Hash("test_password");
        Assert.assertArrayEquals(Hex.decode(
                "10a6e6cc8311a3e2bcc09bf6c199adecd5dd59408c343e926b129c4914f3cb01"), hash);
    }

    @Test
    public void calculate_profit() {
        OpenedPosition opened = new OpenedPosition();
        opened.setAmount(new BigDecimal("150"));
        opened.setOpeningPrice(new BigDecimal("1.125"));
        opened.setLongPosition(true);

        ExchangeRate rate = new ExchangeRate();
        ReflectionTestUtils.setField(rate, "currencyPair", "EURCHF");
        ReflectionTestUtils.setField(rate, "ask", new BigDecimal("1.245"));
        ReflectionTestUtils.setField(rate, "bid", new BigDecimal("1.241"));

        BigDecimal profit = resolver.calculateProfit(opened, rate);

        Assert.assertEquals(new BigDecimal("15.46667"), profit);
    }

    @Test
    public void update_account_balance() {
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        resolver.updateAccountBalance(3L, new BigDecimal("30.952"));

        Mockito.verify(usersDao).save(userCaptor.capture());

        User user = userCaptor.getValue();

        Assert.assertEquals(new BigDecimal("20530.952"), user.getAccountBalance());
    }

    @Test
    public void init_user() {
        User user = controller.initUser("example_user@mail.com");

        Assert.assertEquals("ExampleFirstName", user.getFirstName());
        Assert.assertEquals("ExampleLastName", user.getLastName());
        Assert.assertEquals("example_user@mail.com", user.getEmail());
        Assert.assertEquals(new BigDecimal("20500"), user.getAccountBalance());
    }

    @Test
    public void open_position() {
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        ArgumentCaptor<OpenedPosition> openedPositionCaptor =
                ArgumentCaptor.forClass(OpenedPosition.class);

        resolver.openPosition(3L, "GBPCAD", new BigDecimal("177.2395"), true);

        Mockito.verify(usersDao).save(userCaptor.capture());
        Mockito.verify(openedPositionsDao).save(openedPositionCaptor.capture());

        User user = userCaptor.getValue();
        OpenedPosition opened = openedPositionCaptor.getValue();

        Assert.assertEquals(new BigDecimal("20322.7605"), user.getAccountBalance());

        Assert.assertEquals(Long.valueOf(3L), opened.getUid());
        Assert.assertEquals("GBPCAD", opened.getCurrencyPair());
        Assert.assertEquals(new BigDecimal("177.2395"), opened.getAmount());
        Assert.assertEquals(true, opened.isLongPosition());
        Assert.assertEquals(new BigDecimal("1.5841"), opened.getOpeningPrice());
    }

    @Test
    public void close_position() {
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        ArgumentCaptor<ClosedPosition> closedPositionCaptor =
                ArgumentCaptor.forClass(ClosedPosition.class);

        resolver.closePosition(15L);

        Mockito.verify(usersDao).save(userCaptor.capture());
        Mockito.verify(closedPositionsDao).save(closedPositionCaptor.capture());

        User user = userCaptor.getValue();
        ClosedPosition closed = closedPositionCaptor.getValue();

        Assert.assertEquals(new BigDecimal("23710.89891"), user.getAccountBalance());

        Assert.assertEquals(new BigDecimal("-289.95109"), closed.getProfit());
        Assert.assertEquals("GBPCAD", closed.getCurrencyPair());
        Assert.assertEquals(new BigDecimal("1.5841"), closed.getClosingPrice());
    }

    @Test
    public void json_to_xchgRate() {
        ExchangeRate rate = new ExchnageRatesProvider().jsonToExchangeRates("[{\"Ask\":1.37012,"
                + "\"Bid\":1.40991,\"Min\":1.42034,\"Max\":1.36235,"
                + "\"Time\":\"2020-01-07T16:22:48\","
                + "\"Name\":\"GBPCHF\",\"ChartDirection\":0}]").get(0);

        Assert.assertEquals(new BigDecimal("1.37012"), rate.getAsk());
        Assert.assertEquals(new BigDecimal("1.40991"), rate.getBid());
        Assert.assertEquals(new Timestamp(1578414168000L), rate.getTimestamp());
        Assert.assertEquals("GBPCHF", rate.getCurrencyPair());
    }

}
