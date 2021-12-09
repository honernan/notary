package com.victor.notary.controller;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-10-19  20：55
 * */

import com.victor.notary.model.NotaryBetweenEth;
import com.victor.notary.service.NotaryBetweenEthService;
import com.victor.notary.service.EthUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Controller
@RequestMapping(value = "/api/notaryBetweenEth")
@Api(value = "公证人组管理",tags = "公证人组管理")
public class NotaryBetweenEthController {
    @Resource
    private EthUserService ethUserService;
    @Resource
    private NotaryBetweenEthService notaryBetweenEthService;
    @Resource
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "更新公证人组在redis中的用户信息")
    @PostMapping(value = "/updateNotaryBetweenEthToRedis")
    @Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public String updateNotaryBetweenEthToRedis() {
        List<NotaryBetweenEth> list = notaryBetweenEthService.getAll();
        //
        List<NotaryBetweenEth> ethUser1 = (List<NotaryBetweenEth>)redisTemplate.opsForList().range("notaryInfo",0, -1).get(0);
        // 把所有公证人信息存入redis
        if (ethUser1.size() == 0) {
            redisTemplate.opsForList().rightPush("notaryInfo", list);
        } else {
            redisTemplate.opsForList().rightPop("notaryInfo");
            redisTemplate.opsForList().rightPush("notaryInfo", list);
        }
        return "成功更新公证人组在redis中的用户信息！！！";
    }

    @ApiOperation(value = "用户加入公证人组验证")
    @RequestMapping(value = "/join", method = RequestMethod.GET)
    @Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public String join(@RequestParam(value = "account1")String account1,
                       @RequestParam(value = "account2")String account2,
                       @RequestParam(value = "chainAddress1")String address1,
                       @RequestParam(value = "chainAddress2")String address2,
                       @RequestParam(value = "blockNum1")long blockNum1,
                       @RequestParam(value = "blockNum2")long blockNum2,
                       @RequestParam(value = "transactionHash1")String transactionHash1,
                       @RequestParam(value = "transactionHash2")String transactionHash2,
                       @RequestParam(value = "jsonfile1")String jsonfile1,
                       @RequestParam(value = "jsonfile2")String jsonfile2
                       ) throws Exception{
        //分别调用两条链查询id的接口
        boolean flag1 = ethUserService.isExistence(account1, address1);
        boolean flag2 = ethUserService.isExistence(account2, address2);
        if (flag2 == false) {
            throw new Exception(address2+"这条链上的用户id有误，请检查后重新输入！！！");
        }
        if (flag1 == false) {
            throw new Exception(address1+"这条链上的用户id有误，请检查后重新输入！！！");
        }

        //分别从两条链上验证缴纳的保证金
        Web3j web3 = Web3j.build(new HttpService(address1));

        // 保证金
        BigInteger totalBone = BigInteger.valueOf(0);
        // 源链和目标链的账户id
        String from = "";
        String to = "";
        EthBlock.Block blockInfo1 = web3
                .ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNum1)),true)
                .send().getBlock();
        List<EthBlock.TransactionResult> transactionResults1 = blockInfo1.getTransactions();
        for (EthBlock.TransactionResult t:transactionResults1
             ) {
            EthBlock.TransactionObject object = (EthBlock.TransactionObject)t.get();
            if (transactionHash1.equals(object.getHash())) {
                totalBone = object.getValue();
                from = object.getFrom();
                break;
            }
        }
        EthBlock.Block blockInfo2 = web3
                .ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNum2)),true)
                .send().getBlock();
        List<EthBlock.TransactionResult> transactionResults2 = blockInfo2.getTransactions();
        for (EthBlock.TransactionResult transactionResult:transactionResults2
        ) {
            EthBlock.TransactionObject object2 = (EthBlock.TransactionObject)transactionResult.get();
            if (transactionHash2.equals(object2.getHash())) {
                totalBone = totalBone.add(object2.getValue());
                to = object2.getFrom();
                break;
            }
        }

        // 假定加入的总的保证金为100
        if (totalBone.compareTo(BigInteger.valueOf(100)) < 0)
            return "保证金不足，无法加入公证人组！！！";


        // 获取两个账户的余额
        EthGetBalance ethGetBalanceFrom = null;
        EthGetBalance ethGetBalance1To = null;
        try {
            ethGetBalanceFrom = web3
                    .ethGetBalance(from, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            ethGetBalance1To = web3
                    .ethGetBalance(to, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
        }catch (Exception e) {
            e.printStackTrace();
        }
        // 在公证人组用户表加入两个用户的id（将两个账户关联）
        NotaryBetweenEth notaryBetweenEth = new NotaryBetweenEth();
        notaryBetweenEth.setUserkeyfilejson1(jsonfile1);
        notaryBetweenEth.setUserkeyfilejson1(jsonfile2);
        notaryBetweenEth.setFlag1(1);
        notaryBetweenEth.setFlag2(1);
        notaryBetweenEth.setUseraddress1(from);
        notaryBetweenEth.setUseraddress2(to);
        notaryBetweenEth.setCredit1(1);
        notaryBetweenEth.setCredit2(1);
        notaryBetweenEth.setEthchain1(address1);
        notaryBetweenEth.setEthchain2(address2);
        notaryBetweenEth.setMoney1(String.valueOf(ethGetBalanceFrom.getBalance()));
        notaryBetweenEth.setMoney2(String.valueOf(ethGetBalance1To.getBalance()));

        boolean flag = false;
        try {
            flag = notaryBetweenEthService.addNotary(notaryBetweenEth);
        }catch (Exception e) {
            e.printStackTrace();
        }

        // 更新redis中的公证人组对象
        List<NotaryBetweenEth> notaryBetweenEths = notaryBetweenEthService.getAll();
        redisTemplate.opsForList().rightPop("notaryInfo");
        redisTemplate.opsForList().rightPush("notaryInfo", notaryBetweenEths);

       if (!flag)
            return "加入公证人组失败！！！";
        return "成功加入公证人组！！！";
    }
}
