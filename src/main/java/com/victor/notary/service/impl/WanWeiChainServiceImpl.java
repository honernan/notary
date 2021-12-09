package com.victor.notary.service.impl;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-11-26  15：49
 * */

import com.victor.notary.mapper.EthUser1Mapper;
import com.victor.notary.mapper.EthUser2Mapper;
import com.victor.notary.service.WanWeiChainService;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.utils.Numeric;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
@Service(value = "wanWeiChainService")
public class WanWeiChainServiceImpl implements WanWeiChainService {
    @Resource
    private EthUser1Mapper ethUser1Mapper;

    @Resource
    private EthUser2Mapper ethUser2Mapper;

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
    @Override
    public Map<String, Object> wanWeiChainSend(String fromAddress, String url, String keyStore, String to,
                                               BigInteger value, int flag) throws Exception
    {
        Admin web3 = Admin.build(new HttpService(url));
        // 获取账户密码
        String password = "";
        if (flag == 0) {
            password = ethUser1Mapper.selectByUserAddress(fromAddress).getPassword();
        } else {
            password = ethUser2Mapper.selectByUserAddress(fromAddress).getPassword();
        }
        // 通过admin进行账户解锁
        PersonalUnlockAccount personalUnlockAccount = null;
        try {
            personalUnlockAccount = web3.personalUnlockAccount(fromAddress,password).send();
            //.sendAsync().get();
        }catch (Exception e) {
            e.printStackTrace();
        }
        if (personalUnlockAccount != null){
            personalUnlockAccount.accountUnlocked();
        }
        //加载本地KeyStore文件生成Credentials对象
        Credentials credentials = WalletUtils.loadCredentials(password,keyStore);
        EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(
                fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        BigInteger gasPrice = new BigInteger("12000000000");
        BigInteger gasLimit = Contract.GAS_LIMIT/*.divide(new BigInteger("2"))*/;
        //生成RawTransaction交易对象
        RawTransaction rawTransaction  = RawTransaction.createTransaction(nonce,gasPrice,gasLimit,to,value,"abcde123");//可以额外带数据
        //使用Credentials对象对RawTransaction对象进行签名
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();
        String transactionHash = ethSendTransaction.getTransactionHash();
        // 获取区块号
        BigInteger blockid = web3.ethGetTransactionByHash(transactionHash).send().getTransaction().get().getBlockNumber();

        if(ethSendTransaction.hasError())
        {
            String message=ethSendTransaction.getError().getMessage();
            System.out.println("transaction failed,info:"+message);
        }
        else
        {
            String hash=ethSendTransaction.getTransactionHash();
            EthGetTransactionReceipt send = web3.ethGetTransactionReceipt(hash).send();
        }
        // 返回的交易结果集包含该交易的哈希码以及交易所在区块号
        Map<String, Object> resMap = new HashMap<>(2);
        resMap.put("transactionHash", transactionHash);
        resMap.put("blockid", blockid);
        System.out.println("提交交易成功！！！");
        return resMap;
    }
}
