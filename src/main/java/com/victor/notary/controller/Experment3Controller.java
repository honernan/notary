package com.victor.notary.controller;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-11-05  16：43
 * */

import com.victor.notary.mapper.DifferentPercentMaliciousNotaryMapper;
import com.victor.notary.model.DifferentPercentMaliciousNotary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Random;

@Controller
@RequestMapping(value = "/api/experment3")
@Api(value = "实验三，不同比例恶意节点交易时间对比",tags = "实验三，不同比例恶意节点交易时间对比！")
public class Experment3Controller {
    @Autowired
    private DifferentPercentMaliciousNotaryMapper differentPercentMaliciousNotaryMapper;
    private int total = 0;
    private int time = 0;

    @PostMapping(value = "/initTable")
    @ApiOperation(value = "向diff_percent_malicious_notary表中插入初始数据")
    public void initTable() {
        DifferentPercentMaliciousNotary notary = new DifferentPercentMaliciousNotary();
        notary.setFive(0);
        notary.setTen(0);
        notary.setThirty(0);
        notary.setFourty(0);

        for (int i = 3; i <=100; i++) {
            notary.setId(i);
            differentPercentMaliciousNotaryMapper.insert(notary);
        }
    }

    @PostMapping(value = "/initMaliciousNotary")
    @ApiOperation(value = "初始化diff_percent_malicious_notary表的恶意节点")
    public void initMaliciousNotary() {
        DifferentPercentMaliciousNotary notary = new DifferentPercentMaliciousNotary();

        for (int i = 1; i <=100; i++) {
            // 标记5%的恶意公证人
            if (i % 20 == 0) {
                notary.setId(i);
                notary.setFive(1);
                notary.setTen(0);
                notary.setThirty(0);
                notary.setFourty(0);
                differentPercentMaliciousNotaryMapper.updateFiveByPrimaryKey(notary);
            }
            // 标记10%的恶意公证人
            if (i % 10 == 6) {
                notary.setId(i);
                notary.setFive(0);
                notary.setTen(1);
                notary.setThirty(0);
                notary.setFourty(0);
                differentPercentMaliciousNotaryMapper.updateTenByPrimaryKey(notary);
            }
            // 标记30%的恶意公证人
            if (i % 10 == 6 || i%10 == 3 || i%10 == 9) {
                notary.setId(i);
                notary.setFive(0);
                notary.setTen(0);
                notary.setThirty(1);
                notary.setFourty(0);
                differentPercentMaliciousNotaryMapper.updateThirtyByPrimaryKey(notary);
            }
            // 标记40%的恶意公证人
            if (i % 10 == 2 || i%10 == 4 || i%10 == 6 || i%10 == 8) {
                notary.setId(i);
                notary.setFive(0);
                notary.setTen(0);
                notary.setThirty(0);
                notary.setFourty(1);
                differentPercentMaliciousNotaryMapper.updateFourtyByPrimaryKey(notary);
            }
        }
    }

    @PostMapping(value = "/fivePercentMaliciousNotary")
    @ApiOperation(value = "测试5%恶意节点的情况下，交易数和时间关系")
    public void fivePercentMaliciousNotary() {

        Random random = new Random();
        DifferentPercentMaliciousNotary notary = new DifferentPercentMaliciousNotary();
        /* 1、竞选公证人
         * 1.1  算信誉平均值
         * 1.2  highList---大于平均值的公证人集合
         * 1.3  lowList---小于平均值的公证人集合
         * 1.4  从highList选取公证人的概率为（highList的个数）/（highList公证人个数+lowList公证人个数）
         * 1.5  从lowList选取公证人的概率为（lowList的个数）/（highList公证人个数+lowList公证人个数）
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

        //2、如果选出的公证人id为1，则为恶意公证人（没参加过跨链交易）
        // 更新信誉值
        if (notary.getFive() == 1) {
            // 获取第一公证人
            DifferentPercentMaliciousNotary firstNotary = differentPercentMaliciousNotaryMapper.selectFirstNotary().get(0);
            /*
             * 1) 如果flag==1 且，flag2==1，则认为第一公证人将虚假交易验证通过，被其他公证人查出，这是真实公证人信誉值-1，第一公证人信誉值/2， 验出虚假交易的公证人信誉值增加1-1.5
             * 2) 如果flag==2 且，flag2==1，则认为第一公证人将虚假交易验证通过，被其他公证人查出，这是真实公证人信誉值==0，第一公证人信誉值/2， 验出虚假交易的公证人信誉值增加1-1.5
             * */
            int flag2 = random.nextInt(2);
            // 恶意公证人的节点信誉值置为0
            differentPercentMaliciousNotaryMapper.updateCreditById(notary.getId(), 0);
            System.out.println("恶意节点的信誉值值为0！！！");
            //第一公证人将虚假交易验证通过，被其他公证人查出，第一公证人信誉值/2
            if (flag2 == 1) {
                differentPercentMaliciousNotaryMapper.updateCreditById(firstNotary.getId(), firstNotary.getCredit()/2);
            }
            // 随机选取发现恶意交易的公证人
            int finderId =  random.nextInt(100)+1;
            while (finderId == notary.getId()) {
                finderId = random.nextInt(100)+1;
            }
            // 发现恶意交易的公证人信誉值加1~1.5
            float floatNum = random.nextFloat();
            if (floatNum > 0.5) {
                floatNum += 0.5f;
            } else {
                floatNum += 1;
            }
            differentPercentMaliciousNotaryMapper.updateCreditById(finderId, floatNum);
            // 将恶意公证人的标识置为2
            notary.setFive(2);
            differentPercentMaliciousNotaryMapper.updateFiveByPrimaryKey(notary);
            ++total;
            System.out.println("===============一共发生了：" +total+"笔的恶意交易啊 ！！！============================================");
        } else {
            System.out.println("跨链交易成功，公证人节点信誉值加1！！！");
            //获取原来的信誉值
            float credit =  differentPercentMaliciousNotaryMapper.selectByPrimaryKey(notary.getId()).getCredit();
            differentPercentMaliciousNotaryMapper.updateCreditById(notary.getId(), credit + 1);
        }
        ++time;
        System.out.println("===============一共发生了："+time+"笔的交易,其中包含" +total+"笔的恶意交易啊 ！！！============================================");

    }


    @PostMapping(value = "/tenPercentMaliciousNotary")
    @ApiOperation(value = "测试10%恶意节点的情况下，交易数和时间关系")
    public void tenPercentMaliciousNotary() {

        Random random = new Random();
        DifferentPercentMaliciousNotary notary = new DifferentPercentMaliciousNotary();
        /* 1、竞选公证人
         * 1.1  算信誉平均值
         * 1.2  highList---大于平均值的公证人集合
         * 1.3  lowList---小于平均值的公证人集合
         * 1.4  从highList选取公证人的概率为（highList的个数）/（highList公证人个数+lowList公证人个数）
         * 1.5  从lowList选取公证人的概率为（lowList的个数）/（highList公证人个数+lowList公证人个数）
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
        //2、如果选出的公证人id为1，则为恶意公证人（没参加过跨链交易）
        // 更新信誉值
        if (notary.getTen() == 1) {
            // 获取第一公证人
            DifferentPercentMaliciousNotary firstNotary = differentPercentMaliciousNotaryMapper.selectFirstNotary().get(0);
            /*
             * 1) 如果flag==1 且，flag2==1，则认为第一公证人将虚假交易验证通过，被其他公证人查出，这是真实公证人信誉值-1，第一公证人信誉值/2， 验出虚假交易的公证人信誉值增加1-1.5
             * 2) 如果flag==2 且，flag2==1，则认为第一公证人将虚假交易验证通过，被其他公证人查出，这是真实公证人信誉值==0，第一公证人信誉值/2， 验出虚假交易的公证人信誉值增加1-1.5
             * */
            int flag2 = random.nextInt(2);
            // 恶意公证人的节点信誉值置为0
            differentPercentMaliciousNotaryMapper.updateCreditById(notary.getId(), 0);
            System.out.println("恶意节点的信誉值值为0！！！");
            //第一公证人将虚假交易验证通过，被其他公证人查出，第一公证人信誉值/2
            if (flag2 == 1) {
                differentPercentMaliciousNotaryMapper.updateCreditById(firstNotary.getId(), firstNotary.getCredit()/2);
            }
            // 随机选取发现恶意交易的公证人
            int finderId =  random.nextInt(100)+1;
            while (finderId == notary.getId()) {
                finderId = random.nextInt(100)+1;
            }
            // 发现恶意交易的公证人信誉值加1~1.5
            float floatNum = random.nextFloat();
            if (floatNum > 0.5) {
                floatNum += 0.5f;
            } else {
                floatNum += 1;
            }
            differentPercentMaliciousNotaryMapper.updateCreditById(finderId, floatNum);
            // 将恶意公证人的标识置为2
            notary.setTen(2);
            differentPercentMaliciousNotaryMapper.updateTenByPrimaryKey(notary);
            ++total;
            String temp = String.valueOf(notary.getId());
            System.out.println("===============一共发生了：" +total+"笔的恶意交易啊 ！！！============================================"+temp);
        } else {
            System.out.println("跨链交易成功，公证人节点信誉值加1！！！");
            //获取原来的信誉值
            float credit =  differentPercentMaliciousNotaryMapper.selectByPrimaryKey(notary.getId()).getCredit();
            differentPercentMaliciousNotaryMapper.updateCreditById(notary.getId(), credit + 1);
        }
        ++time;
        String temp = String.valueOf(notary.getId());
        System.out.println("===============一共发生了："+time+"笔的交易,其中包含" +total+"笔的恶意交易啊 ！！！============================================"+temp);

    }

    @PostMapping(value = "/thirtyPercentMaliciousNotary")
    @ApiOperation(value = "测试30%恶意节点的情况下，交易数和时间关系")
    public void thirtyPercentMaliciousNotary() {

        Random random = new Random();
        DifferentPercentMaliciousNotary notary = null;
        /* 1、竞选公证人
         * 1.1  算信誉平均值
         * 1.2  highList---大于平均值的公证人集合
         * 1.3  lowList---小于平均值的公证人集合
         * 1.4  从highList选取公证人的概率为（highList的个数）/（highList公证人个数+lowList公证人个数）
         * 1.5  从lowList选取公证人的概率为（lowList的个数）/（highList公证人个数+lowList公证人个数）
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

        //2、如果选出的公证人id为1，则为恶意公证人（没参加过跨链交易）
        // 更新信誉值
        if (notary.getThirty() == 1) {
            // 获取第一公证人
            DifferentPercentMaliciousNotary firstNotary = differentPercentMaliciousNotaryMapper.selectFirstNotary().get(0);
            /*
             * 1) 如果flag==1 且，flag2==1，则认为第一公证人将虚假交易验证通过，被其他公证人查出，这是真实公证人信誉值-1，第一公证人信誉值/2， 验出虚假交易的公证人信誉值增加1-1.5
             * 2) 如果flag==2 且，flag2==1，则认为第一公证人将虚假交易验证通过，被其他公证人查出，这是真实公证人信誉值==0，第一公证人信誉值/2， 验出虚假交易的公证人信誉值增加1-1.5
             * */
            int flag2 = random.nextInt(2);
            // 恶意公证人的节点信誉值置为0
            differentPercentMaliciousNotaryMapper.updateCreditById(notary.getId(), 0);
            System.out.println("恶意节点的信誉值值为0！！！");
            //第一公证人将虚假交易验证通过，被其他公证人查出，第一公证人信誉值/2
            if (flag2 == 1) {
                differentPercentMaliciousNotaryMapper.updateCreditById(firstNotary.getId(), firstNotary.getCredit()/2);
            }
            // 随机选取发现恶意交易的公证人
            int finderId =  random.nextInt(100)+1;
            while (finderId == notary.getId()) {
                finderId = random.nextInt(100)+1;
            }
            // 发现恶意交易的公证人信誉值加1~1.5
            float floatNum = random.nextFloat();
            if (floatNum > 0.5) {
                floatNum += 0.5f;
            } else {
                floatNum += 1;
            }
            differentPercentMaliciousNotaryMapper.updateCreditById(finderId, floatNum);
            // 将恶意公证人的标识置为2
            notary.setThirty(2);
            differentPercentMaliciousNotaryMapper.updateThirtyByPrimaryKey(notary);
            ++total;
            System.out.println("===============一共发生了：" +total+"笔的恶意交易啊 ！！！============================================"+notary.getId());
        } else {
            System.out.println("跨链交易成功，公证人节点信誉值加1！！！");
            //获取原来的信誉值
            float credit =  differentPercentMaliciousNotaryMapper.selectByPrimaryKey(notary.getId()).getCredit();
            differentPercentMaliciousNotaryMapper.updateCreditById(notary.getId(), credit + 1);
        }
        ++time;
        String temp = String.valueOf(notary.getId());
        System.out.println("===============一共发生了："+time+"笔的交易,其中包含" +total+"笔的恶意交易啊 ！！！============================================"+temp);

    }

    @PostMapping(value = "/fourtyPercentMaliciousNotary")
    @ApiOperation(value = "测试40%恶意节点的情况下，交易数和时间关系")
    public void fourtyPercentMaliciousNotary() {

        Random random = new Random();
        DifferentPercentMaliciousNotary notary = null;
        /* 1、竞选公证人
         * 1.1  算信誉平均值
         * 1.2  highList---大于平均值的公证人集合
         * 1.3  lowList---小于平均值的公证人集合
         * 1.4  从highList选取公证人的概率为（highList的个数）/（highList公证人个数+lowList公证人个数）
         * 1.5  从lowList选取公证人的概率为（lowList的个数）/（highList公证人个数+lowList公证人个数）
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

        //2、如果选出的公证人id为1，则为恶意公证人（没参加过跨链交易）
        // 更新信誉值
        if (notary.getFourty() == 1) {
            // 获取第一公证人
            DifferentPercentMaliciousNotary firstNotary = differentPercentMaliciousNotaryMapper.selectFirstNotary().get(0);
            /*
             * 1) 如果flag==1 且，flag2==1，则认为第一公证人将虚假交易验证通过，被其他公证人查出，这是真实公证人信誉值-1，第一公证人信誉值/2， 验出虚假交易的公证人信誉值增加1-1.5
             * 2) 如果flag==2 且，flag2==1，则认为第一公证人将虚假交易验证通过，被其他公证人查出，这是真实公证人信誉值==0，第一公证人信誉值/2， 验出虚假交易的公证人信誉值增加1-1.5
             * */
            int flag2 = random.nextInt(2);
            // 恶意公证人的节点信誉值置为0
            differentPercentMaliciousNotaryMapper.updateCreditById(notary.getId(), 0);
            System.out.println("恶意节点的信誉值值为0！！！");
            //第一公证人将虚假交易验证通过，被其他公证人查出，第一公证人信誉值/2
            if (flag2 == 1) {
                differentPercentMaliciousNotaryMapper.updateCreditById(firstNotary.getId(), firstNotary.getCredit()/2);
            }
            // 随机选取发现恶意交易的公证人
            int finderId =  random.nextInt(100)+1;
            while (finderId == notary.getId()) {
                finderId = random.nextInt(100)+1;
            }
            // 发现恶意交易的公证人信誉值加1~1.5
            float floatNum = random.nextFloat();
            if (floatNum > 0.5) {
                floatNum += 0.5f;
            } else {
                floatNum += 1;
            }
            differentPercentMaliciousNotaryMapper.updateCreditById(finderId, floatNum);
            // 将恶意公证人的标识置为2
            notary.setFourty(2);
            differentPercentMaliciousNotaryMapper.updateFourtyByPrimaryKey(notary);
            ++total;
            System.out.println("===============一共发生了：" +total+"笔的恶意交易啊 ！！！============================================"+notary.getId());
        } else {
            System.out.println("跨链交易成功，公证人节点信誉值加1！！！");
            //获取原来的信誉值
            float credit =  differentPercentMaliciousNotaryMapper.selectByPrimaryKey(notary.getId()).getCredit();
            differentPercentMaliciousNotaryMapper.updateCreditById(notary.getId(), credit + 1);
        }
        ++time;
        String temp = String.valueOf(notary.getId());
        System.out.println("===============一共发生了："+time+"笔的交易,其中包含" +total+"笔的恶意交易啊 ！！！============================================"+temp);

    }

    @PostMapping(value = "/fourtyPercentMaliciousNotaryWithoutCredit")
    @ApiOperation(value = "测试40%恶意节点的情况下，交易数和时间关系")
    public void fourtyPercentMaliciousNotaryWithoutCredit() {

        Random random = new Random();
        int id = random.nextInt(100)+1;
        // 1、随机选取公证人
        DifferentPercentMaliciousNotary notary = differentPercentMaliciousNotaryMapper.selectByPrimaryKey(id);


        //2、如果选出的公证人标志为1，则为恶意公证人（没参加过跨链交易）
        // 更新信誉值
        if (notary.getTen() == 1) {
            ++total;
        }
        ++time;
        String temp = String.valueOf(notary.getId());
        System.out.println("===============一共发生了："+time+"笔的交易,其中包含" +total+"笔的恶意交易啊 ！！！============================================"+temp);

    }
}
