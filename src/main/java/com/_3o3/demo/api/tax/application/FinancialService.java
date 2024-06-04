package com._3o3.demo.api.tax.application;

import com._3o3.demo.api.tax.application.dto.AnnualFinancialAllDto;
import com._3o3.demo.api.tax.application.dto.RequestBodyDto;
import com._3o3.demo.api.tax.domain.AnnualFinancial;
import com._3o3.demo.api.tax.infrastructure.FinancialRepository;
import com._3o3.demo.api.user.domain.User;
import com._3o3.demo.api.user.infrastructure.UserRepository;
import com._3o3.demo.common.ApiResponse;
import com._3o3.demo.common.exception.CustomValidationException;
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
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly=true) //성능 최적화
@RequiredArgsConstructor
public class FinancialService {

    private final WebClientUtil webClientUtil;
    private final FinancialRepository financialRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    /**
     * 스크래핑
     * @param 회원 가입 객체
     * @return
     */
    @Transactional
    public ApiResponse<Long> scrap(SignInDetails signInDetails) {
        // SecurityContext에서 JWT 토큰 추출
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String jwtToken = "Bearer " + (String) authentication.getCredentials(); ;
        Optional<User> userOptional = userRepository.findByUserName(signInDetails.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            RequestBodyDto requestBody = RequestBodyDto.builder()
                    .name(user.getName())
                    .regNo(user.getRegNoBirth() + "-" + AES256Util.decrypt(user.getRegNoEnc()))
                    .build();

            Map<Object, Object> responseData = webClientUtil.requestPostAnualFinancialData(jwtToken, "/scrap", requestBody);
            AnnualFinancialAllDto allDto = saveFinancialData(responseData);
            log.info("ash annualfinal = {}", allDto.toString());
        }




        Long result = 1L;
        return result > 0 ? ApiResponse.of(HttpStatus.OK, result)
                : ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, result);

    }

    private AnnualFinancialAllDto saveFinancialData(Map<Object, Object> data) {
        Map<String, Object> dataMap = (Map<String, Object>) data.get("data");
        Map<String, Object> innerData = (Map<String, Object>) dataMap.get("소득공제");

        return AnnualFinancialAllDto.builder()
                .annualTotalAmount(new BigDecimal(dataMap.get("종합소득금액").toString()))
                .incomeYear((LocalDate) innerData.get("year"))
                .user(User.builder().name((String) dataMap.get("이름")).build())
                .nationPensionAmount(calculateNationPensionAmount(innerData))
                .creditCardAmount(calculateCreditCardAmount(innerData))
                .taxCreditAmount(new BigDecimal(dataMap.get("세액공제").toString()))
                .build();
    }
    private static BigDecimal calculateNationPensionAmount(Map<String, Object> innerData) {
        List<Map<String, Object>> nationPensionList = (List<Map<String, Object>>) innerData.get("국민연금");
        BigDecimal nationPensionAmount = BigDecimal.ZERO;
        for (Map<String, Object> pension : nationPensionList) {
            log.info("ash  국민연금 {}", pension.get("공제액").toString());
            nationPensionAmount = nationPensionAmount.add(new BigDecimal(pension.get("공제액").toString()));
        }
        return nationPensionAmount;
    }

    private static BigDecimal calculateCreditCardAmount(Map<String, Object> innerData) {
        List<Map<String, Object>> creditCardList = (List<Map<String, Object>>) innerData.get("신용카드소득공제");
        BigDecimal creditCardAmount = BigDecimal.ZERO;
        for (Map<String, Object> creditCard : creditCardList) {
            for (Map.Entry<String, Object> entry : creditCard.entrySet()) {
                if (!entry.getKey().equals("year")) {
                    creditCardAmount = creditCardAmount.add(new BigDecimal(entry.getValue().toString()));
                }
            }
        }
        return creditCardAmount;
    }
}

