package com.example.demo.entity;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//Junit Test
class UserEntityTest {


    //Success
    @Test
    void getId() {
        UserEntity userEntity = new UserEntity();
        userEntity.setSeqNo(11L);
        userEntity.setUserName("정우철");
        userEntity.setUserEmail("dncjf116@gmail.com");
        userEntity.setUserAddress("강남구");

        final Long seqNo = userEntity.getSeqNo();
        assertEquals(11L,seqNo);

    }

    //Fail
    @Test
    void getName() {
        UserEntity userEntity = new UserEntity();
        userEntity.setSeqNo(11L);
        userEntity.setUserName("정우철");
        userEntity.setUserEmail("dncjf116@gmail.com");
        userEntity.setUserAddress("강남구");

        final String name = userEntity.getUserName();
        assertEquals("정우찰",name);

    }
}