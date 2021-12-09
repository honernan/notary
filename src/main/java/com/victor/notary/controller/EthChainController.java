package com.victor.notary.controller;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-10-20  20：04
 * */

import com.victor.notary.model.EthUser1;
import com.victor.notary.model.EthUser2;
import com.victor.notary.service.Eth1Service;
import com.victor.notary.service.Eth2Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping(value = "/api/ethChain")
@Api(value = "以太坊链内交易",tags = "以太坊链内交易")
public class EthChainController {
    @Autowired
    private Eth1Service eth1Service;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private Eth2Service eth2Service;

    @ApiOperation(value = "更新eth1在redis中的用户信息")
    @PostMapping(value = "/updateEthuser1ToRedis")
    @Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public String updateEthuser1ToRedis() {
        List<EthUser1> list = eth1Service.getAllUser();
        //
        List<EthUser1> ethUser1 = (List<EthUser1>)redisTemplate.opsForList().range("eth1UserInfo",0, -1).get(0);
        // 把所有公证人信息存入redis
        if (ethUser1.size() == 0) {
            redisTemplate.opsForList().rightPush("eth1UserInfo", list);
        } else {
            redisTemplate.opsForList().rightPop("eth1UserInfo");
            redisTemplate.opsForList().rightPush("eth1UserInfo", list);
        }
        return "成功更新eth1在redis中的用户信息！！！";
    }

    @PostMapping("/sendMoneyInnerEths")
    @ApiOperation(value = "以太坊单链内转账1链，用于并发实验，交易金额随机生成")
    public void sendMoneyInnerEths1() {
        List<EthUser1> eth1UserInfo = (List<EthUser1>)redisTemplate.opsForList().range("eth1UserInfo",0, -1).get(0);
        int total = eth1UserInfo.size();
        EthUser1 from = null;
        EthUser1 to = null;
        int id = (int)redisTemplate.opsForValue().get("usernumber");
        System.out.println(id);
        if (id < total-1) {
            redisTemplate.opsForValue().set("usernumber",id+1);
            from = eth1UserInfo.get(id);
        } else if(id == total-1) {
            redisTemplate.opsForValue().set("usernumber",0);
            from = eth1UserInfo.get(id);
        } else  {
            redisTemplate.opsForValue().set("usernumber",1);
            from = eth1UserInfo.get(0);
        }
        //创建一个Random类对象实例
        Random rd = new Random();
        int num = rd.nextInt(total);
        to = eth1UserInfo.get(num);
        try {
            //获取5-10的随机数
            int money = (int)(5 + Math.random()*(10 -5+1));
            System.out.println(money);
            eth1Service.localSendEther(from.getUseraddress(), "http://172.23.17.135:8547", from.getUserkeyfilejson(), to.getUseraddress(),
                    BigInteger.valueOf(money));
        }catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }

    }

//==============================================================eth2===============================================
    @ApiOperation(value = "更新eth2在redis中的用户信息")
    @PostMapping(value = "/updateEthuser2ToRedis")
    @Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public String updateEthuser2ToRedis() {
        List<EthUser2> list = eth2Service.selectAll();
        //
        List<EthUser2> ethUser2 = (List<EthUser2>)redisTemplate.opsForList().range("eth2UserInfo",0, -1).get(0);
        // 把所有公证人信息存入redis
        if (ethUser2.size() == 0) {
            redisTemplate.opsForList().rightPush("eth2UserInfo", list);
        } else {
            redisTemplate.opsForList().rightPop("eth2UserInfo");
            redisTemplate.opsForList().rightPush("eth2UserInfo", list);
        }
        return "成功更新eth1在redis中的用户信息！！！";
    }

    @PostMapping("/sendMoneyInnerEths2")
    @ApiOperation(value = "以太坊单链内转账2链，用于并发实验，交易金额随机生成")
    public void sendMoneyInnerEths2() {
        List<EthUser2> eth2UserInfo = (List<EthUser2>)redisTemplate.opsForList().range("eth2UserInfo",0, -1).get(0);
        int total = eth2UserInfo.size();
        EthUser2 from = null;
        EthUser2 to = null;
        int id = (int)redisTemplate.opsForValue().get("usernumber");
        System.out.println(id);
        if (id < total-1) {
            redisTemplate.opsForValue().set("usernumber",id+1);
            from = eth2UserInfo.get(id);
        } else if(id == total-1) {
            redisTemplate.opsForValue().set("usernumber",0);
            from = eth2UserInfo.get(id);
        } else  {
            redisTemplate.opsForValue().set("usernumber",1);
            from = eth2UserInfo.get(0);
        }
        //创建一个Random类对象实例
        Random rd = new Random();
        int num = rd.nextInt(total);
        to = eth2UserInfo.get(num);
        try {
            //获取5-10的随机数
            int money = (int)(5 + Math.random()*(10 -5+1));
            System.out.println(money);
            eth2Service.localSendEther(from.getUseraddress(), "http://172.23.17.136:8549", from.getUserkeyfilejson(), to.getUseraddress(),
                    BigInteger.valueOf(money));
        }catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }

    }
}
