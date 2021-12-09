package com.victor.notary.controller;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2020-02-04  10：35
 * */

import com.victor.notary.model.NotaryBetweenEth;
import com.victor.notary.model.SecondPoint;
import com.victor.notary.service.CrossChainService;
import com.victor.notary.service.NotaryBetweenEthService;
import com.victor.notary.service.SecondPointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/api/secondpoint")
@Api(value = "大论文",tags = "第二个点")
public class SecondpointController {
    @Autowired
    private SecondPointService secondPointService;
    @Autowired
    private CrossChainService crossChainService;
    @Autowired
    private NotaryBetweenEthService notaryBetweenEthService;

    @PostMapping(value = "/insert")
    @ApiOperation(value = "secondpoint表初始化")
    public void insertRecord() {
        SecondPoint secondPoint = new SecondPoint();
        for (int i=1; i<101; i++) {
            secondPoint.setId(i);
            if (i < 51) {
                secondPoint.setMoney((double)i);
                secondPoint.setCredit((double)i);

            }else {
                break;
            }
            secondPoint.setSuccessnum(0);
            secondPoint.setFailnum(0);
            secondPoint.setHaderror(0);
            secondPointService.insert(secondPoint);
        }
        for (int i=1; i < 51; i++) {
            secondPoint.setId(i+50);
            secondPoint.setCredit((double)(51-i));
            secondPoint.setMoney((double)(51-i));

            secondPoint.setSuccessnum(0);
            secondPoint.setFailnum(0);
            secondPoint.setHaderror(0);
            secondPointService.insert(secondPoint);
        }
    }
    @PostMapping(value = "/profile")
    @ApiOperation(value = "公证人利润变化")
    public void experiment1(){
        List<SecondPoint> notaryList = secondPointService.selecAll();
        // 1、选择公证人
        //1.1、公证人组的平均利润
        int totalnum = notaryList.size();
        double totalMoney = secondPointService.selectTotalMoney();
        double avgMoney = (double) totalMoney/totalnum;
        // 1.2选择小于等于平均利润的公证人
        List<SecondPoint> lowerMoneyNotaryList = secondPointService.selectLower(avgMoney);
        int lowerNum = lowerMoneyNotaryList.size();
        // 1.3选择大于平均利润的公证人
        List<SecondPoint> higherMoneyNotaryList = secondPointService.selectHigher(avgMoney);
        int higherNum = higherMoneyNotaryList.size();
        // 1.4根据lowerNum：higherNum的比例决定从哪一组选择公证人
        SecondPoint realNotary = new SecondPoint();
        Random random = new Random();
        int ran = random.nextInt(lowerNum+higherNum);
        if (ran <= lowerNum){
            //获取lowerMoneyNotaryList中总的信誉值
            double totalCredit1 = secondPointService.totalCreditOfLower(avgMoney);
            double temp = 0;
            int random2 = random.nextInt((int)Math.floor(totalCredit1));
            for (int i = 0; i < lowerNum; i++) {
                temp += lowerMoneyNotaryList.get(i).getCredit();
                if (temp >= random2) {
                    realNotary = lowerMoneyNotaryList.get(i);
                    break;
                }
            }
        }else{
            //获取higherMoneyNotaryList中总的信誉值
            double totalCredit2 = secondPointService.totalCreditOfHigher(avgMoney);
            double temp2 = 0;
            int random3 = random.nextInt((int)Math.floor(totalCredit2));
            for (int i = 0; i < higherNum; i++) {
                temp2 += higherMoneyNotaryList.get(i).getCredit();
                if (temp2 >= random3) {
                    realNotary = higherMoneyNotaryList.get(i);
                    break;
                }
            }
        }
        //2、真实公证人利润
        //2.1 规定交易额均为100，手续费比例k=0.01,
        double M =100;
        double k = 0.01;
        double profile = M*k;
        int totalCount = secondPointService.totalTrade();
        int count1 = realNotary.getSuccessnum();
        //对于在higher组取得值， log(2-...)
        double temp = Math.log(2-realNotary.getMoney()/totalMoney)/Math.log(2);
        //对于在lower组取得值， log(3-...)
        if (ran <= lowerNum) {
            temp = Math.log(3-realNotary.getMoney()/totalMoney)/Math.log(2);
        }
        if (totalCount == 0 || count1 == 0) {
            profile = profile*temp;
        } else {
            profile = profile*(1-count1/totalCount)*temp;
        }
        // 更新真实公证人的利润
        realNotary.setMoney(realNotary.getMoney()+profile);
        realNotary.setSuccessnum(realNotary.getSuccessnum()+1);
        int recordNum = secondPointService.updateByPrimaryKey(realNotary);

    }

    @PostMapping(value = "/credit")
    @ApiOperation(value = "公证人信誉值变化")
    public void experiment2(){
        List<SecondPoint> notaryList = secondPointService.selecAll();
        // 1、选择公证人
        //1.1、公证人组的平均利润
        int totalnum = notaryList.size();
        double totalMoney = secondPointService.selectTotalMoney();
        double avgMoney = (double) totalMoney/totalnum;
        // 1.2选择小于等于平均利润的公证人
        List<SecondPoint> lowerMoneyNotaryList = secondPointService.selectLower(avgMoney);
        int lowerNum = lowerMoneyNotaryList.size();
        // 1.3选择大于平均利润的公证人
        List<SecondPoint> higherMoneyNotaryList = secondPointService.selectHigher(avgMoney);
        int higherNum = higherMoneyNotaryList.size();
        // 1.4根据lowerNum：higherNum的比例决定从哪一组选择公证人
        SecondPoint realNotary = new SecondPoint();
        Random random = new Random();
        int ran = random.nextInt(lowerNum+higherNum);
        System.out.println(ran);
        // 两个组中的信誉值总和
        double totalCredit1 = secondPointService.totalCreditOfLower(avgMoney);
        double totalCredit2 = secondPointService.totalCreditOfHigher(avgMoney);;
        if (ran <= lowerNum){
            //获取lowerMoneyNotaryList中总的信誉值
           // totalCredit1 = secondPointService.totalCreditOfLower(avgMoney);
            double temp = 0;
            int random2 = random.nextInt((int)Math.floor(totalCredit1));
            for (int i = 0; i < lowerNum; i++) {
                temp += lowerMoneyNotaryList.get(i).getCredit();
                if (temp >= random2) {
                    realNotary = lowerMoneyNotaryList.get(i);
                    System.out.println("真实公证人："+realNotary.getId());
                    break;
                }
            }
        }else{
            //获取higherMoneyNotaryList中总的信誉值
            //totalCredit2 = secondPointService.totalCreditOfHigher(avgMoney);
            double temp2 = 0;
            int random3 = random.nextInt((int)Math.floor(totalCredit2));
            for (int i = 0; i < higherNum; i++) {
                temp2 += higherMoneyNotaryList.get(i).getCredit();
                if (temp2 >= random3) {
                    realNotary = higherMoneyNotaryList.get(i);
                    break;
                }
            }
        }

        //2、真实公证人利润
        //2.1 规定交易额均为100，手续费比例k=0.01,
        double M =100;
        double k = 0.01;
        double profile = M*k;
        int totalCount = secondPointService.totalTrade();
        int count1 = realNotary.getSuccessnum();
        //对于在higher组取得值， log(2-...)
        double temp = Math.log(2-realNotary.getMoney()/totalMoney)/Math.log(2);
        //对于在lower组取得值， log(3-...)
        if (ran <= lowerNum) {
            temp = Math.log(3-realNotary.getMoney()/totalMoney)/Math.log(2);
        }
        if (totalCount == 0 || count1 == 0) {
            profile = profile*temp;
        } else {
            profile = profile*(1-count1/totalCount)*temp;
        }
        // 更新真实公证人的利润
        realNotary.setMoney(realNotary.getMoney()+profile);


        //3、真实公证人信誉值
        //3.1 规定基础信誉值v=2
        double v = 2;
        double avgCredit = (totalCredit1+totalCredit2)/notaryList.size();//整个公证人组的平均信誉值
        double realNotaryCredit = realNotary.getCredit();
        BigDecimal data1 = new BigDecimal(avgCredit);
        BigDecimal data2 = new BigDecimal(realNotaryCredit);
        //3.2 计算交易成功率
        double successrate = 0;
        if (realNotary.getFailnum() > 0) {
            successrate = realNotary.getSuccessnum()/(realNotary.getSuccessnum()+realNotary.getFailnum());
        }else {
            successrate = 1;
        }
        double credit = 0;//公证人获得的信誉值
        double temp2 = (avgCredit-realNotary.getCredit())/(totalCredit1+totalCredit2);
        if (data1.compareTo(data2) > 0) {
            credit = v*successrate*Math.pow(Math.E,temp2+0.5);
        }else {
            credit = v*successrate*Math.pow(Math.E,temp2-0.5);
        }

        // 更新真实公证人的利润
        realNotary.setCredit(realNotary.getCredit()+credit);
        realNotary.setSuccessnum(realNotary.getSuccessnum()+1);

        int recordNum = secondPointService.updateByPrimaryKey(realNotary);
    }

    @PostMapping(value = "/time")
    @ApiOperation(value = "平均交易时间")
    public void experiment3(){
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        System.out.println("开始时间:"+ new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(date));//精确至毫秒
        List<SecondPoint> notaryList = secondPointService.selecAll();
        SecondPoint realNotary = new SecondPoint(); //真实公证人
        Random random = new Random();
        // 1、选择公证人
        //1.1 随机生成1-100的交易额，查看保证金池余额是否足够
        double transferMoney = (double) random.nextInt(10000);
        if (notaryList.get(100).getMoney() >= transferMoney) {
            realNotary = notaryList.get(100);
            //1.1.1 手续费比例k=0.01,
            double k = 0.01;
            double profile = transferMoney*k;
            // 更新真实公证人的利润
            realNotary.setMoney(realNotary.getMoney()+profile-transferMoney);
            realNotary.setSuccessnum(realNotary.getSuccessnum()+1);
            int recordNum = secondPointService.updateByPrimaryKey(realNotary);
        } else {
            //1.1、公证人组的平均利润
            int totalnum = notaryList.size();
            double totalMoney = secondPointService.selectTotalMoney();
            double avgMoney = (double) totalMoney / totalnum;
            // 1.2选择小于等于平均利润的公证人
            List<SecondPoint> lowerMoneyNotaryList = secondPointService.selectLower(avgMoney);
            int lowerNum = lowerMoneyNotaryList.size();
            // 1.3选择大于平均利润的公证人
            List<SecondPoint> higherMoneyNotaryList = secondPointService.selectHigher(avgMoney);
            int higherNum = higherMoneyNotaryList.size();
            // 1.4根据lowerNum：higherNum的比例决定从哪一组选择公证人
            int ran = random.nextInt(lowerNum + higherNum);
            System.out.println(ran);
            // 两个组中的信誉值总和
            double totalCredit1 = secondPointService.totalCreditOfLower(avgMoney);
            double totalCredit2 = secondPointService.totalCreditOfHigher(avgMoney);
            ;
            if (ran <= lowerNum) {
                //获取lowerMoneyNotaryList中总的信誉值
                // totalCredit1 = secondPointService.totalCreditOfLower(avgMoney);
                double temp = 0;
                int random2 = random.nextInt((int) Math.floor(totalCredit1));
                for (int i = 0; i < lowerNum; i++) {
                    temp += lowerMoneyNotaryList.get(i).getCredit();
                    if (temp >= random2) {
                        realNotary = lowerMoneyNotaryList.get(i);
                        System.out.println("真实公证人：" + realNotary.getId());
                        break;
                    }
                }
            } else {
                //获取higherMoneyNotaryList中总的信誉值
                //totalCredit2 = secondPointService.totalCreditOfHigher(avgMoney);
                double temp2 = 0;
                int random3 = random.nextInt((int) Math.floor(totalCredit2));
                for (int i = 0; i < higherNum; i++) {
                    temp2 += higherMoneyNotaryList.get(i).getCredit();
                    if (temp2 >= random3) {
                        realNotary = higherMoneyNotaryList.get(i);
                        break;
                    }
                }
            }
            //2、真实公证人利润
            //2.1 规定交易额均为100，手续费比例k=0.01,
            double M = 100;
            double k = 0.01;
            double profile = M * k;
            int totalCount = secondPointService.totalTrade();
            int count1 = realNotary.getSuccessnum();
            //对于在higher组取得值， log(2-...)
            double temp = Math.log(2 - realNotary.getMoney() / totalMoney) / Math.log(2);
            //对于在lower组取得值， log(3-...)
            if (ran <= lowerNum) {
                temp = Math.log(3 - realNotary.getMoney() / totalMoney) / Math.log(2);
            }
            if (totalCount == 0 || count1 == 0) {
                profile = profile * temp;
            } else {
                profile = profile * (1 - count1 / totalCount) * temp;
            }
            // 更新真实公证人的利润
            realNotary.setMoney(realNotary.getMoney() + profile);


            //3、真实公证人信誉值
            //3.1 规定基础信誉值v=2
            double v = 2;
            double avgCredit = (totalCredit1 + totalCredit2) / notaryList.size();//整个公证人组的平均信誉值
            double realNotaryCredit = realNotary.getCredit();
            BigDecimal data1 = new BigDecimal(avgCredit);
            BigDecimal data2 = new BigDecimal(realNotaryCredit);
            //3.2 计算交易成功率
            double successrate = 0;
            if (realNotary.getFailnum() > 0) {
                successrate = realNotary.getSuccessnum() / (realNotary.getSuccessnum() + realNotary.getFailnum());
            } else {
                successrate = 1;
            }
            double credit = 0;//公证人获得的信誉值
            double temp2 = (avgCredit - realNotary.getCredit()) / (totalCredit1 + totalCredit2);
            if (data1.compareTo(data2) > 0) {
                credit = v * successrate * Math.pow(Math.E, temp2 + 0.5);
            } else {
                credit = v * successrate * Math.pow(Math.E, temp2 - 0.5);
            }

            // 更新真实公证人的利润
            realNotary.setCredit(realNotary.getCredit() + credit);
            realNotary.setSuccessnum(realNotary.getSuccessnum() + 1);

            int recordNum = secondPointService.updateByPrimaryKey(realNotary);
        }
        // 结束时间
        System.out.println("结束时间:"+ new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(new Date()));//精确至毫秒
    }


}
