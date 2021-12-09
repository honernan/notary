package com.victor.notary.controller;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-10-13  10：43
 * */

import com.victor.notary.model.NotaryBetweenEth;
import com.victor.notary.model.TxInfoBetweenEth;
import com.victor.notary.service.CrossChainService;
import com.victor.notary.service.NotaryBetweenEthService;
import com.victor.notary.service.TxInfoBetweenEthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping(value = "/api/crossChain")
@Api(value = "跨链交易API",tags = "跨链交易API")
public class CrossChainController {

    @Autowired
    private CrossChainService crossChainService;
    @Autowired
    private TxInfoBetweenEthService txInfoBetweenEthService;
    @Autowired
    private NotaryBetweenEthService notaryBetweenEthService;
    @Autowired
    private RedisTemplate redisTemplate;

    private int time = 0;
    private String bentime = "";

    /*
    * 1、从0-200随机抽取一个数，如果为4则该交易为恶意节点承担公证人（在源链收到保证金后，不从目标链转回给目标节点）
    *   1.1、 这时需要有保证金账户（公证人表中id=1的账户）发起这笔交易（公证人-->目标节点）
    *   1.2、 将原公证人的信誉值置为0，也就是所有公证人账户都发生一次恶意行为（信誉值都为0）该公证人才有机会重新获得机会担任公证人
    *  2、如果是正常交易，正从公证人表中选举公证人
    *     2.1  先找到credit1>=AVG（credit1）,的公证人，
    *     //2.1.1  然后在这些公证人中找到credit2最大的一个或多个公证人(删除本步骤，因为会造成一个公证人的信誉越来越高，也就是公证人永远是他)
    *     2.1.2 如果只有一个候选公证人，那么这个公证人就是真实公证人
    *     2.1.2 如果credit2最大且相等的公证人有多个，那就随机选取一个作为真实公证人
    *     2.2  在目标链交易发起成功后公证人在两条链上的credit都+1
    * */
    @RequestMapping("/sendMoneyBetweenEths")
    @ApiOperation(value = "以太坊之间跨链转账，用于并发实验，交易金额随机生成")
    public String sendMoneyBetweenEths() {
        if (time == 0 || time == 120 || time == 240) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            bentime = df.format(new Date());
        }
        // 1、随机选取源链交易发起人（源链交易）   只选取1-9号，0号为保证金池
        //List<NotaryBetweenEth> notaryBetweenEths = notaryBetweenEthService.getAll();
        List<NotaryBetweenEth> notaryBetweenEths = (List<NotaryBetweenEth>)redisTemplate.opsForList().range("notaryInfo",0, -1).get(0);
        int total = notaryBetweenEthService.getNotarysNum();
        NotaryBetweenEth fromNotary = null;
        NotaryBetweenEth toNotary = null;
        int id = (int)redisTemplate.opsForValue().get("usernumber");
        System.out.println(id);
        if (id < total-1 && id != 0) {
            redisTemplate.opsForValue().set("usernumber",id+1);
            fromNotary = notaryBetweenEths.get(id);
        } else if(id == total-1) {
            redisTemplate.opsForValue().set("usernumber",1);
            fromNotary = notaryBetweenEths.get(id);
        } else  {
            redisTemplate.opsForValue().set("usernumber",2);
            fromNotary = notaryBetweenEths.get(1);
        }
        // 2、竞选公证人
           // 2.1获取1链信誉值大于1链信誉平均值的几个账户
        List<NotaryBetweenEth> notaryBetweenEthList = notaryBetweenEthService.getNotaryByCredit(fromNotary.getId());
           // 2.2从notaryBetweenEthList中随机选取一个作为公证人
        int totalCandite = notaryBetweenEthList.size();
        System.out.println("totalCandite==" + totalCandite);
        if (totalCandite == 1) {
            toNotary = notaryBetweenEthList.get(0);
        } else  {
            //2.2.1创建一个Random类对象实例
            Random rd = new Random();
            int num = rd.nextInt(totalCandite);
            toNotary = notaryBetweenEthList.get(num);
        }
         // 转账金额
        int money = 0;
        int badFlag = 0;
        /*
        * 源链转账   源节点-->保证金池
        * 金额 ：money+2
        * 保证金池账户  0xc9e5ed88509b1943b8cdba6a4f786200802a6a73  数据库的第一个公证人
        * */
        try {
            //获取5-10的随机数
            money = (int)(5 + Math.random()*(10 -5+1));
            System.out.println("需要转账的金额为：" + money);
            /*crossChainService.localSendEther(fromNotary.getUseraddress1(), "http://172.23.17.135:8547", fromNotary.getUserkeyfilejson1(), toNotary.getUseraddress1(),
                    BigInteger.valueOf(money), 0, toNotary, badFlag);*/
            crossChainService.localSendEther(fromNotary.getUseraddress1(), "http://10.1.4.192:8547", fromNotary.getUserkeyfilejson1(), "0xc9e5ed88509b1943b8cdba6a4f786200802a6a73",
                    BigInteger.valueOf(money + 2), 0, toNotary, badFlag);
        }catch (Exception e) {
            System.out.println("源节点所在区块链发起交易失败！！！");
            e.getMessage();
            e.printStackTrace();
        }

        /*
         * 目标链转账  公证人-->目标节点
         * 金额 ：money
         * 保证金池账户  0xc9e5ed88509b1943b8cdba6a4f786200802a6a73  数据库的第一个公证人
         * */
        try {
            Random random = new Random();
            badFlag = random.nextInt(200);
            int toNum = random.nextInt(totalCandite);
                    //(int)(1 + Math.random()*(10 -1+1));
            if (badFlag == 4) {
                System.out.println("恶意节点不转帐！！！保证金池（id=1）发起补偿交易!!!");
                /*crossChainService.localSendEther(notaryBetweenEths.get(0).getUseraddress2(), "http://172.23.17.136:8549", notaryBetweenEths.get(0).getUserkeyfilejson2(),
                        notaryBetweenEths.get(toNum).getUseraddress2(),
                        BigInteger.valueOf(money), 1, toNotary, badFlag);*/
                crossChainService.localSendEther("0xc9e5ed88509b1943b8cdba6a4f786200802a6a73", "http://10.1.4.204:8549",
                        "UTC--2019-10-14T11-44-53.019947626Z--c9e5ed88509b1943b8cdba6a4f786200802a6a73.json",
                        notaryBetweenEths.get(toNum).getUseraddress2(),
                        BigInteger.valueOf(money), 1, toNotary, badFlag);
            } else {
                /*crossChainService.localSendEther(toNotary.getUseraddress2(), "http://172.23.17.136:8549", toNotary.getUserkeyfilejson2(),
                        notaryBetweenEths.get(toNum).getUseraddress2(),
                        BigInteger.valueOf(money), 1, toNotary, badFlag);*/
                crossChainService.localSendEther(toNotary.getUseraddress2(), "http://10.1.4.204:8549", toNotary.getUserkeyfilejson2(),
                        notaryBetweenEths.get(toNum).getUseraddress2(),
                        BigInteger.valueOf(money), 1, toNotary, badFlag);
            }
        }catch (Exception e) {
            System.out.println("目标节点所在区块链发起交易失败！！！");
            e.getMessage();
            e.printStackTrace();
        }
        /*
         * 源链转账   保证金池-->公证人（如果目标链交易失败badFlag == 4 则什么都不做，在前面已经发起过补偿交易了）
         * 金额 ：money +1
         * 保证金池账户  0xe3d59dc0327a346ecbcf45f3409f1040a192a4fd  数据库的第一个公证人
         * */
        try {
            /*crossChainService.localSendEther(fromNotary.getUseraddress1(), "http://172.23.17.135:8547", fromNotary.getUserkeyfilejson1(), toNotary.getUseraddress1(),
                    BigInteger.valueOf(money), 0, toNotary, badFlag);*/
            if (badFlag == 4) {
            } else {
                crossChainService.localSendEther("0xe3d59dc0327a346ecbcf45f3409f1040a192a4fd", "http://10.1.4.192:8547",
                        "UTC--2019-08-13T13-44-59.713748167Z--e3d59dc0327a346ecbcf45f3409f1040a192a4fd.json", toNotary.getUseraddress1(),
                        BigInteger.valueOf(money + 1), 0, toNotary, 0);
            }
        }catch (Exception e) {
            System.out.println("保证金池在源链转账失败！！！");
            e.getMessage();
            e.printStackTrace();
        }
        time++;
        System.out.println("========================================="+time+"=============================================================");
        if (time == 120 || time == 240 || time== 360) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("begin at:   " + bentime);
            System.out.println("end at:   " + df.format(new Date()));
        }

        return "成功在以太坊之间完成一次跨链交易！！！";
    }

    @PostMapping("/veify")
    @ApiOperation(value = "根据区块号验证交易信息")
    public String verifyTransactionByBlockid(@RequestParam(value = "sourceUrl", required = true) String sourceUrl,
                                             @RequestParam(value = "blockId", required = true) long blockId,
                                             @RequestParam(value = "txHash", required = true) String txHash,
                                             @RequestParam(value = "senderAddress", required = true) String from,
                                             @RequestParam(value = "receiverAddress", required = true) String to,
                                             @RequestParam(value = "money", required = true) BigInteger money) {
        boolean flag = false;
        try {
            flag = crossChainService.verifyTransactionByBlockid(sourceUrl,blockId, txHash, from, to, money);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag == true ? "已查询到该交易！！！":"该交易不存在或交易信息有误！！！";
    }


    @PostMapping("/sendMoney")
    @ApiOperation(value = "以太坊之间转账，金额根据参数决定")
    public String sendMoney(@RequestParam(value = "fromAddress", required = true) String from,
                            @RequestParam(value = "toAddress", required = true) String to,
                            @RequestParam(value = "sourceFlag", required = true) int sourceFlag,
                            @RequestParam(value = "money", required = true) long money) throws IOException {
        //jsonFileName = "UTC--2019-08-13T13-44-59.713748167Z--e3d59dc0327a346ecbcf45f3409f1040a192a4fd.json";

        //设置交易信息
        TxInfoBetweenEth txInfoBetweenEth = new TxInfoBetweenEth();
        txInfoBetweenEth.setStarttime(new Date());
        txInfoBetweenEth.setFrom(from);
        txInfoBetweenEth.setTo(to);
        txInfoBetweenEth.setMoney(String.valueOf(money));
        // 初始值
        String sourceUrl = "";
        String targetUrl = "";
        if (sourceFlag == 1) {
            sourceUrl = "http://172.23.17.135:8547";
            targetUrl = "http://172.23.17.136:8549";
        }else {
            sourceUrl = "http://172.23.17.136:8549";
            targetUrl = "http://172.23.17.135:8547";
        }
        // 竞选公证人
        //TODO

        // 源链转账，从源节点-->公证人节点
        EthBlock.TransactionObject txInfo = null;
        try {
            // ======================TODO
            txInfo = crossChainService.sendMoney(from, "0x2eb4d54849713a004684a3acc70996c51fede178", sourceUrl, "UTC--2019-08-14T08-01-56.648563262Z--2eb4d54849713a004684a3acc70996c51fede178.json", money, 0);
        }catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        // 公证人组验证源链上交易
        boolean flag = false;
        try {
            // ======================TODO
          // flag = crossChainService.verifyTransaction(txInfo, sourceUrl, from, "0x2eb4d54849713a004684a3acc70996c51fede178", money);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!flag)
            return "源节点所在区块链发起交易失败！！！";

        // 目标链转账   公证人-->目标节点
        NotaryBetweenEth notaryBetweenEth = notaryBetweenEthService.getNotaryByUserAddress1("0xe3d59dc0327a346ecbcf45f3409f1040a192a4fd");
        EthBlock.TransactionObject txInfo2 = null;
        try {
            txInfo2 = crossChainService.sendMoney(notaryBetweenEth.getUseraddress2(), to, targetUrl, notaryBetweenEth.getUserkeyfilejson2(), money, 1);
        }catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        // 公证人组验证目标链上交易
        boolean flag2 = false;
        try {
           // flag2 = crossChainService.verifyTransaction(txInfo2, targetUrl, notaryBetweenEth.getUseraddress2(), to, money);
            flag2 = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!flag2)
            return "目标链所在区块链发起交易失败！！！";

        //
        return "交易成功！！！";
    }
}
