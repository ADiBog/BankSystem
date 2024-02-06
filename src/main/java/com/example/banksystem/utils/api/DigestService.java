package com.example.banksystem.utils.api;

public interface DigestService {

    /**
     *  Хэшировать данные.
     *
     * @param string данные для хэширования.
     * @return даные после хэширования
     */
    String hash(String string);
}
