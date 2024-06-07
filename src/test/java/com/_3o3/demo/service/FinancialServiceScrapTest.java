package com._3o3.demo.service;

import com._3o3.demo.api.financial.application.FinancialService;
import com._3o3.demo.api.financial.domain.AnnualFinancial;
import com._3o3.demo.api.financial.infrastructure.FinancialRepository;
import com._3o3.demo.api.member.domain.Member;
import com._3o3.demo.api.member.infrastructure.MemberRepository;
import com._3o3.demo.common.ApiResultResponse;
import com._3o3.demo.common.exception.CustomValidationException;
import com._3o3.demo.security.Authentication.SignInDetails;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.WebUtils;

import java.util.Optional;

import static org.assertj.core.api.Fail.fail;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class FinancialServiceScrapTest {

    @Autowired
    FinancialService financialService;

    @MockBean
    FinancialRepository financialRepository;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    WebUtils webUtils;

    @WithMockUser("USER")
    @Test
    public void 스크램_정상_호출() throws Exception {
        // given
        AnnualFinancial annualFinancial = AnnualFinancial.builder().id(1L).build();
        Member member = Member.builder().userId("test").password("123").name("동탁").regNoBirth("921108").regNoEnc("IQYiL5yGwL0DbHVCbJL//Q==").build();
        SignInDetails signdto = new SignInDetails(member);
        String userId ="test";
        // when
        when(memberRepository.findByUserId(userId)).thenReturn(Optional.ofNullable(member));
        when(financialRepository.save(annualFinancial)).thenReturn(null);
        //then
        HttpStatus httpStatus = financialService.scrap(signdto).getHttpStatus();

        Assertions.assertThat(httpStatus).isEqualTo(HttpStatus.OK);
    }


    @WithMockUser("USER")
    @Test(expected = CustomValidationException.class)
    public void 스크램_비정상_호출() throws Exception {
        // given
        AnnualFinancial annualFinancial = AnnualFinancial.builder().id(1L).build();
        Member member = Member.builder().userId("test").password("123").name("동탁").regNoBirth("921109").regNoEnc("IQYiL5yGwL0DbHVCbJL//Q==").build();
        SignInDetails signdto = new SignInDetails(member);
        String userId ="test";
        // when
        when(memberRepository.findByUserId(userId)).thenReturn(Optional.ofNullable(member));
        when(financialRepository.save(annualFinancial)).thenReturn(null);
        //then
        HttpStatus httpStatus = financialService.scrap(signdto).getHttpStatus();

        fail("정보 호출 실패");
    }

}
