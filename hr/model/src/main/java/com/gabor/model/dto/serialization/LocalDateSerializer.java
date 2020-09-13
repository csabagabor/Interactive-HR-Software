package com.gabor.model.dto.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gabor.model.config.Constants;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;

@Slf4j
public class LocalDateSerializer extends JsonSerializer<LocalDate> {

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try {
            gen.writeString(value.format(Constants.DATE_FORMATTER));
        } catch (Exception e) {
            log.warn("date cannot be serialized: {}", value);
            gen.writeString("");
        }
    }
}