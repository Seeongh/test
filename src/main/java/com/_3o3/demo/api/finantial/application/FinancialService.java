package com._3o3.demo.api.finantial.application;

import com._3o3.demo.api.finantial.application.dto.AnnualFinancialAllDTO;
import com._3o3.demo.api.finantial.application.dto.AnnualFinancialCreateDTO;
import com._3o3.demo.api.finantial.application.dto.IncomeDeductionCreateDTO;
import com._3o3.demo.api.finantial.application.dto.TaxDeductionCreateDTO;
import com._3o3.demo.api.finantial.application.dto.webClientDto.*;
import com._3o3.demo.api.finantial.domain.AnnualFinancial;
import com._3o3.demo.api.finantial.domain.IncomeDeduction;
import com._3o3.demo.api.finantial.domain.TaxDeduction;
import com._3o3.demo.api.finantial.infrastructure.FinancialRepository;
import com._3o3.demo.api.member.domain.Member;
import com._3o3.demo.api.member.infrastructure.MemberRepository;
import com._3o3.demo.common.ApiResponse;
import com._3o3.demo.common.exception.WebClientException;
import com._3o3.demo.security.Authentication.SignInDetails;
import com._3o3.demo.security.Authentication.provider.JwtTokenProvider;
import com._3o3.demo.util.AES256Util;
import com._3o3.demo.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly=true) //성능 최적화
@RequiredArgsConstructor
public class FinancialService {

    private final WebClientUtil webClientUtil;
    private final FinancialRepository financialRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    /**
     * 스크래핑
     * @param 회원 가입 객체
     * @return
     */
    @Transactional
    public ApiResponse<String> scrap(SignInDetails signInDetails) {
        // SecurityContext에서 JWT 토큰 추출
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String jwtToken = "Bearer " + (String) authentication.getCredentials(); ;
        Optional<Member> userOptional = memberRepository.findByUserId(signInDetails.getUsername());

        if (userOptional.isPresent()) {
            Member member = userOptional.get();
            RequestBodyDto requestBody = RequestBodyDto.builder()
                    .name(member.getName())
                    .regNo(member.getRegNoBirth() + "-" + AES256Util.decrypt(member.getRegNoEnc()))
                    .build();

            //비동기 통신
            ResponseBodyDto responseDto = webClientUtil.requestPostAnualFinancialData(jwtToken,"/scrap",requestBody);
            //응답 처리
            AnnualFinancial annualFinancial = processApiResponse(responseDto, member);
            if(annualFinancial == null) {
                throw new WebClientException("정보를 불러오지 못했습니다.");
            }
//            financialRepository.save(annualFinancial);
            return ApiResponse.of(HttpStatus.OK, "정보를 가져옵니다.");
        }
        else {
            return ApiResponse.of(HttpStatus.UNAUTHORIZED, "잘못된 접근입니다.");
        }

    }

     //결과 가공
    private AnnualFinancial processApiResponse(ResponseBodyDto response, Member member) {
        if (!response.getStatus().equals("success")) {
            return null;
        }

        Deduction deduction = response.getData().getDeduction(); //소득공제

        BigDecimal nationPensionTotal = deduction.getPension()
                .stream()
                .map(Pension::getDeductionAmount) // 공제액을 BigDecimal로 매핑
                .reduce(BigDecimal.ZERO, BigDecimal::add); // 직접 덧셈 연산

        BigDecimal creditTotal = deduction.getCreditCardDeduction().getMonth()
                .stream()
                .flatMap(month -> month.entrySet().stream()) // 모든 항목을 하나의 스트림으로 평면화
                .filter(entry -> !entry.getKey().equals("year")) // "year" 키를 제외한 항목만 필터링
                .map(entry -> new BigDecimal(entry.getValue().replace(",", ""))) // 문자열 값을 BigDecimal로 변환
                .reduce(BigDecimal.ZERO, BigDecimal::add); // 모든 값을 더함

        //연간재무 dto
        AnnualFinancialCreateDTO annualFinancialCreateDTO = AnnualFinancialCreateDTO
                                .builder()
                                .annualTotalAmount(response.getData().getTotalIncome())
                                .incomeYear(LocalDate.ofEpochDay(deduction.getCreditCardDeduction().getYear()))
                                .build();

        //소득공제 dto
        IncomeDeductionCreateDTO incomeDeductionCreateDTO = IncomeDeductionCreateDTO
                                .builder()
                                .nationPensionAmount(nationPensionTotal)
                                .creditCardAmount(creditTotal)
                                .build();

        //세액공제 dto
        TaxDeductionCreateDTO taxDeductionCreateDTO =  TaxDeductionCreateDTO
                                .builder()
                                .taxCreditAmount(deduction.getTaxDeduction())
                                .build();

        //엔티티로 변경
        AnnualFinancial annualFinancial = annualFinancialCreateDTO.toEntity();
        IncomeDeduction incomeDeduction = incomeDeductionCreateDTO.toEntity();
        TaxDeduction taxDeduction = taxDeductionCreateDTO.toEntity();

        annualFinancial.create(member, incomeDeduction, taxDeduction);
        return annualFinancial;
    }

    public ApiResponse<String> refund(SignInDetails signInDetails) {
        // 과세표준 == (종합 소득금액 - 소득공제)
        // 기본세율 == ((과세금액-과세표준) * 세율) + 기준별 금액
        // 산출세액 == 과세 표준 * 기본세율
        // 결정세액 == 산출세액 - 세액 공제
        AnnualFinancialAllDTO allDto = financialRepository.findByUserId(signInDetails.getUsername());
        log.info("ash in??? allDto {}" , allDto.toString());


        return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "");
    }

}

