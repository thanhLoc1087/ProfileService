package com.loc.common_service.common;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public class CommonFunction {

    @SneakyThrows
    public static void jsonValidate(InputStream inputStream, String jsonRequest)
    throws JsonProcessingException {
        JsonSchema schema = JsonSchemaFactory
            .getInstance(SpecVersion.VersionFlag.V7)
            .getSchema(inputStream);
        
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonRequest);
        Set<ValidationMessage> errors = schema.validate(jsonNode);
        Map<String, String> stringSetMap = new HashMap<>();
        for (ValidationMessage error : errors) {
            if (stringSetMap.containsKey(formatStringValidate(error.getPath()))) {
                String message = stringSetMap.get(formatStringValidate(error.getPath()));
                stringSetMap.put(formatStringValidate(error.getPath()), message + ", " + formatStringValidate(error.getMessage()));
            } else {
                stringSetMap.put(formatStringValidate(error.getPath()), formatStringValidate(error.getMessage()));
            }
        }
        if (!errors.isEmpty()) {
            throw new ValidateException("RQ01", stringSetMap, HttpStatus.BAD_REQUEST);
        }
    }

    public static String formatStringValidate(String message){
        return message.replaceAll("\\$.", "");
    }
}
