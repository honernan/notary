package com.victor.notary.util;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-11-26  10：55
 * */

import com.victor.notary.model.EthUser1;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProofOfStack {
    @Resource
    private RedisTemplate redisTemplate;

    public void posConsensus() {
        Map<Integer, String> stackMap = new HashMap();
        List<EthUser1> ethUser1 = (List<EthUser1>)redisTemplate.opsForList().range("eth1UserInfo",0, -1).get(0);

        for (EthUser1 eth1:
             ethUser1) {
            int value = Integer.valueOf(eth1.getMoney());


            stackMap.put(value, eth1.getUseraddress());

        }


    }

}
