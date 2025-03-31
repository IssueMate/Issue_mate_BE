package study.issue_mate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

// Extension 사용을 위한 어노테이션
@ExtendWith(SpringExtension.class)
// MockMvc 제어하는 어노테이션
@AutoConfigureMockMvc
@SpringBootTest
//@WebMvcTest(TestController.class)
public class TestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void controllerGetTest() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/test/get"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void controllerPostTest() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/test/post"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}