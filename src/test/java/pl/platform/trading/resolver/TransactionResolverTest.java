package pl.platform.trading.resolver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.internal.util.reflection.FieldSetter;

import pl.platform.trading.rates.ExchangeRate;
import pl.platform.trading.rates.ExchnageRatesProvider;
import pl.platform.trading.sql.closedposition.ClosedPosition;
import pl.platform.trading.sql.closedposition.ClosedPositionRepository;
import pl.platform.trading.sql.openedposition.OpenedPosition;
import pl.platform.trading.sql.openedposition.OpenedPositionBuilder;
import pl.platform.trading.sql.openedposition.OpenedPositionRepository;
import pl.platform.trading.sql.user.User;
import pl.platform.trading.sql.user.UserRepository;

import java.math.BigDecimal;

class TransactionResolverTest {

    @Mock
    UserRepository usersDao;

    @Mock
    OpenedPositionRepository openedPositionsDao;

    @Mock
    ClosedPositionRepository closedPositionsDao;

    @Mock
    ExchnageRatesProvider rates;

    @InjectMocks
    TransactionResolver resolver;


    @BeforeEach
    public void testSetUp() {
        MockitoAnnotations.initMocks(this);

        User testUser = new User();
        testUser.setFirstName("ExampleFirstName");
        testUser.setLastName("ExampleLastName");
        testUser.setEmail("example_user@mail.com");
        testUser.setAccountBalance(new BigDecimal("20500"));
        Mockito.when(usersDao.findByUid(3L)).thenReturn(testUser);

        Mockito.when(usersDao.save(Mockito.any(User.class))).then(AdditionalAnswers.returnsFirstArg());

        OpenedPosition testOpened = new OpenedPositionBuilder()
                .setCurrencyPair("GBPCAD")
                .setLongPosition(false)
                .setUid(3L)
                .setAmount(new BigDecimal("3500.85"))
                .setOpeningPrice(new BigDecimal("1.4529"))
                .build();
        Mockito.when(openedPositionsDao.findByTid(15L)).thenReturn(testOpened);

        ExchangeRate testRate = new ExchangeRate();
        try {
            FieldSetter.setField(testRate, testRate.getClass().getDeclaredField("currencyPair"),
                    "GBPCAD");
            FieldSetter.setField(testRate, testRate.getClass().getDeclaredField("ask"),
                    new BigDecimal("1.5841"));
            FieldSetter.setField(testRate, testRate.getClass().getDeclaredField("bid"),
                    new BigDecimal("1.5824"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        Mockito.when(rates.getPairRate("GBPCAD")).thenReturn(testRate);
    }

    @Test
    public void calculateProfit() {
        OpenedPosition opened = new OpenedPositionBuilder()
                .setAmount(new BigDecimal("150"))
                .setOpeningPrice(new BigDecimal("1.125"))
                .setLongPosition(true)
                .build();

        ExchangeRate rate = new ExchangeRate();
        try {
            FieldSetter.setField(rate, rate.getClass().getDeclaredField("currencyPair"),
                    "EURCHF");
            FieldSetter.setField(rate, rate.getClass().getDeclaredField("ask"),
                    new BigDecimal("1.245"));
            FieldSetter.setField(rate, rate.getClass().getDeclaredField("bid"),
                    new BigDecimal("1.241"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        BigDecimal profit = resolver.calculateProfit(opened, rate);

        Assertions.assertEquals(new BigDecimal("15.46667"), profit);
    }

    @Test
    public void updateAccountBalance() {
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        resolver.updateAccountBalance(3L, new BigDecimal("30.952"));

        Mockito.verify(usersDao).save(userCaptor.capture());

        User user = userCaptor.getValue();

        Assertions.assertEquals(new BigDecimal("20530.952"), user.getAccountBalance());
    }

    @Test
    public void openPosition() {
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        ArgumentCaptor<OpenedPosition> openedPositionCaptor =
                ArgumentCaptor.forClass(OpenedPosition.class);

        resolver.openPosition(3L, "GBPCAD", new BigDecimal("177.2395"), true);

        Mockito.verify(usersDao).save(userCaptor.capture());
        Mockito.verify(openedPositionsDao).save(openedPositionCaptor.capture());

        User user = userCaptor.getValue();
        OpenedPosition opened = openedPositionCaptor.getValue();

        Assertions.assertEquals(new BigDecimal("20322.7605"), user.getAccountBalance());

        Assertions.assertEquals(Long.valueOf(3L), opened.getUid());
        Assertions.assertEquals("GBPCAD", opened.getCurrencyPair());
        Assertions.assertEquals(new BigDecimal("177.2395"), opened.getAmount());
        Assertions.assertEquals(true, opened.isLongPosition());
        Assertions.assertEquals(new BigDecimal("1.5841"), opened.getOpeningPrice());
    }

    @Test
    public void closePosition() {
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        ArgumentCaptor<ClosedPosition> closedPositionCaptor =
                ArgumentCaptor.forClass(ClosedPosition.class);

        resolver.closePosition(15L);

        Mockito.verify(usersDao).save(userCaptor.capture());
        Mockito.verify(closedPositionsDao).save(closedPositionCaptor.capture());

        User user = userCaptor.getValue();
        ClosedPosition closed = closedPositionCaptor.getValue();

        Assertions.assertEquals(new BigDecimal("23710.89891"), user.getAccountBalance());

        Assertions.assertEquals(new BigDecimal("-289.95109"), closed.getProfit());
        Assertions.assertEquals("GBPCAD", closed.getCurrencyPair());
        Assertions.assertEquals(new BigDecimal("1.5841"), closed.getClosingPrice());
    }
}