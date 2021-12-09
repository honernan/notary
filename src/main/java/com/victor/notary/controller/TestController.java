package com.victor.notary.controller;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-10-16  17：02
 * */

import com.victor.notary.config.ThreadPoolConfig;
import com.victor.notary.model.NotaryBetweenEth;
import com.victor.notary.service.CrossChainService;
import com.victor.notary.service.Eth1Service;
import com.victor.notary.service.NotaryBetweenEthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;

@RestController
public class TestController {
    @Autowired
    ThreadPoolConfig threadPoolConfig;
    @Autowired
    private CrossChainService crossChainService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private NotaryBetweenEthService notaryBetweenEthService;
    @Autowired
    private Eth1Service eth1Service;

    /**
     * 测试模拟下单请求 入口
     * @param id
     * @return
     */
    @GetMapping("/start/{id}")
    public String start(@PathVariable Long id) {
        //模拟的随机数
        String orderNo = System.currentTimeMillis() + UUID.randomUUID().toString();

        threadPoolConfig.addOrders(orderNo);

        return "Test ThreadPoolExecutor start";
    }

    @GetMapping("/test")
    public void test1() {
        List<NotaryBetweenEth> notaryBetweenEthList = notaryBetweenEthService.getNotaryByCredit(1);
        List<NotaryBetweenEth> notaryBetweenEths = notaryBetweenEthService.getAll();
        // 把所有公证人信息存入redis
        redisTemplate.opsForList().rightPush("notaryInfo", notaryBetweenEths);
        Object notaryBetweenEths1 = (redisTemplate.opsForList().range("notaryInfo",0, -1).get(0));

        List<NotaryBetweenEth> notaryBetweenEths2 = (List<NotaryBetweenEth>)redisTemplate.opsForList().range("notaryInfo",0, -1).get(0);
        NotaryBetweenEth notar = notaryBetweenEths2.get(2);
        String fromAddress = "";
        // 获取1-2两个随机数
        Random rd = new Random(); //创建一个Random类对象实例
        int num = rd.nextInt(2)+1;
        System.out.println("====" + num);



    }

    @GetMapping("/test2")
    public void test2() {
        for (int i = 0; i < 5; i++) {
            redisTemplate.opsForValue().set("usernumber",i);
            System.out.println(redisTemplate.opsForValue().get("usernumber"));
        }
    }

    @GetMapping("/test3")
    public void test3() {
        //List<NotaryBetweenEth> notaryBetweenEths = notaryBetweenEthService.getAll();
        List<NotaryBetweenEth> notaryBetweenEths = (List<NotaryBetweenEth>)redisTemplate.opsForList().range("notaryInfo",0, -1).get(0);
        int total = notaryBetweenEthService.getNotarysNum();
        NotaryBetweenEth fromNotary = null;
        NotaryBetweenEth toNotary = null;
        int id = (int)redisTemplate.opsForValue().get("usernumber");
        System.out.println(id);
        if (id < total-1) {
            redisTemplate.opsForValue().set("usernumber",id+1);
            fromNotary = notaryBetweenEths.get(id);
        } else if(id == total-1) {
            redisTemplate.opsForValue().set("usernumber",0);
            fromNotary = notaryBetweenEths.get(id);
        } else  {
            redisTemplate.opsForValue().set("usernumber",1);
            fromNotary = notaryBetweenEths.get(0);
        }
        Random rd = new Random(); //创建一个Random类对象实例
        int num = rd.nextInt(total);
        toNotary = notaryBetweenEths.get(num);
        //EthBlock.TransactionObject txInfo = null;
        try {
            //获取5-10的随机数
            int money = (int)(5 + Math.random()*(10 -5+1));
            System.out.println(money);
            //txInfo = crossChainService.sendMoney(fromNotary.getUseraddress1(), toNotary.getUseraddress1(), "http://172.23.17.135:8547", fromNotary.getUserkeyfilejson1(), money, 0);
            crossChainService.localSendEther(fromNotary.getUseraddress1(), "http://172.23.17.135:8547", fromNotary.getUserkeyfilejson1(), toNotary.getUseraddress1(),
                    BigInteger.valueOf(money), 0, toNotary, 0);
        }catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }

    }
    /**
     * 停止服务
     * @param id
     * @return
     */
    @GetMapping("/end/{id}")
    public String end(@PathVariable Long id) {

        threadPoolConfig.shutdown();

        Queue q = threadPoolConfig.getMsgQueue();
        System.out.println("关闭了线程服务，还有未处理的信息条数：" + q.size());
        return "Test ThreadPoolExecutor start";
    }


}
