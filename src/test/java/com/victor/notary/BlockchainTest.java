package com.victor.notary;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-10-03  10：11
 * */

import org.junit.runner.RunWith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.protocol.Web3j;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BlockchainTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlockchainTest.class);
    @Autowired
    Web3j web3j;

    /*@Test
    public void sendMoney() throws CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        LOGGER.info("get the money of accounts...");
        // 实例化智能合约
        BigInteger a = new BigInteger("6");
       // RemoteCall<TransactionReceipt> receiptRemoteCall = trade.sendcoin("0xe3d59dc0327a346ecbcf45f3409f1040a192a4fd", BigInteger.valueOf(6), BigInteger.valueOf(6));
        // connect to node
        Web3j web3 = Web3j.build(new HttpService("http://172.23.17.134:8547"));

        List<EthBlock.TransactionResult> txs = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST,true).send()
                .getBlock().getTransactions();
        EthAccounts ethAccounts =  web3.ethAccounts().send();
        List<String> accountList = ethAccounts.getResult();
        boolean flag = accountList.contains("0xe3d59dc0327a346ecbcf45f3409f1040a192a4fd");
        // send asynchronous requests to get balance
        EthGetBalance ethGetBalance = null;
        try {
            // 获取余额
            ethGetBalance = web3
                    .ethGetBalance("0xe3d59dc0327a346ecbcf45f3409f1040a192a4fd", DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();

        }catch (Exception e) {
            e.printStackTrace();
        }

        BigInteger wei = ethGetBalance.getBalance();
        System.out.println("0xe3d59dc0327a346ecbcf45f3409f1040a192a4fd账户余额为："+wei);
    }


    @Test
    // 查询余额
    public void getBalance() throws CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        LOGGER.info("get the money of accounts...");
        // connect to node
        Web3j web3 = Web3j.build(new HttpService("http://172.23.17.134:8547"));
        // send asynchronous requests to get balance
        EthGetBalance ethGetBalance = null;
        try {
            ethGetBalance = web3
                    .ethGetBalance("0xe3d59dc0327a346ecbcf45f3409f1040a192a4fd", DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();

        }catch (Exception e) {
            e.printStackTrace();
        }

        BigInteger wei = ethGetBalance.getBalance();
        System.out.println("0xe3d59dc0327a346ecbcf45f3409f1040a192a4fd账户余额为："+wei);
    }
*/
}
