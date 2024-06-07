package com._3o3.demo.api.financial.presentation;
import com._3o3.demo.api.financial.application.FinancialService;
import com._3o3.demo.api.financial.docs.FinancialControllerDocs;
import com._3o3.demo.common.ApiResultResponse;
import com._3o3.demo.security.Authentication.SignInDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 회원 가입 및 로그인 처리
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/szs")
@PreAuthorize("hasRole('USER')")
public class FinancialController implements FinancialControllerDocs {

   private final FinancialService financialService;

    @PostMapping(value = "/scrap")
    public ApiResultResponse<String> scrap(@AuthenticationPrincipal SignInDetails signInDetails) {
        log.info("FinancialController :: scrap");
        return financialService.scrap(signInDetails);
    }

    @GetMapping(value="/refund", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResultResponse<Map<String,String>> refund(@AuthenticationPrincipal SignInDetails signInDetails) {
        log.info("FinancialController :: refund");
        return financialService.refund(signInDetails);
    }
}
