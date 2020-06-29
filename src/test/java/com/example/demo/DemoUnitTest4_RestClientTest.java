package com.example.demo;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseActions;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;
import java.util.UUID;

/*
* Rest 클라이언트 테스트가 가능하다.
* Rest 통신의 JSON 형식이 예상대로 응답을 반환하는지 등을 테스트.
* ex) Apache HttpClient, Spring RestTemplate 을 사용하여 외부 서버에 웹 요청을 보내는 경우에 이에 응답할 Mock 서버를 만드는 것이라고 생각면 됨.
* */
@Slf4j
@RestClientTest(UserService.class)
public class DemoUnitTest4_RestClientTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MockRestServiceServer server;

    private final Long seqno = UUID.randomUUID().getLeastSignificantBits();

    @BeforeEach
    void setData() {
        UserEntity userEntity = new UserEntity();

        userEntity.setSeqNo(seqno);
        userEntity.setUserName("정우철");
        userEntity.setUserEmail("dncjf116@gmail.com");
        userEntity.setUserAddress("강남구");

        BDDMockito.given(userRepository.findById(seqno))
                .willReturn(Optional.of(userEntity));

        log.info("userEntity id : {}", seqno);
    }

    @Test
    void getData(){
        log.info("### START : RestClientTest Test ###");

        ResponseActions actions =server.expect(MockRestRequestMatchers.requestTo("/user"));
        actions.andRespond(MockRestResponseCreators.withSuccess(new ClassPathResource("/test.json",getClass()), MediaType.APPLICATION_JSON));

        log.info("### END : RestClientTest Test ###");
    }

}
