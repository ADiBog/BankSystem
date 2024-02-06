package com.example.banksystem.utils;

import com.example.banksystem.utils.api.DigestService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * Реализация сервиса по хэшированию данных.
 */
@Component
public class DigestServiceImpl implements DigestService {

    @Override
    public String hash(String string) {
        return DigestUtils.md5Hex(string);
    }
}
