package com.adanali.library.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class JsonStorageUtil <T> {
    private final ObjectMapper objectMapper;
    private final File filePath;

    public JsonStorageUtil(String filePath) {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE);
        this.filePath = new File(filePath);
    }

    public Set<T> loadData(TypeReference<Set<T>> typeRef) throws IOException {
        if (filePath.exists() && filePath.length() > 0){
            try {
                return objectMapper.readValue(filePath, typeRef);
            } catch (IOException e) {
                throw new IOException("Failed to load data from the Database", e);
            }
        }else return new HashSet<>();
    }

    public void saveData(Collection<T> collection) throws IOException {
            try {
                File parent = filePath.getParentFile();
                if (parent != null && !parent.exists()){
                    if (!parent.mkdirs()){
                        throw new IOException("Could not create directories for path: "+parent.getAbsolutePath());
                    }
                }
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath,collection);
            } catch (IOException e) {
                throw new IOException("Failed to save data to the Database", e);
            }
    }
}