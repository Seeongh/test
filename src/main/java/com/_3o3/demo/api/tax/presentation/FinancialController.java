package com._3o3.demo.api.tax.presentation;
import com._3o3.demo.api.tax.application.FinancialService;
import com._3o3.demo.api.user.domain.User;
import com._3o3.demo.common.ApiResponse;
import com._3o3.demo.security.Authentication.SignInDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 가입 및 로그인 처리
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/szs")
@PreAuthorize("hasRole('USER')")
public class FinancialController {

   private final FinancialService financialService;

    @PostMapping(value = "/scrap", consumes = "application/json")
    public ApiResponse<Long> scrap(@AuthenticationPrincipal SignInDetails signInDetails) {
        return financialService.scrap(signInDetails);
    }
}