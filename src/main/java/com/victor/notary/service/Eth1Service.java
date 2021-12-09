package com.victor.notary.service;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-10-20  19：35
 * */

import com.victor.notary.model.EthUser1;
import com.victor.notary.model.SamebeginOfCredit;

import java.math.BigInteger;
import java.util.List;

public interface Eth1Service {
    /** 根据地址更新用户余额
     * @param money
     * @param userAddress
     */
    public void updateMoney(String money, String userAddress);

    /** 一条以太坊链内带nonce的转账
     * @param fromAddress
     * @param url
     * @param keyStore
     * @param to
     * @param value
     * @throws Exception
     */
    public void localSendEther(String fromAddress, String url, String keyStore, String to,
                               BigInteger value) throws Exception;

    /**获取以太坊1链所有的用户信息
     * @return
     */
    public List<EthUser1> getAllUser();
}
