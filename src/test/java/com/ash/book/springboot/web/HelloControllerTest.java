package com.ash.book.springboot.web;


import com.ash.book.springboot.config.auth.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * -@RunWith : 스프링부트 테스트와 jUnit사이의 연결자 역할
 * -@WebMvcTest : Web(Spring MVC)에 집중할 수 있는 어노테이션. 선언할 경우 @Controller, @ControllerAdvice 등 사용 가능
 * 그러나 @Service, @Component 등 사용 불가. 여기서는 컨트롤러 사용하기 때문에 선언
 * -MockMvc : 웹 API 테스트시 사용. 스프링 MVC 테스트의 시작점. GET,POST 등 API 테스트 가능
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HelloController.class, excludeFilters = {
        @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
})
public class HelloControllerTest {
    @Autowired
    private MockMvc mvc;

    @WithMockUser(roles = "USER")
    @Test
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";

        //mvc.perform(get("/hello")) : MockMvc를 통해 /hello 주소로 HTTP GET 요청(체이닝 지원)
        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello)); //mvc.perform의 결과 검증
    }
    @WithMockUser(roles = "USER")
    @Test
    public void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;

        mvc.perform(
                get("/hello/dto")
                        .param("name", name)
                        .param("amount", String.valueOf(amount))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));
    }
}