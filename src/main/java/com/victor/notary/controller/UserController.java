package com.victor.notary.controller;

import com.victor.notary.entity.Crosschain_trade;
import com.victor.notary.service.EthUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;

import javax.annotation.Resource;
import java.math.BigInteger;

@Api(value = "用户管理",tags = "用户管理")
@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    @Resource
    private EthUserService ethUserService;






    @ApiOperation(value = "用户退出公证人组验证")
    @RequestMapping(value = "/exit", method = RequestMethod.GET)
    public String exit(@RequestParam(value = "account1")String account1,
                       @RequestParam(value = "account2")String account2,
                       @RequestParam(value = "address1")String address1,
                       @RequestParam(value = "address2")String address2,
                       @RequestParam(value = "transaction1")String transaction1,
                       @RequestParam(value = "transaction2")String transaction2) throws Exception{
        //分别调用两条链查询id的接口
        boolean flag1 = ethUserService.isExistence(account1, address1);
        boolean flag2 = ethUserService.isExistence(account2, address2);

        if (flag1 == false) {
            throw  new Exception(address1+"这条链上的用户id有误，请检查后重新输入！！！");
        }
        if (flag2 == false) {
            throw  new Exception(address2+"这条链上的用户id有误，请检查后重新输入！！！");
        }

        //TODO
        // 在公证人组用户表加入两个用户的id
        boolean flag = false;
        try {
            flag = ethUserService.exitNatoryGroup(account1, account2);
        }catch (Exception e) {
            e.printStackTrace();
        }
        if (!flag)
            return "退出公证人组失败！！！";
        return "退出加入公证人组！！！";
    }



    @ApiOperation(value = "测试")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        Admin web3 = Admin.build(new HttpService("http://172.23.17.134:8547"));
        // 通过admin进行账户解锁
        PersonalUnlockAccount personalUnlockAccount = null;
        try {
            personalUnlockAccount = web3.personalUnlockAccount("0xe3d59dc0327a346ecbcf45f3409f1040a192a4fd","123456").sendAsync().get();
        }catch (Exception e) {
            e.getMessage();
        }
        if (personalUnlockAccount.accountUnlocked()) {
            // 实例化智能合约
            Credentials credentials = null;
            try {
                credentials = WalletUtils.loadCredentials("123456", "UTC--2019-08-13T13-44-59.713748167Z--e3d59dc0327a346ecbcf45f3409f1040a192a4fd.json");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Crosschain_trade contract = new Crosschain_trade("0xE37E6457b0D6b792e6AE54f28E41195664301a3a", web3, credentials, Contract.GAS_PRICE, Contract.GAS_LIMIT);

            BigInteger remoteCall = null;
            try {
                remoteCall = contract.GetBlocknumbers("0xE460e2F5107C63B217A75950D6AB1ac5ED488049").send();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //分别调用两条链查询id的接口

            //TODO
            //分别从两条链上验证缴纳的保证金
            //TODO
            // 在公证人组用户表加入两个用户的id

        }
        return "退出加入公证人组！！！";
    }

    @ApiOperation(value = "测试余额")
    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    public void test2(@RequestParam(value = "toHttp")String toHttp,
                      @RequestParam(value = "fromAddress")String fromAddress,
                      @RequestParam(value = "toAddress")String toAddress) {
        Admin web3 = Admin.build(new HttpService(toHttp));
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
    }
}
