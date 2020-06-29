package com.example.demo;

import com.fasterxml.uuid.Generators;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.internal.util.StringUtil;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
public class UUIDTest {

    // char(2byte) : 64 -> (hex convert 8byte) : 32 (+ 4 '-') =>  long(16byte)  : 18 + 1 + X
    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    public void randomBasedUUID(){
        //UUID type4 - random
        UUID uuid = UUID.randomUUID();

        long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
        //timeBased가 아니라 안됨
//        log.debug("timestamp : {}, clockSequence : {}",uuid.timestamp(), uuid.clockSequence());

        String candidate1 = Long.toString(l, Character.MAX_RADIX);
        log.info("uid candidate1: {}",candidate1.toUpperCase() + getSuffixPadding(candidate1));
    }
    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    public void timeBased2UUID(){
        long msb = System.currentTimeMillis();
        long lsb = System.currentTimeMillis();
        //initialize uuid
        UUID uuid = new UUID(msb, lsb);
        long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
        //timeBased가 아니라 안됨
//        log.debug("timestamp : {}, clockSequence : {}",uuid.timestamp(), uuid.clockSequence());

        String candidate1 = Long.toString(l, Character.MAX_RADIX);
        log.info("uid candidate4: {}",candidate1.toUpperCase() + getSuffixPadding(candidate1));
    }
    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    public void timeBasedUUID(){
        //UUID type1 - time based
        UUID uuid = Generators.timeBasedGenerator().generate();
        long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
        log.debug("timestamp : {}, clockSequence : {}",uuid.timestamp(), uuid.clockSequence());

        String candidate2 = Long.toString(l, Character.MAX_RADIX);
        log.info("uid candidate2: {}",candidate2.toUpperCase() + getSuffixPadding(candidate2));
    }
    //-1 이 나옴..
    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    public void sha256BasedUUID() throws Exception {
        //SHA-256
        MessageDigest salt = MessageDigest.getInstance("SHA-512");
        salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
        long l = ByteBuffer.wrap(salt.digest()).getLong();

        String candidate3 = Long.toString(l, Character.MAX_RADIX);
        log.info("uid candidate3: {}",candidate3.toUpperCase() + getSuffixPadding(candidate3));
    }

    private String getSuffixPadding(String key){

        if(key.length() >= 20){
            return StringUtils.EMPTY;
        }

        StringBuilder sb = new StringBuilder(key);
        for(int i = 0; i< 20 - (key.length()) ; i++){
            sb.append("X");
        }
        return sb.toString();
    }
}
