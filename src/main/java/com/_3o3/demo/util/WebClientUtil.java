package com._3o3.demo.util;

import com._3o3.demo.api.tax.application.dto.RequestBodyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Request;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebClientUtil {

    private final WebClient webClient;

    public Map<Object, Object> requestPostAnualFinancialData(String jwtToken, String getUri, RequestBodyDto requestBodyDto) {

        Map<Object, Object> response = new HashMap<>();
        log.info("ash postgresquest :jwt {}, requestbodydto={}", jwtToken, requestBodyDto.toString());

        try{
             response =
                     webClient
                             .post()
                             .uri(getUri)
                              .header(HttpHeaders.AUTHORIZATION,"Bearer "+ jwtToken)
                             .body(Mono.just(requestBodyDto), RequestBodyDto.class)
                             .retrieve()
                             .bodyToMono(Map.class)
                             .block();
                           // .block();

        } catch (WebClientResponseException e) {
            log.error("Scraping failed: {} {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new RuntimeException("Scraping failed: " + e.getStatusCode() + " " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("An unexpected error occurred", e);
            throw new RuntimeException("An unexpected error occurred", e);
        }
        log.info(response.toString());
        return response;

        //확인
//        if(response.get("status").equals("success")) {
//            return response.get("data");
//        }
//        else {
//            return response.get("errors");
//        }
    }
}
