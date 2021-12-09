package com.victor.notary.service.impl;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-10-13  10：55
 * */

import com.victor.notary.mapper.EthUser1Mapper;
import com.victor.notary.mapper.EthUser2Mapper;
import com.victor.notary.mapper.NotaryBetweenEthMapper;
import com.victor.notary.model.NotaryBetweenEth;
import com.victor.notary.service.CrossChainService;
import org.springframework.stereotype.Service;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "crossChainService")
public class CrossChainServiceImpl implements CrossChainService {
    // 区块链地址
    private final String EHT_1 = "http://172.23.17.134:8547";
    private final String EHT_2 = "http://172.23.17.136:8549";
    //private final String HYPER = "";

    @Resource
    private EthUser1Mapper ethUser1Mapper;
    @Resource
    private EthUser2Mapper ethUser2Mapper;
    @Resource
    private NotaryBetweenEthMapper notaryBetweenEthMapper;

    //转账
    @Override
    //@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public EthBlock.TransactionObject sendMoney(String fromAddress, String toAddress, String url, String jsonFileName, long money, int flag)
            throws CipherException, IOException {
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
            personalUnlockAccount = web3.personalUnlockAccount(fromAddress,password).sendAsync().get();
        }catch (Exception e) {
            e.getMessage();
        }
        Credentials credentials = null;

        if (personalUnlockAccount == null){
            //转账人私钥
            credentials = WalletUtils.loadCredentials(password,jsonFileName);
        }else if(personalUnlockAccount.accountUnlocked()) {
            //转账人私钥
            credentials = WalletUtils.loadCredentials(password,jsonFileName);
        }
        TransactionReceipt transactionReceipt = null;
        RawTransaction transaction = null;
        try {
            transactionReceipt = Transfer.sendFunds(web3, credentials, toAddress, BigDecimal.valueOf(money),
                    Convert.Unit.ETHER).send();
            System.out.println("挖矿完成");
        }catch (Exception e) {
            e.printStackTrace();
        }

        EthGetBalance ethGetBalanceFrom = null;
        EthGetBalance ethGetBalance1To = null;
        // 获取余额
        try {
            ethGetBalanceFrom = web3
                    .ethGetBalance(fromAddress, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            ethGetBalance1To = web3
                    .ethGetBalance(toAddress, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
        }catch (Exception e) {
            e.printStackTrace();
        }

        BigInteger weiFrom = ethGetBalanceFrom.getBalance();
        System.out.println("交易后" + fromAddress + "--账户余额为："+ weiFrom);
        BigInteger weiTo = ethGetBalance1To.getBalance();
        System.out.println("交易后" + toAddress + "---账户余额为："+ weiTo);

        // 更新用户表余额
        switch (url) {
            case EHT_1 :
                int numFrom1 = ethUser1Mapper.updateMoneyByUserAddr(fromAddress, String.valueOf(weiFrom));
                int numTo1 = ethUser1Mapper.updateMoneyByUserAddr(toAddress, String.valueOf(weiTo));
            case EHT_2 :
                int numFrom2 = ethUser2Mapper.updateMoneyByUserAddr(fromAddress, String.valueOf(weiFrom));
                int numTo2 = ethUser2Mapper.updateMoneyByUserAddr(toAddress, String.valueOf(weiTo));
             // TODO
             // case HYPER :
        }
        // 更新公证人组表余额
        switch (flag) {
            // 0代表源链交易（以太坊）
            case 0 :
                int numFrom1 = notaryBetweenEthMapper.updateMoney1ByUserAddr1(fromAddress, String.valueOf(weiFrom));
                int numTo1 = notaryBetweenEthMapper.updateMoney1ByUserAddr1(toAddress, String.valueOf(weiTo));
           // 1代表目标链交易（以太坊）
            case 1 :
                int numFrom2 = notaryBetweenEthMapper.updateMoney2ByUserAddr2(fromAddress, String.valueOf(weiFrom));
                int numTo2 = notaryBetweenEthMapper.updateMoney2ByUserAddr2(toAddress, String.valueOf(weiTo));
                // TODO
                // 3代表源链交易（超级账本）
                // case 3 :
                // 4代表目标链交易（超级账本）
                // case 4 :
        }
        // 传递该笔交易信息

        EthBlock.TransactionObject txObj = new EthBlock.TransactionObject();
        /*txObj.setHash(transactionReceipt.getTransactionHash());
        txObj.setFrom(transactionReceipt.getFrom());
        txObj.setTo(transactionReceipt.getTo());
        txObj.setValue(String.valueOf(money));*/
        return txObj;
    }


    /**  带nonce的转账
     * @param fromAddress
     * @param url
     * @param keyStore
     * @param to
     * @param value
     * @throws Exception
     */
    @Override
    public void localSendEther(String fromAddress, String url, String keyStore, String to,
                               BigInteger value, int flag, NotaryBetweenEth toNotary, int badFlag) throws Exception
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


        /*恶意公证人的节点信誉值置为0*/
        if (badFlag == 4) {
            try {
                notaryBetweenEthMapper.updateCreditById(toNotary.getId(), 0, 0);
                System.out.println("更新信誉值成功！！！");
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("更新信誉值失败！！！");
            }
            System.out.println("保证金池账户发起补偿交易成功！！！");
        } else {
            System.out.println("提交交易成功！！！");
            //第二笔交易是更新公证人信誉值
            if (flag == 1) {
                int credit1 = toNotary.getCredit1();
                int credit2 = toNotary.getCredit2();
                credit1 += 1;
                credit2 += 1;
                try {
                    notaryBetweenEthMapper.updateCreditById(toNotary.getId(), credit1, credit2);
                    System.out.println("更新信誉值成功！！！");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*根据区块号验证区块链上交易*/
    @Override
    public boolean verifyTransactionByBlockid(String sourceUrl, long blockId, String txHash,
                                             String from, String to, BigInteger money) throws Exception {
        Web3j web3 = Web3j.build(new HttpService(sourceUrl));

        // 根据区块号获取区块信息
        EthBlock.Block blockInfo = null;
        try {
            blockInfo = web3
                    .ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockId)),true)
                    .send().getBlock();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EthBlock.TransactionResult> transactionResults = blockInfo.getTransactions();
        if (transactionResults.size() > 0) {
            for (int i = 0; i < transactionResults.size(); i++) {
                EthBlock.TransactionObject transactionObject = (EthBlock.TransactionObject) transactionResults.get(i);
                String transactionHash = transactionObject.getHash();
                String senderAddress = transactionObject.getFrom();
                String receiverAddress = transactionObject.getTo();
                BigInteger value = transactionObject.getValue();
                if (transactionHash.equals(txHash) && value.equals(money) && senderAddress.equals(from) && receiverAddress.equals(to)) {
                    return true;
                }
            }

        }

        return false;
    }

    /*跨链过程中验证交易信息*/
    @Override
    public boolean verifyTransaction(EthBlock.TransactionObject txInfo, String sourceUrl,
                                     String from, String to, long money) throws Exception {
        Web3j web3 = Web3j.build(new HttpService(sourceUrl));

        if (txInfo.getValue().equals(String.valueOf(money)) && txInfo.getFrom().equals(from) && txInfo.getTo().equals(to)) {
            return true;
        }

        return false;
    }
}
