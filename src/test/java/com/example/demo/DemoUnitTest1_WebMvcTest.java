package com.example.demo;


import com.example.demo.controller.UserController;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.BDDAssertions;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.BDDMockito.given;

/*
* MVC를 위한 테스터, 컨트롤러가 예상대로 동작하는지 테스트하는데 사용.
* MockBean, MockMVC를 자동 구성하여 테스트 가능하도록 한다.
* Spring security의 테스터도 지원한다.
* 테스트할 특정 컨트롤러를 클래스를 명시하도록 한다.
* */
@WebMvcTest(UserController.class)
@Slf4j
public class DemoUnitTest1_WebMvcTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private UserService userService;

    @Autowired
    WebApplicationContext ctx;

    @BeforeEach
    void encording(){
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilter(new CharacterEncodingFilter("UTF-8",true))
                .build();
    }

    @Test
    void getSaveFindList() throws Exception {
        log.info("### START : MockMvc Test ###");
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("정우철");
        userEntity.setUserEmail("dncjf116@gmail.com");
        userEntity.setUserAddress("강남구");

        //given --> 추가를 요렇게 해버림..
        given(userService.getFindByUserName(userEntity.getUserName()))
                .willReturn(userEntity);

        //When
        final ResultActions actions = mvc.perform(MockMvcRequestBuilders.get("/user/{userName}","정우철")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        //Then
        actions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName", Is.is("정우철")))
                .andDo(MockMvcResultHandlers.print());

        log.info("### END : MockMvc Test ###");

    }



}
