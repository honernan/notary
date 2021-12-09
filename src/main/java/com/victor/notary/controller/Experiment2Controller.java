package com.victor.notary.controller;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-11-20  15：33
 * */

import com.victor.notary.mapper.DifferentPercentMaliciousNotaryMapper;
import com.victor.notary.model.DifferentPercentMaliciousNotary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Random;

@Controller
@RequestMapping(value = "/api/experiment2")
@Api(value = "实验二，不同比例恶意节点对跨链交易成功率的影响", tags = "实验二，不同比例恶意节点对跨链交易成功率的影响")
public class Experiment2Controller {
    @Autowired
    private DifferentPercentMaliciousNotaryMapper differentPercentMaliciousNotaryMapper;

    private int time = 0;


    @PostMapping(value = "/generateMalicious")
    @ApiOperation(value = "随机标记默认比例的恶意节点,参数只能输入5，10，30或40！！！")
    public void generateMaliciousNodeRandomly(@RequestParam(value = "percent") int percent) {
        DifferentPercentMaliciousNotary notary = new DifferentPercentMaliciousNotary();
        if (percent == 5) {
            Random random = new Random();
            int id = 0;
            for (int i = 0; i < 5; i++) {
                id = random.nextInt(100) + 1;
                notary.setId(id);
                notary.setFive(1);
                differentPercentMaliciousNotaryMapper.updateFiveByPrimaryKey(notary);
            }
            int num = differentPercentMaliciousNotaryMapper.countFiveNum();
            while (num < 5) {
                int temp = 5 - num;
                for (int i = 0; i < temp; i++) {
                    id = random.nextInt(100) + 1;
                    notary.setId(id);
                    notary.setThirty(1);
                    differentPercentMaliciousNotaryMapper.updateFiveByPrimaryKey(notary);
                }
                num = differentPercentMaliciousNotaryMapper.countFiveNum();
            }
        }

        if (percent == 10) {
            Random random = new Random();
            int id = 0;
            for (int i = 0; i < 10; i++) {
                id = random.nextInt(100) + 1;
                notary.setId(id);
                notary.setTen(1);
                differentPercentMaliciousNotaryMapper.updateTenByPrimaryKey(notary);
            }
            int num = differentPercentMaliciousNotaryMapper.countTenNum();
            while (num < 10) {
                int temp = 10 - num;
                for (int i = 0; i < temp; i++) {
                    id = random.nextInt(100) + 1;
                    notary.setId(id);
                    notary.setThirty(1);
                    differentPercentMaliciousNotaryMapper.updateTenByPrimaryKey(notary);
                }
                num = differentPercentMaliciousNotaryMapper.countTenNum();
            }
        }

        if (percent == 30) {
            Random random = new Random();
            int id = 0;
            for (int i = 0; i < 30; i++) {
                id = random.nextInt(100) + 1;
                notary.setId(id);
                notary.setThirty(1);
                differentPercentMaliciousNotaryMapper.updateThirtyByPrimaryKey(notary);
            }
            int num = differentPercentMaliciousNotaryMapper.countThirtyNum();
            while (num < 30) {
                int temp = 30 - num;
                for (int i = 0; i < temp; i++) {
                    id = random.nextInt(100) + 1;
                    notary.setId(id);
                    notary.setThirty(1);
                    differentPercentMaliciousNotaryMapper.updateThirtyByPrimaryKey(notary);
                }
                num = differentPercentMaliciousNotaryMapper.countThirtyNum();
            }
        }

        if (percent == 40) {
            Random random = new Random();
            int id = 0;
            for (int i = 0; i < 40; i++) {
                id = random.nextInt(100) + 1;
                notary.setId(id);
                notary.setFourty(1);
                differentPercentMaliciousNotaryMapper.updateFourtyByPrimaryKey(notary);
            }
            int num = differentPercentMaliciousNotaryMapper.countFourtyNum();
            while (num < 40) {
                int temp = 40 - num;
                for (int i = 0; i < temp; i++) {
                    id = random.nextInt(100) + 1;
                    notary.setId(id);
                    notary.setThirty(1);
                    differentPercentMaliciousNotaryMapper.updateFourtyByPrimaryKey(notary);
                }
                num = differentPercentMaliciousNotaryMapper.countFourtyNum();
            }
        }
    }

    @PostMapping(value = "/transactionSuccessRate")
    @ApiOperation(value = "测试不同比例恶意节点的交易成功率,参数只能输入5，10，30或40！！！")
    public void transactionSuccessRate(@RequestParam(value = "percent") int percent) {
        DifferentPercentMaliciousNotary notary = new DifferentPercentMaliciousNotary();
        Random random = new Random();

        /* 2、竞选公证人
         * 2.1  算信誉平均值
         * 2.2  highList---大于平均值的公证人集合
         * 2.3  lowList---小于平均值的公证人集合
         * 2.4  从highList选取公证人的概率为（highList的个数）/（highList公证人个数+lowList公证人个数）
         * 2.5  从lowList选取公证人的概率为（lowList的个数）/（highList公证人个数+lowList公证人个数）
         * */
        // 获取highList和lowList
        List<DifferentPercentMaliciousNotary> highList = differentPercentMaliciousNotaryMapper.selectHighNotary();
        List<DifferentPercentMaliciousNotary> lowList = differentPercentMaliciousNotaryMapper.selectLowNotary();
        // 计算lowList和highList的总个数
        int totalNotary = highList.size() + lowList.size();
        // 根据2.4，2.5的概率确定公证人从哪一组选取,并从对应组中随机选取公证人
        int id = random.nextInt(totalNotary) + 1;
        if (id <= highList.size()) {
            System.out.println("从highList里面选取公证人！！！");
            notary = highList.get(random.nextInt(highList.size()));
            System.out.println("======================id=:" + notary.getId());
        } else {
            System.out.println("从lowList里面选取公证人！！！");
            notary = lowList.get(random.nextInt(lowList.size()));
            System.out.println("======================id=:" + notary.getId());
        }

        //isBad==1则说明该节点为恶意节点
        int isBad = 0;
        if (percent == 5) {
            isBad = notary.getFive();
        }
        if (percent == 10) {
            isBad = notary.getTen();
        }
        if (percent == 30) {
            isBad = notary.getThirty();
        }
        if (percent == 40) {
            isBad = notary.getFourty();
        }
        // 更新信誉值
        if (isBad == 1) {
            // 获取第一公证人
            DifferentPercentMaliciousNotary firstNotary = differentPercentMaliciousNotaryMapper.selectFirstNotary().get(0);

            /* 2）由于真实公证人伪造虚假交易导致交易未成功，真实公证人的信誉值置为0
             * 2.1) 如果flag2==1，则认为第一公证人将虚假交易验证通过，被其他公证人查出，这是真实公证人信誉值==0，第一公证人信誉值/2， 验出虚假交易的公证人信誉值增加1-1.5
             * */
            int flag2 = random.nextInt(2);

            // 恶意节点信誉值置为0
            differentPercentMaliciousNotaryMapper.updateCreditById(notary.getId(), 0);
            // 如果第一公证人将虚假交易验证通过，那么信誉值减半，验出虚假交易的公证人信誉值增加1-1.5
            if (flag2 == 1) {
                // 恶意公证人的节点信誉值置为0
                differentPercentMaliciousNotaryMapper.updateCreditById(firstNotary.getId(),
                        firstNotary.getCredit() / 2);
                System.out.println("第一公证人将虚假交易验证通过，被其他公证人查出，第一公证人信誉值减半！！！");
                // 随机选取发现恶意交易的公证人
                int finderId = random.nextInt(100) + 1;
                while (finderId == notary.getId()) {
                    finderId = random.nextInt(100) + 1;
                }
                // 发现恶意交易的公证人信誉值加1~1.5
                float floatNum = random.nextFloat();
                if (floatNum > 0.5) {
                    floatNum += 0.5f;
                } else {
                    floatNum += 1;
                }
                differentPercentMaliciousNotaryMapper.updateCreditById(finderId, floatNum);
                System.out.println("发现恶意交易的公证人信誉值加-----" + String.valueOf(floatNum).substring(0, 5));
            }
        } else {
            System.out.println("跨链交易成功，公证人节点信誉值加1！！！");
            //获取原来的信誉值
            float credit = notary.getCredit();
            differentPercentMaliciousNotaryMapper.updateCreditById(notary.getId(), credit + 1);
            ++time;
            System.out.println("==============================================" + time + "=================================================");
        }
    }
}
