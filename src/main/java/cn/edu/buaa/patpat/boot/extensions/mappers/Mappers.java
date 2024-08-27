package cn.edu.buaa.patpat.boot.extensions.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@Slf4j
public class Mappers {
    private final ModelMapper modelMapper;
    private final ObjectMapper jsonMapper;
    private final ObjectMapper yamlMapper;

    public Mappers(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.jsonMapper = getJsonMapper();
        this.yamlMapper = getYamlMapper();
    }

    private static ObjectMapper getJsonMapper() {
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    private static ObjectMapper getYamlMapper() {
        var mapper = new ObjectMapper(new YAMLFactory());
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);
        return mapper;
    }

    public String toJson(Object object) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(object);
    }

    public String toJson(Object object, String defaultValue) {
        try {
            return jsonMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize object: {}", object, e);
            return defaultValue;
        }
    }

    public Object fromJson(String json) throws JsonProcessingException {
        return jsonMapper.readValue(json, Object.class);
    }

    public <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return jsonMapper.readValue(json, clazz);
    }

    public <T> T fromJson(String json, Class<T> clazz, T defaultValue) {
        try {
            return jsonMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse JSON: {}", json, e);
            return defaultValue;
        }
    }

    public <T> T fromJsonFile(String path, Class<T> clazz) throws IOException {
        return fromJson(Files.readString(Path.of(path)), clazz);
    }

    public String toYaml(Object object) throws JsonProcessingException {
        return yamlMapper.writeValueAsString(object);
    }

    public String toYaml(Object object, String defaultValue) {
        try {
            return yamlMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize object: {}", object, e);
            return defaultValue;
        }
    }

    public <T> T fromYaml(String yaml, Class<T> clazz) throws JsonProcessingException {
        return yamlMapper.readValue(yaml, clazz);
    }

    public <T> T fromYaml(String yaml, Class<T> clazz, T defaultValue) {
        try {
            return yamlMapper.readValue(yaml, clazz);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse YAML: {}", yaml, e);
            return defaultValue;
        }
    }

    public <T> T fromYamlFile(String path, Class<T> clazz) throws IOException {
        return fromYaml(Files.readString(Path.of(path)), clazz);
    }

    public <T> T map(Object source, Class<T> destinationType) {
        return modelMapper.map(source, destinationType);
    }

    public <T> void map(Object source, T destination) {
        modelMapper.map(source, destination);
    }
}
