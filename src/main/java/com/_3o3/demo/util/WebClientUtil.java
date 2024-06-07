package com._3o3.demo.util;

import com._3o3.demo.api.financial.application.dto.webClientDto.RequestBodyDto;
import com._3o3.demo.api.financial.application.dto.webClientDto.ResponseBodyDto;
import com._3o3.demo.common.exception.WebClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebClientUtil {

    private final WebClient webClient;
    private static final String BEARER_PREFIX = "Bearer ";

    public ResponseBodyDto requestPostAnualFinancialData(String jwtToken, String getUri, RequestBodyDto requestBodyDto) {

      try{
            return webClient.post()
                    .uri(getUri)
                    .header(HttpHeaders.AUTHORIZATION,BEARER_PREFIX+ jwtToken)
                    .body(Mono.just(requestBodyDto), RequestBodyDto.class)
                    .retrieve()
                    .bodyToMono(ResponseBodyDto.class)
                    .timeout(Duration.ofSeconds(25))
                    .block(); //동기 방식
        } catch (WebClientResponseException e) {
            throw new WebClientException("정보를 불러오지 못했습니다.");
        } catch(Exception e) {
            throw new WebClientException("정보를 불러오지 못했습니다.");
        }

    }
}
