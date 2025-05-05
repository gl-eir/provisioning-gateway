package com.eir.pgm.services;

import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

public interface SystemConfigurationService {

    List<String> getActiveInstances();

    public String findByKey(String key) throws RuntimeException;

    public Integer findByKey(String key, int defaultValue);

    public String findByKey(String key, String defaultValue);

    public Float findByKey(String key, float defaultValue);
}
