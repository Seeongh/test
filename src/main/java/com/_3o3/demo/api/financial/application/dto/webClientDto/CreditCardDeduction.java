package com._3o3.demo.api.financial.application.dto.webClientDto;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class CreditCardDeduction {

    private List<Map<String, String>> month;
    private int year;

}
