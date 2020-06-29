package com.example.demo;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

/*
* JPA 관련된 설정만 로드.
* @Entity 클래스를 스캔하여 스프링 데이터 JPA 저장소를 구성.
* @Transactional 포함. ( 필요하지 않다면 옵션으로 propagation=Propagation.NOT_SUPPOPTED)
* 기본적으로 Embeded DataBase에 대한 테스트.
* real database를 사용하고자 하는 경우 @AutoConfigureTestDatabase 사용.
*   - default 값 : any (내장된 데이터 소스)
*   - replace 옵션 :  실제 DB 연결을 하고 싶다면 AutoConfigureTestDatabase.Replace.NONE 지정.
*   - @ActiveProfiles("test") 등의 영향을 받을 수 있음.
*
* 주요 목적은 아래와 같다.
*
* 1. 설정 정상 여부.
* 2. JPA를 사용하는 조회, 생성, 수정, 삭제 테스트
* */
@DataJdbcTest
@Slf4j
public class DemoUnitTest3_DataJpaTest {

    @MockBean //실직적 테스트를 위해서는 autowired도 좋다.
    private UserRepository repository;

    private final Long seqno = UUID.randomUUID().getLeastSignificantBits();

    @BeforeEach
    void setData() {
        UserEntity userEntity = new UserEntity();

        userEntity.setSeqNo(seqno);
        userEntity.setUserName("정우철");
        userEntity.setUserEmail("dncjf116@gmail.com");
        userEntity.setUserAddress("강남구");

        BDDMockito.given(repository.findById(seqno))
                .willReturn(Optional.of(userEntity));

        log.info("userEntity id : {}", seqno);
    }
    @Test
    void findById(){
        log.info("### START : DataJpaTest Test ###");
        Optional<UserEntity> userEntity = repository.findById(seqno);

        if(userEntity.isPresent()){
            log.info("userEntity id : {}",userEntity.get().getSeqNo());
            BDDAssertions.then(userEntity.get().getSeqNo()).isEqualTo(seqno);
            BDDAssertions.then(userEntity.get().getUserName()).isEqualTo("정우철");

        }

        log.info("### END : DataJpaTest Test ###");
    }
}
