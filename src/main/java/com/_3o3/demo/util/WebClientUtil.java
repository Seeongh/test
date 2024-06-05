package com._3o3.demo.util;

import com._3o3.demo.api.finantial.application.dto.webClientDto.RequestBodyDto;
import com._3o3.demo.api.finantial.application.dto.webClientDto.ResponseBodyDto;
import com._3o3.demo.common.exception.WebClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebClientUtil {

    private final WebClient webClient;

    public ResponseBodyDto requestPostAnualFinancialData(String jwtToken, String getUri, RequestBodyDto requestBodyDto) {

      try{
            return webClient.post()
                    .uri(getUri)
                    .header(HttpHeaders.AUTHORIZATION,"Bearer "+ jwtToken)
                    .body(Mono.just(requestBodyDto), RequestBodyDto.class)
                    .retrieve()
                    .bodyToMono(ResponseBodyDto.class)
                    .block(); //비동기 방식
            //log.info(response.toString());

        } catch (WebClientResponseException e) {
            log.error("Scraping failed: {} {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new WebClientException("정보를 불러오지 못했습니다.");
        } catch (Exception e) {
            log.error("An unexpected error occurred", e);
            throw new WebClientException("정보를 불러오지 못했습니다.");
        }

    }
}
