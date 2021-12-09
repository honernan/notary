package com.victor.notary.controller;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-11-04  16：33
 * */

import com.victor.notary.mapper.NormalDistributionCreditMapper;
import com.victor.notary.mapper.SamebeginOfCreditMapper;
import com.victor.notary.model.NormalDistributionCredit;
import com.victor.notary.model.SamebeginOfCredit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Random;

@Controller
@RequestMapping(value = "/api/experimentIV")
@Api(value = "实验四，不同信誉初始值实验",tags = "实验四，不同信誉初始值实验")
public class Experiment4Controller {
    @Autowired
    private SamebeginOfCreditMapper samebeginOfCreditMapper;
    @Autowired
    private NormalDistributionCreditMapper normalDistributionCreditMapper;

    // 计算发起请求次数
    private int time = 0;


    @PostMapping(value = "/sameCredit")
    @ApiOperation(value = "测试相同初始信誉值，恶意交易比例为10%的情况下，在公证人信誉机制下的信誉值变化")
    public void sameCredit() {
        SamebeginOfCredit notary = new SamebeginOfCredit();
        // 1、选择1-10随机数，为4则为恶意交易
        Random random = new Random();
        int badFlag = random.nextInt(20);

        // 2、竞选公证人
        // 2.1获取1链信誉值大于1链信誉平均值的几个账户
        List<SamebeginOfCredit> notaryList = samebeginOfCreditMapper.selectNotary();
        // 2.2从notaryBetweenEthList中随机选取一个作为公证人
        int totalCandite = notaryList.size();
        System.out.println("totalCandite========" + totalCandite);
        if (totalCandite == 1) {
            notary = notaryList.get(0);
        } else  {
            //2.2.1创建一个Random类对象实例
            Random rd = new Random();
            int num = rd.nextInt(totalCandite);
            notary = notaryList.get(num);
        }

        // 更新信誉值
        /*if (badFlag == 4) {
            // 获取第一公证人
            SamebeginOfCredit firstNotary = samebeginOfCreditMapper.selectFirstNotary().get(0);
            *//*
            * 1）如果flag为0，1则为由于网络原因造成交易未成功，真实公证人的信誉值减1
            * 1.1) 如果flag==1 且，flag2==1，则认为第一公证人将虚假交易验证通过，被其他公证人查出，这是真实公证人信誉值-1，第一公证人信誉值/2， 验出虚假交易的公证人信誉值增加1-1.5
            * 2）如果flag为2则为由于真实公证人伪造虚假交易导致交易未成功，真实公证人的信誉值置为0
            * 2.1) 如果flag==2 且，flag2==1，则认为第一公证人将虚假交易验证通过，被其他公证人查出，这是真实公证人信誉值==0，第一公证人信誉值/2， 验出虚假交易的公证人信誉值增加1-1.5
            * *//*
            int flag = random.nextInt(5);
            int flag2 = random.nextInt(2);
            if (flag == 2) {
                // 恶意公证人的节点信誉值置为0
                samebeginOfCreditMapper.updateEndCreditById(notary.getId(), 0);
                System.out.println("恶意节点的信誉值值为0！！！");

            } else {
                // 由于非人为元素，导致交易失败，恶意公证人的节点信誉值-1
                samebeginOfCreditMapper.updateEndCreditById(notary.getId(), notary.getEndcredit()-1);

            }
            //第一公证人将虚假交易验证通过，被其他公证人查出，第一公证人信誉值/2
            if (flag2 == 1) {
                samebeginOfCreditMapper.updateEndCreditById(firstNotary.getId(), firstNotary.getEndcredit()/2);
            }
            // 随机选取发现恶意交易的公证人
            int finderId =  random.nextInt(10);
            while (finderId == notary.getId()) {
                finderId = random.nextInt(10);
            }
            // 发现恶意交易的公证人信誉值加1~1.5
            float floatNum = random.nextFloat();
            if (floatNum > 0.5) {
                floatNum += 0.5f;
            } else {
                floatNum += 1;
            }
            samebeginOfCreditMapper.updateEndCreditById(finderId, floatNum);
            System.out.println("发现恶意交易的公证人信誉值加-----"+ String.valueOf(floatNum).substring(0,5));
        }*/ //else {
            System.out.println("跨链交易成功，公证人节点信誉值加1！！！");
            //获取原来的信誉值
            float credit =  samebeginOfCreditMapper.selectByPrimaryKey(notary.getId()).getEndcredit();
            samebeginOfCreditMapper.updateEndCreditById(notary.getId(), credit + 1);
            //}

        ++time;
        System.out.println("=============================================="+time+"=================================================");
        }


    @PostMapping(value = "/normalDistributionCredit")
    @ApiOperation(value = "测试初始信誉值为正态分布、均值、阶梯、线性，在公证人信誉机制下的信誉值变化，不考虑恶意节点")
    public void normalDistributionCredit() {
        NormalDistributionCredit notary = new NormalDistributionCredit();
        Random random = new Random();
        int badFlag = random.nextInt(20);

        /* 2、竞选公证人
        * 2.1  算信誉平均值
        * 2.2  highList---大于平均值的公证人集合
        * 2.3  lowList---小于平均值的公证人集合
        * 2.4  从highList选取公证人的概率为（highList的个数）/（highList公证人个数+lowList公证人个数）
        * 2.5  从lowList选取公证人的概率为（lowList的个数）/（highList公证人个数+lowList公证人个数）
        * */
        // 获取highList和lowList
        List<NormalDistributionCredit> highList = normalDistributionCreditMapper.selectHighNotary();
        List<NormalDistributionCredit> lowList = normalDistributionCreditMapper.selectLowNotary();
        // 计算lowList和highList的总个数
        int totalNotary = highList.size()+lowList.size();
        // 根据2.4，2.5的概率确定公证人从哪一组选取,并从对应组中随机选取公证人
        int id = random.nextInt(totalNotary)+1;
        if (id <= highList.size()) {
            System.out.println("从highList里面选取公证人！！！");
            notary = highList.get(random.nextInt(highList.size()));
            System.out.println("======================id=:"+notary.getId());
        } else {
            System.out.println("从lowList里面选取公证人！！！");
            notary = lowList.get(random.nextInt(lowList.size()));
            System.out.println("======================id=:"+notary.getId());
        }


        // 更新信誉值
        /*if (badFlag == 4) {
            // 获取第一公证人
            NormalDistributionCredit firstNotary = normalDistributionCreditMapper.selectFirstNotary().get(0);
            *//*
             * 1）如果flag为0，1则为由于网络原因造成交易未成功，真实公证人的信誉值减1
             * 1.1) 如果flag==1 且，flag2==1，则认为第一公证人将虚假交易验证通过，被其他公证人查出，这是真实公证人信誉值-1，第一公证人信誉值/2， 验出虚假交易的公证人信誉值增加1-1.5
             * 2）如果flag为2则为由于真实公证人伪造虚假交易导致交易未成功，真实公证人的信誉值置为0
             * 2.1) 如果flag==2 且，flag2==1，则认为第一公证人将虚假交易验证通过，被其他公证人查出，这是真实公证人信誉值==0，第一公证人信誉值/2， 验出虚假交易的公证人信誉值增加1-1.5
             * *//*
            int flag = random.nextInt(5);
            int flag2 = random.nextInt(2);
            if (flag == 2) {
                // 恶意公证人的节点信誉值置为0
                normalDistributionCreditMapper.updateEndCreditById(notary.getId(), 0);
                System.out.println("恶意节点的信誉值值为0！！！");

            } else {
                // 由于非人为元素，导致交易失败，恶意公证人的节点信誉值-1
                normalDistributionCreditMapper.updateEndCreditById(notary.getId(), notary.getEndcredit()-1);

            }
            //第一公证人将虚假交易验证通过，被其他公证人查出，第一公证人信誉值/2
            if (flag2 == 1) {
                normalDistributionCreditMapper.updateEndCreditById(firstNotary.getId(), firstNotary.getEndcredit()/2);
            }
            // 随机选取发现恶意交易的公证人
            int finderId =  random.nextInt(10);
            while (finderId == notary.getId()) {
                finderId = random.nextInt(10);
            }
            // 发现恶意交易的公证人信誉值加1~1.5
            float floatNum = random.nextFloat();
            if (floatNum > 0.5) {
                floatNum += 0.5f;
            } else {
                floatNum += 1;
            }
            normalDistributionCreditMapper.updateEndCreditById(finderId, floatNum);
            System.out.println("发现恶意交易的公证人信誉值加-----"+ String.valueOf(floatNum).substring(0,5));
        } *///else {
            System.out.println("跨链交易成功，公证人节点信誉值加1！！！");
            //获取原来的信誉值
            float credit =  normalDistributionCreditMapper.selectByPrimaryKey(notary.getId()).getEndcredit();
            normalDistributionCreditMapper.updateEndCreditById(notary.getId(), credit + 1);
       // }

        ++time;
        System.out.println("=============================================="+time+"=================================================");
    }
}
