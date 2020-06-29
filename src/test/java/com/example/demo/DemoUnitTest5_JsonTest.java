package com.example.demo;

import com.example.demo.model.JsonTestModel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

/*
* Json serialization, deserialization 테스트
* Gson, jackson의 테스터 제공.(JacksonTester, GsonTester, BasicJsonTester)
*
* */

@JsonTest
public class DemoUnitTest5_JsonTest {

    @Autowired
    private JacksonTester<JsonTestModel> json;

    @Test
    void testSerialization() throws Exception {
        JsonTestModel model = new JsonTestModel("1111", "jwc");
        Assertions.assertThat(this.json.write(model)).isEqualToJson("/expected.json");
        Assertions.assertThat(this.json.write(model)).hasJsonPathStringValue("@.id");
        Assertions.assertThat(this.json.write(model)).extractingJsonPathStringValue("@.id")
            .isEqualTo("1111");
        
    }
    @Test
    void testDeserialization() throws IOException {
        String content ="{\"id\":\"1111\",\"name\":\"jwc\"}";
        Assertions.assertThat(this.json.parse(content))
                .isEqualTo(new JsonTestModel("1111","jwc"));

        Assertions.assertThat(this.json.parseObject(content).getId()).isEqualTo("1111");
    }

}
