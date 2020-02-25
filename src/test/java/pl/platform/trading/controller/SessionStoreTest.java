package pl.platform.trading.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.platform.trading.sql.user.User;

import java.math.BigDecimal;

class SessionStoreTest {

    @Mock
    SessionStore sessionStore;


    @BeforeEach
    public void testSetUp() {
        MockitoAnnotations.initMocks(this);

        User testUser = new User();

        testUser.setFirstName("ExampleFirstName");
        testUser.setLastName("ExampleLastName");
        testUser.setEmail("example_user@mail.com");
        testUser.setAccountBalance(new BigDecimal("20500"));

        Mockito.when(sessionStore.getCurrentUser()).thenReturn(testUser);
    }

    @Test
    void getCurrentUser() {
        User user = sessionStore.getCurrentUser();

        Assertions.assertEquals("ExampleFirstName", user.getFirstName());
        Assertions.assertEquals("ExampleLastName", user.getLastName());
        Assertions.assertEquals("example_user@mail.com", user.getEmail());
        Assertions.assertEquals(new BigDecimal("20500"), user.getAccountBalance());
    }
}
