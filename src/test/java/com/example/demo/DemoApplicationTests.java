package com.example.demo;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
//MockMvc - Get / Post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

//MockMvc - Print / HttpStatus
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//MockGiven
import static org.mockito.BDDMockito.given;
//Hamcrest -is
import static org.hamcrest.core.Is.is;


/*
Mockito (mocking framework)
Hamcrest (matcher Objects - 비교, 대조를 위한 라이브러리)
AssertJ (일반 java 확인을 위한 라이브러리)
JSONassert (json을 확인하기 위한 라이브러리)
JsonPath (Xpath for JSON)
*/

/*
Junit5 를 사용 시에는 RunWith(SpringRunner.class)를 명시할 필요가 없다.
SpringBootTest 안에 @ExtendWith({SpringExtension.class})
*/
//@RunWith(SpringRunner.class)
@SpringBootTest(
        //Test 시에 필요한 properties 를 설정 및 변경 가능
        properties = {
                "propertyTest.value=propertyTest",
                "testValue=test"
        }
        //기본적으로 Mock 환경 이다.(아래의 MockMvc도 이때 사용한다.)
        ,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
/*
Mock 테스트시 필요한 의존성을 제공.
MockMvc 객체를 통해 실제 컨테이너가 실행되는 것은 아니지만 로직상으로 테스트를 진행할 수 있음.(DispatcherServlet을 로딩하여 Mockup으로 처리 가능.)
print() 함수를 통하여 좀 더 디테일한 테스트 결과를 바랄 수 있음.
*/
@AutoConfigureMockMvc
/*
프로파일 전략을 사용 중이라면 원하는 프로파일 환경값 설정이 가능.
*/
//@ActiveProfiles

/*
테스트 완료 후 자동으로 rollback됨.
Spring-boot-test 는 단순히 Spring-test를 확장한 것이라 @Test어노테이션과 함께 @Transactional 어노테이션을 함께 사용하여 끝날 시 rollback함
단, webEnvironment.RANDOM_PORT, DEFINED_PORT를 사용하면 실제 테스트 서버는 별도의 스레드에서 테스트를 수행하기에 롤백이 안됨.
*/
@Transactional
/*
* 실제로 실행할 필요가 없기 때문에 Ignore를 선언
* */
@Ignore
// lombok log4j
@Slf4j
public class DemoApplicationTests {

    @Value("${propertyTest.value}")
    private String propertyTestValue;

    @Value("${testValue}")
    private String testValue;

    /*
    실제 객체를 만들기에 비용과 시간이 들거나 의존성이 길게 걸쳐져 있는 경우에 가짜 개체를 만들어서 사용한다.
    WebApplicationContext를 로드하며 내장된 서블릿 컨테이너가 아닌 Mock 서블릿을 제공 (일반적으로 Controller사용에서 쓰인다.)
    @AutoConfigureMockMvc 어노테이션을 함께 사용하면 별다른 설정 없이 간편하게 MockMvc를 사용한 테스트를 진행.
    MockMvc는 브라우저에서 요청과 응답을 의미하는 객체로써 Controller 테스터 사용을 용이하게 해주는 라이브러리.
    Character encoding 변경을 위해서 MockMvcBuilders 를 이용하여 재 세팅
    */
    @Autowired
    MockMvc mvc;
    /*
     * Spring web application context 설정 파일 정보
     */
    @Autowired
    WebApplicationContext ctx;

    /*
    webEnvironment 환경이 None을 설정하지 않을 시 사용 가능 하며 그에 맞춰서 자동으로 빈을 설정하여 RestTemplate 처리가능.
    Spring 4.x 이후부터 지원하는 Spring HTTP 통신 템플릿
    HTTP 요청 후 Json,xml,String과 같은 응답을 받을 수 있는 템플릿
    Http request를 지원하는 HttpClient를 사용.
    ResponseEntity 와 Server to Server 통신에 사용.(HttpEntity를 상속받아서 HttpHeader와 body, 심지어 상태코드, 응답메시지등도 포함)
    Header,Context-Type등을 설정하여 외부 API 호출
    */
    @Autowired
    TestRestTemplate testTemplate;

    /*
    Mock 객체를 빈으로써 등록할 수 있다.
    @MockBean 은 Spring의 ApplicationContext는 Mock 객체를 빈으로 등록,
    @MockBean 으로 선언된 객체와 같은 이름과 타입으로 이미 빈이 등록되어 있다며 해당빈은 선언한 @MockBean으로 대체.
    */
    @MockBean(name = "userRepository")
    UserRepository repository;

    /*Service로 등록하는 Bean*/
    @Autowired
    UserService userService;

    @Test
    void propertyTest() {
        log.info("### Properties Test ###");
        log.info("propertyTestValue : {}", propertyTestValue);
        log.info("testValue : {}", testValue);
    }

//    @BeforeAll junit4의 @BeforeClass : static 하게 만들어서 해당 Test running 하기 전에 수행
    @BeforeEach //Junit4의 @Before     : 각 Test가 수행하기 전에 수행
    public void setConfiguration(){
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilter(charterEncoding())
//                .alwaysDo(print())
                .build();
    }

    private CharacterEncodingFilter charterEncoding(){
        return new CharacterEncodingFilter("UTF-8",true);
    }

//    @BeforeEach
//    public void settingMockData() throws Exception {
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUserName("정우철");
//        userEntity.setUserEmail("dncjf116@gmail.com");
//        userEntity.setUserAddress("강남구");
//        ObjectMapper mapper =  new ObjectMapper();
//        mvc.perform(
//                post("/save")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .characterEncoding("UTF-8")
//                        .content(mapper.writeValueAsBytes(userEntity))
//                )
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print());
//
//    }
    @Test
    void mockMvcTest() throws Exception {
        log.info("### START : MOC MVC Test ###");
        mvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userName", is("정우철")))
                .andDo(print());
        log.info("### END : MOC MVC Test ###");
    }

    @Test
    void testTemplateTest(){
        log.info("### START : testTemplate Test ###");
        ResponseEntity<UserEntity[]> response = testTemplate.getForEntity("/user", UserEntity[].class); //List 형태 일 경우, Arrays로 묶어서 사용하거나 Wrapper class로 사
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isNotNull();

        log.info("### END : testTemplate Test ###");
    }

    @Test
    void mockBeanTest(){
        log.info("### START : mockBean Test ###");
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("정우철");
        userEntity.setUserEmail("dncjf116@gmail.com");
        userEntity.setUserAddress("강남구");

        //given --> 추가를 요렇게 해버림..
        given(repository.findByUserName("정우철"))
                .willReturn(userEntity);

        UserEntity userEntity2 = repository.findByUserName("정우철");
        List<UserEntity> userEntityList = userService.getFindAll();

        then(userEntityList.get(0).getUserName()).isEqualTo(userEntity2.getUserName());

        log.info("### END : mockBean Test ###");
    }
}
