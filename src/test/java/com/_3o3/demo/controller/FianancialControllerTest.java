package com._3o3.demo.controller;


import com._3o3.demo.api.financial.application.FinancialService;
import com._3o3.demo.api.member.domain.Member;
import com._3o3.demo.common.ApiResultResponse;
import com._3o3.demo.security.Authentication.SignInDetails;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK)
public class FianancialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FinancialService financialService;

    @WithMockUser("USER")
    @Test
    public void 스크램_호출() throws Exception {
        // given
        Member member = Member.builder().userId("test").password("123456").build();
        SignInDetails signInDetails = new SignInDetails(member);
        when(financialService.scrap(signInDetails)).thenReturn(ApiResultResponse.of(HttpStatus.OK, null));
        // when, then
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/szs/scrap")
                            .content("{}")
                             .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    public void 스크램_호출_권한없음() throws Exception {
        // given
        Member member = Member.builder().userId("test").password("123456").build();
        SignInDetails signInDetails = new SignInDetails(member);
        when(financialService.scrap(signInDetails)).thenReturn(ApiResultResponse.of(HttpStatus.OK, null));
        // when, then
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/szs/scrap")
                .content("{}")
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser("USER")
    @Test
    public void 결정세액조회_호출() throws Exception {
        // given
        // Refund 메서드의 파라미터 타입에 맞게 실제 데이터를 생성하여 전달해야 합니다.
        // 예시: SignInDetails 객체 생성

        Member member = Member.builder().userId("test").password("123456").build();
        SignInDetails signInDetails = new SignInDetails(member);
        when(financialService.refund(signInDetails)).thenReturn(ApiResultResponse.of(HttpStatus.OK, null));

        // when, then
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/szs/refund")
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }


    @WithMockUser("USER")
    @Test
    public void 결정세액조회_호출실패_post() throws Exception {
        // given
        // Refund 메서드의 파라미터 타입에 맞게 실제 데이터를 생성하여 전달해야 합니다.
        // 예시: SignInDetails 객체 생성

        Member member = Member.builder().userId("test").password("123456").build();
        SignInDetails signInDetails = new SignInDetails(member);
        when(financialService.refund(signInDetails)).thenReturn(ApiResultResponse.of(HttpStatus.OK, null));

        // when, then
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/szs/refund")
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError());
    }
}
