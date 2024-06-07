package com._3o3.demo.controller;

import com._3o3.demo.api.member.application.MemberService;
import com._3o3.demo.api.member.application.dto.MemberCreateDTO;
import com._3o3.demo.api.member.application.dto.MemberSignInDTO;
import com._3o3.demo.api.member.application.dto.TockenDTO;
import com._3o3.demo.api.member.presentation.MemberController;
import com._3o3.demo.common.ApiResultResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Before
    public void before() {

    }
    @Test
    public void 회원가입_호출() throws Exception {
        // given
        /*MemberCreateDTO memberCreateDTO = MemberCreateDTO.builder()
                .userId("test")
                .password("test")
                .name("동탁")
                .regNo("921108-1582816")
                .build();*/

        when(memberService.signup(any(MemberCreateDTO.class))).thenReturn(ApiResultResponse.of(HttpStatus.OK, "동탁"));

        // when, then
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/szs/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"userId\": \"test\", \"password\": \"12345\", \"name\": \"동탁\", \"regNo\": \"921108-1582816\" }")
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

    }


    @Test
    public void 로그인_호출() throws Exception {
        // given

        /*MemberSignInDTO signInDTO = MemberSignInDTO.builder()
                .userId("test")
                .password("12345")
                .build();*/

        when(memberService.login(any(MemberSignInDTO.class))).thenReturn(new ApiResultResponse(HttpStatus.OK, "ok","successToken"));

        // when, then
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/szs/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"userId\": \"test\", \"password\": \"12345\" }")
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.responseMessage").value("ok"))
                .andExpect(jsonPath("$.data").value("successToken"));
    }
}
