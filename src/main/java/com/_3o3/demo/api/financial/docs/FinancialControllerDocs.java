package com._3o3.demo.api.financial.docs;

import com._3o3.demo.common.ApiResultResponse;
import com._3o3.demo.security.Authentication.SignInDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.Map;

@Tag(name = "재무정보 스크래핑 및 결정세액 조회", description = "재무정보 스크래핑 및 결정세액 조회")
public interface FinancialControllerDocs {
    @Operation(summary = "스크래핑", description = "accessToken 정보로 사용자의 재무정보 불러오기 수행.")
    @ApiResponse(
            responseCode = "200",
            description = "스크래핑 성공!",
            content = @Content(schema = @Schema(implementation = ApiResultResponse.class))
    )
    public ApiResultResponse<String> scrap(@AuthenticationPrincipal SignInDetails signInDetails);

    @Operation(summary = "결정 세액 조회", description = "accessToken 정보로 사용자의 결정 세액 조회.")
    @ApiResponse(
            responseCode = "200",
            description = "결정 세액 조회 성공!",
            content = @Content(schema = @Schema(implementation = ApiResultResponse.class))
    )
    public ApiResultResponse<Map<String, String>> refund(@AuthenticationPrincipal SignInDetails signInDetails);
}