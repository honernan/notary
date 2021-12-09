package com.victor.notary.service;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-10-13  10：47
 * */

import com.victor.notary.model.NotaryBetweenEth;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Map;

public interface CrossChainService {

    /**转账
     * @param from   转账发起用户地址
     * @param to     转账接收用户地址
     * @param toHttp   转账接受所在区块链地址
     * @Param jsonFileName  转账发起用户对应的私钥文件名
     * @throws CipherException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws IOException
     */
    public EthBlock.TransactionObject sendMoney(String from, String to, String toHttp, String jsonFileName, long money, int flag)
            throws CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, IOException;

    /**根据区块号验证交易
     * @param sourceUrl
     * @param blockId
     * @param txHash
     * @param from
     * @param to
     * @param money
     * @return
     * @throws Exception
     */
    public boolean verifyTransactionByBlockid(String sourceUrl, long blockId, String txHash,
                                              String from, String to, BigInteger money) throws Exception;


    /**跨链过程中验证交易信息
     * @param txInfo
     * @param sourceUrl
     * @param from
     * @param to
     * @param money
     * @return
     * @throws Exception
     */
    public boolean verifyTransaction(EthBlock.TransactionObject txInfo, String sourceUrl, String from, String to, long money) throws Exception;

    /** 以太坊之间跨链转账，用于并发实验，交易金额随机生成
     * @param fromAddress
     * @param url
     * @param keyStore
     * @param to
     * @param value
     * @param flag
     * @param toNotary
     * @param badFlag  // 判断是否为恶意交易，若为4则为恶意
     * @throws Exception
     */
    public void localSendEther(String fromAddress, String url, String keyStore, String to,
                               BigInteger value, int flag, NotaryBetweenEth toNotary, int badFlag) throws Exception;


}
