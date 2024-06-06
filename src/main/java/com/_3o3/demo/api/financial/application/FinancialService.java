package com._3o3.demo.api.financial.application;

import com._3o3.demo.api.financial.application.dto.*;
import com._3o3.demo.api.financial.application.dto.webClientDto.*;
import com._3o3.demo.api.financial.domain.*;
import com._3o3.demo.api.financial.infrastructure.AnnualTaxRepository;
import com._3o3.demo.api.financial.infrastructure.FinancialRepository;
import com._3o3.demo.api.financial.infrastructure.TaxRepository;
import com._3o3.demo.api.member.domain.Member;
import com._3o3.demo.api.member.infrastructure.MemberRepository;
import com._3o3.demo.common.ApiResponse;
import com._3o3.demo.common.exception.CustomValidationException;
import com._3o3.demo.common.exception.WebClientException;
import com._3o3.demo.security.Authentication.SignInDetails;
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
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly=true) //성능 최적화
@RequiredArgsConstructor
public class FinancialService {

    private final WebClientUtil webClientUtil;
    private final FinancialRepository financialRepository;
    private final MemberRepository memberRepository;
    private final TaxRepository taxRepository;
    private final AnnualTaxRepository annualTaxRepository;

    private static final String RESP_SUCCESS = "success";
    /**
     * 스크래핑
     * @param 회원 가입 객체
     * @return
     */
    @Transactional
    public ApiResponse<String> scrap(SignInDetails signInDetails) {
        // SecurityContext에서 JWT 토큰 추출
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String jwtToken = (String) authentication.getCredentials();
        Member member = memberRepository.findByUserId(signInDetails.getUsername())
                .orElseThrow(() -> new CustomValidationException("잘못된 접근입니다."));


        //요청 생성
        RequestBodyDto requestBody = RequestBodyDto.builder()
                .name(member.getName())
                .regNo(member.getRegNoBirth() + "-" + AES256Util.decrypt(member.getRegNoEnc()))
                .build();

        //비동기 통신
        ResponseBodyDto responseDto = webClientUtil.requestPostAnualFinancialData(jwtToken,"/scrap",requestBody);
        log.info("ash responseDto {}" , responseDto.toString());
        //응답 처리
        AnnualFinancial annualFinancial = processApiResponse(responseDto, member);
        if(annualFinancial == null) {
            throw new WebClientException("정보를 불러오지 못했습니다.");
        }

        financialRepository.save(annualFinancial);
        return ApiResponse.of(HttpStatus.OK, "정보를 불러오는데 성공했습니다.");
    }

    /**
     * 스크래핑 결과 가공
     * @param response
     * @param member
     * @return
     */
    private AnnualFinancial processApiResponse(ResponseBodyDto response, Member member) {

        if (!response.getStatus().equals(RESP_SUCCESS)) {
            throw new CustomValidationException("정보를 불러오는데 실패했습니다.");
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
                                .incomeYear(deduction.getCreditCardDeduction().getYear())
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

        return saveAnnualFinancailData(annualFinancialCreateDTO, incomeDeductionCreateDTO, taxDeductionCreateDTO, member);
    }

    /**
     * 사용자가 조회한 결과 DB저장
     * @param annualFinancialCreateDTO
     * @param incomeDeductionCreateDTO
     * @param taxDeductionCreateDTO
     * @param member
     * @return
     */
    private AnnualFinancial saveAnnualFinancailData( AnnualFinancialCreateDTO annualFinancialCreateDTO,
                                          IncomeDeductionCreateDTO incomeDeductionCreateDTO,
                                          TaxDeductionCreateDTO taxDeductionCreateDTO ,
                                          Member member) {

        //엔티티로 변경
        AnnualFinancial annualFinancial = annualFinancialCreateDTO.toEntity();
        IncomeDeduction incomeDeduction = incomeDeductionCreateDTO.toEntity();
        TaxDeduction taxDeduction = taxDeductionCreateDTO.toEntity();

        annualFinancial.create(member, incomeDeduction, taxDeduction);
        return annualFinancial;
    }

    /**
     * refund (결정세액 조회)
     * @param signInDetails
     * @return
     */
    public ApiResponse<Map<String,String>> refund(SignInDetails signInDetails) {
        return financialRepository.findByUserId(signInDetails.getUsername())
                .map(this::calculateAndSaveTax)
                .orElseGet(() -> ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null));
    }

    /**
     * 과세표준, 기본세율, 산출세액, 결정세액 계산
     * @param taxDto
     * @return
     */
    private ApiResponse<Map<String, String>> calculateAndSaveTax(AnnualFinancialAllDTO taxDto) {
        // 과세표준 == (종합 소득금액 - 소득공제)
        // 기본세율 == ((과세표준-초과기준금액) * 세율) + 기준별 금액
        // 산출세액 == 과세 표준 * 기본세율
        // 결정세액 == 산출세액 - 세액 공제
        BigDecimal taxIncome = calculateTax(taxDto.getAnnualTotalAmount(), taxDto.getTotalDeductionAmount(), calculateMethod.SUBTRACT);
        TaxRate taxRate = taxRepository.findFirstByTaxStandardLessThanEqualOrderByTaxStandardDesc(taxIncome)
                .orElseThrow(() -> new CustomValidationException("세액 계산을 위한 기준을 찾을 수 없습니다."));

        //산출세액
        BigDecimal calculatedTax = calculateCalculatedTax(taxIncome, taxRate);
        // 결정세액
        BigDecimal finalTax = calculateTax(calculatedTax, taxDto.getTaxCreditAmount(), calculateMethod.SUBTRACT);

        //산출 내용 저장
        AnnualTaxCreateDTO annualTaxCreateDTO  = AnnualTaxCreateDTO
                                            .builder()
                                            .taxIncome(taxIncome)
                                            .calculatedTax(calculatedTax)
                                            .finalTaxAmount(finalTax)
                                            .taxCalculationYear(taxDto.getIncomeYear())
                                            .build();
        saveAnnualTax(annualTaxCreateDTO);

        //결정세액 포매팅
        String formattedFinalTax = formatCurrency(finalTax);

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("결정세액", formattedFinalTax);

        return ApiResponse.of(HttpStatus.OK, resultMap);
    }

    /**
     * 산출 세액 계산 ((종합소득과세표준 - 과세 초과기준 금액) * 세율) + 추가금
     * @param taxIncome
     * @param taxRate
     * @return
     */
    private BigDecimal calculateCalculatedTax(BigDecimal taxIncome, TaxRate taxRate) {
        //초과분 구하기 (과세표준 - 과세 초과 기준 금액) - 추후 기준금액과 초과금액이 달라질 경우 대비
        BigDecimal taxExcessThreshold = calculateTax(taxIncome, taxRate.getExcessAmountThreshold(), calculateMethod.SUBTRACT);
        //초과분에 세금 퍼센트 계산
        BigDecimal excessTaxRate = calculateTax(taxExcessThreshold, taxRate.getTaxRatePercentage(), calculateMethod.MULTIPLY);
        //위 게산에 더해야하는 세금 더하기
        return calculateTax(excessTaxRate, taxRate.getAdditionalAmount(), calculateMethod.ADD);
    }

    /**
     * 개인 정보 저장
     * @param annualTaxCreateDTO
     */
    private void saveAnnualTax( AnnualTaxCreateDTO annualTaxCreateDTO ) {
        AnnualTax annualTax =annualTaxCreateDTO.toEntity();
        annualTaxRepository.save(annualTax);
    }

    /**
     * Bigdemical 계산
     * @param c1
     * @param c2
     * @param carMethod
     * @return
     */
    private BigDecimal calculateTax(BigDecimal c1, BigDecimal c2, calculateMethod carMethod) {
        BigDecimal calresult = BigDecimal.ZERO;

        switch(carMethod) {
            case SUBTRACT:
                calresult = c1.subtract(c2);
                break;
            case ADD:
                calresult = c1.add(c2);
                break;
            case MULTIPLY:
                calresult = c1.multiply(c2);
                break;
            case DIVIDE:
                // 0으로 나누는 경우 방지하기 위해 예외처리 추가 필요
                if (c2.compareTo(BigDecimal.ZERO) != 0) {
                    calresult = c1.divide(c2, RoundingMode.HALF_UP);
                }
                break;
        }

        //반올림
        return calresult.setScale(0, RoundingMode.HALF_UP);
    }

    /**
     * 결정세액 포맷팅
     * @param amount
     * @return
     */
    private String formatCurrency(BigDecimal amount) {
        //값에 천단위 쉼표를 붙여줌
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        return decimalFormat.format(amount);
    }
}

