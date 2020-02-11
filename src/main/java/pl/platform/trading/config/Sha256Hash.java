package pl.platform.trading.config;

import java.security.MessageDigest;

public class Sha256Hash {

    public byte[] getSHA256Hash(String s) {
        MessageDigest hasher = null;
        byte[] hash = null;
        try {
            hasher = MessageDigest.getInstance("SHA-256");
            hasher.update(s.getBytes("UTF-8"));
            hash = hasher.digest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return hash;
    }
}
