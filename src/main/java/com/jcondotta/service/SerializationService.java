package com.jcondotta.service;

import com.jcondotta.exception.SerializationException;
import io.micronaut.json.JsonMapper;
import jakarta.inject.Singleton;

import java.io.IOException;

@Singleton
public class SerializationService {

    private final JsonMapper jsonMapper;

    public SerializationService(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public <T> T deserialize(String json, Class<T> clazz) {
        try {
            return jsonMapper.readValue(json, clazz);
        }
        catch (IOException e) {
            throw new SerializationException("IO error during deserialization for class " + clazz.getSimpleName(), e);
        }
    }

    public String serialize(Object object) {
        try {
            return jsonMapper.writeValueAsString(object);
        }
        catch (IOException e) {
            throw new SerializationException("Failed to serialize object of type " + object.getClass().getSimpleName(), e);
        }
    }
}
