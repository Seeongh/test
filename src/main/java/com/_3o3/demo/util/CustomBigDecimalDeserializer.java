package com._3o3.demo.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
public class CustomBigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        String value = p.getText();

        if (value != null) {
            value = value.replace(",", "");
        }
        return new BigDecimal(value);
    }
}
