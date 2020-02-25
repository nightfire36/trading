package pl.platform.trading.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.codec.Hex;

class Sha256HashTest {

    @Test
    void getSHA256Hash() {
        byte[] hash = new Sha256Hash().getSHA256Hash("test_password");
        Assertions.assertArrayEquals(Hex.decode("10a6e6cc8311a3e2bcc09bf6c199adecd5dd59408c343e926b129c4914f3cb01"), hash);
    }
}
