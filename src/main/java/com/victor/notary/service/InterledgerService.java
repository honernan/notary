package com.victor.notary.service;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-11-27  15：33
 * */

import java.math.BigInteger;
import java.util.Map;

public interface InterledgerService {
    /**
     * @param fromAddress
     * @param url
     * @param keyStore
     * @param to
     * @param value
     * @param flag
     * @return
     * @throws Exception
     */
    public Map<String, Object> interledgerSend(String fromAddress, String url, String keyStore, String to,
                                               BigInteger value, int flag) throws Exception;
}
