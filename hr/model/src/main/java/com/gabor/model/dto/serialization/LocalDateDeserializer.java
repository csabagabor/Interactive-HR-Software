package com.gabor.model.dto.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.gabor.model.config.Constants;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.time.LocalDate;

@Slf4j
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctx) throws IOException {

        String str = p.getText();

        try {
            //remove time from the string
            int index = str.indexOf('T');

            if (index > 0) {
                str = str.substring(0, index);
            }

            return LocalDate.parse(str, Constants.DATE_FORMATTER);
        } catch (Exception e) {
            log.warn("date cannot be deserialized: {}", str);
            return null;
        }
    }
}