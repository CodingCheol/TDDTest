package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
public class EncryptionTest {

    @Test
    @Timeout(value = 5000, unit = TimeUnit.MICROSECONDS)
    public void shortUUID() throws Exception {
        // char(2byte) : 64 -> (hex convert) : 32 (+ 4 '-') =>  long(4byte)  : 18 + 1 + X
        UUID uuid = UUID.randomUUID();

        long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();

        String candidate1 = Long.toString(l, Character.MAX_RADIX);
        log.info("uid candidate : {}",candidate1);

        MessageDigest salt = MessageDigest.getInstance("SHA-256");
        salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
        String candidate2 = bytesToHex(salt.digest());

        log.info("uid candidate: {}",candidate2);

    }
    public static String bytesToHex(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            sb.append(String.format("%02x",b));
//            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
