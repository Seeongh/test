package com._3o3.demo.service;

import com._3o3.demo.api.financial.application.FinancialService;
import com._3o3.demo.api.financial.application.dto.AnnualFinancialAllDTO;
import com._3o3.demo.api.financial.domain.AnnualFinancial;
import com._3o3.demo.api.financial.domain.TaxRate;
import com._3o3.demo.api.financial.infrastructure.FinancialRepository;
import com._3o3.demo.api.financial.infrastructure.TaxRepository;
import com._3o3.demo.api.member.domain.Member;
import com._3o3.demo.api.member.infrastructure.MemberRepository;
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
import org.springframework.web.util.WebUtils;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Fail.fail;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class FinancialServiceRefundTest {

    @Autowired
    FinancialService financialService;

    @MockBean
    FinancialRepository financialRepository;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    TaxRepository taxRepository;

    @WithMockUser("USER")
    @Test
    public void 결정세액_조회() throws Exception {
        // given
        AnnualFinancialAllDTO allDto = AnnualFinancialAllDTO.builder()
                .annualTotalAmount(new BigDecimal("20000000"))
                .totalDeductionAmount(new BigDecimal("0"))
                .creditCardAmount(new BigDecimal("0"))
                .nationPensionAmount(new BigDecimal("0"))
                .incomeYear(Year.parse("2023"))
                .taxCreditAmount(new BigDecimal("0"))
                .build();

        TaxRate taxRate = TaxRate.builder()
                .taxRatePercentage(BigDecimal.valueOf(0.15))
                .additionalAmount(new BigDecimal("840000"))
                .excessAmountThreshold(new BigDecimal("14000000"))
                .taxStandard(new BigDecimal("14000000")).build();

        AnnualFinancial annualFinancial = AnnualFinancial.builder().id(1L).build();
        Member member = Member.builder().userId("test").password("123").name("동탁").regNoBirth("921108").regNoEnc("IQYiL5yGwL0DbHVCbJL//Q==").build();
        SignInDetails signdto = new SignInDetails(member);
        String userId ="test";
        // when
        when(financialRepository.findByUserId(userId)).thenReturn(Optional.ofNullable(allDto));
        when(taxRepository.findFirstByTaxStandardLessThanEqualOrderByTaxStandardDesc(new BigDecimal("20000000"))).thenReturn(Optional.ofNullable(taxRate));
        //then
        Map<String,String> resultMap = financialService.refund(signdto).getData();

        Assertions.assertThat(resultMap.get("결정세액")).isEqualTo("1,740,000");
    }

    @WithMockUser("USER")
    @Test(expected = CustomValidationException.class)
    public void 결정세액_조회_실패() throws Exception {
        // given
        AnnualFinancialAllDTO allDto = null;

        TaxRate taxRate =null;

        AnnualFinancial annualFinancial = AnnualFinancial.builder().id(1L).build();
        Member member = Member.builder().userId("test").password("123").name("동탁").regNoBirth("921108").regNoEnc("IQYiL5yGwL0DbHVCbJL//Q==").build();
        SignInDetails signdto = new SignInDetails(member);
        String userId ="test";
        // when
        when(financialRepository.findByUserId(userId)).thenReturn(Optional.ofNullable(allDto));
         //then
        Map<String,String> resultMap = financialService.refund(signdto).getData();

        fail("데이터 없음 오류");
    }

}
