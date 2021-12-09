package com.victor.notary.service.impl;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-10-20  19：36
 * */

import com.victor.notary.mapper.EthUser1Mapper;
import com.victor.notary.mapper.SamebeginOfCreditMapper;
import com.victor.notary.model.EthUser1;
import com.victor.notary.model.SamebeginOfCredit;
import com.victor.notary.service.Eth1Service;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.utils.Numeric;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service(value = "eth1Service")
public class Eth1ServiceImpl implements Eth1Service {
    @Resource
    private EthUser1Mapper ethUser1Mapper;
    /** 根据地址更新用户余额
     * @param money
     * @param userAddress
     */
    public void updateMoney(String money, String userAddress) {
        ethUser1Mapper.updateMoneyByUserAddr(userAddress, money);
    }


    /**  一条以太坊链内带nonce的转账
     * @param fromAddress
     * @param url
     * @param keyStore
     * @param to
     * @param value
     * @throws Exception
     */
    @Override
    public void localSendEther(String fromAddress, String url, String keyStore, String to,
                               BigInteger value) throws Exception
    {
        Admin web3 = Admin.build(new HttpService(url));
        // 获取账户密码
        String password = ethUser1Mapper.selectByUserAddress(fromAddress).getPassword();
        // 通过admin进行账户解锁
        PersonalUnlockAccount personalUnlockAccount = null;
        try {
            personalUnlockAccount = web3.personalUnlockAccount(fromAddress,password).send();
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
        BigInteger gasLimit = Contract.GAS_LIMIT;
        //生成RawTransaction交易对象
        RawTransaction rawTransaction  = RawTransaction.createTransaction(nonce,gasPrice,gasLimit,to,value,"abcde123");//可以额外带数据
        //使用Credentials对象对RawTransaction对象进行签名
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();
        //String transactionHash = ethSendTransaction.getTransactionHash();
        EthGetTransactionReceipt send = null;
        if(ethSendTransaction.hasError())
        {
            String message=ethSendTransaction.getError().getMessage();
            System.out.println("transaction failed,info:"+message);
        }
        else
        {
            String hash=ethSendTransaction.getTransactionHash();
            send = web3.ethGetTransactionReceipt(hash).send();
        }

        System.out.println("以太坊链--1---提交交易成功！！！");
        EthGetBalance ethGetBalanceFrom = null;
        EthGetBalance ethGetBalance1To = null;
        // 获取余额
        try {
            ethGetBalanceFrom = web3
                    .ethGetBalance(fromAddress, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            ethGetBalance1To = web3
                    .ethGetBalance(to, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
        }catch (Exception e) {
            e.printStackTrace();
        }

        // 更新ehtuser1的金额
        this.updateMoney(String.valueOf(ethGetBalanceFrom.getBalance()), fromAddress);
        this.updateMoney(String.valueOf(ethGetBalance1To.getBalance()), to);
        System.out.println("更新余额成功！！！");

    }

    /**获取以太坊1链所有的用户信息
     * @return
     */
    @Override
    public List<EthUser1> getAllUser() {
        return ethUser1Mapper.selectAll();
    }

}
