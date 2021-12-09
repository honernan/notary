package com.victor.notary.controller;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-11-26  11：02
 * */

import com.victor.notary.mapper.EthUser1Mapper;
import com.victor.notary.mapper.EthUser2Mapper;
import com.victor.notary.mapper.InteledgerNotaryMapper;
import com.victor.notary.model.EthUser1;
import com.victor.notary.model.EthUser2;
import com.victor.notary.model.InteledgerNotary;
import com.victor.notary.model.NotaryBetweenEth;
import com.victor.notary.service.CrossChainService;
import com.victor.notary.service.InterledgerService;
import com.victor.notary.service.NotaryBetweenEthService;
import com.victor.notary.service.WanWeiChainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/api/experiment1")
@Api(value = "实验一，interledger和公证人组跨链平均时间对比", tags = "实验一，interledger和公证人组跨链平均时间对比")
public class Experiment1Controller {
    private int time;
    private String bentime = "";

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private WanWeiChainService wanWeiChainService;
    @Resource
    private EthUser1Mapper ethUser1Mapper;
    @Resource
    private EthUser2Mapper ethUser2Mapper;
    @Resource
    private InteledgerNotaryMapper inteledgerNotaryMapper;
    @Autowired
    private InterledgerService interledgerService;
    @Autowired
    private CrossChainService crossChainService;
    @Autowired
    private NotaryBetweenEthService notaryBetweenEthService;

    @PostMapping("/interledger")
    @ApiOperation(value = "使用interledger实现以太坊之间跨链转账，用于并发实验，交易金额随机生成")
    public String interledger() {
        if (time == 0 || time == 120 || time == 240) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            bentime = df.format(new Date());
        }
        Random random = new Random();
        // 1、随机选择交易发起者和接收者
        List<EthUser1> ethUser1 = (List<EthUser1>)redisTemplate.opsForList().range("eth1UserInfo",0, -1).get(0);
        List<EthUser2> ethUser2 = (List<EthUser2>)redisTemplate.opsForList().range("eth2UserInfo",0, -1).get(0);
        EthUser1 oriAccount = ethUser1.get(random.nextInt(10));
        EthUser2 tarAccount = ethUser2.get(random.nextInt(10));

        // 2、公证人选举
        InteledgerNotary notary = new InteledgerNotary();
        //1.1) 随机生成OriAccount\TarAccounbt的最大容错阈值（9-18）最大容错阈值可根据参与节点的阈值而定
        int oriFmax = random.nextInt(10) + 9;
        int tarFmax = random.nextInt(10) + 9;
        //1.2) 从候选公证人中选择容错值不大于oriFmax且不大于tarFmax的公证人集
        List<InteledgerNotary> notaryList = inteledgerNotaryMapper.selectByFmax(oriFmax, tarFmax, oriAccount.getUseraddress(), tarAccount.getUseraddress());
        int size = notaryList.size();
        // 1.2.1) 如果notaryList>0 则发送方依赖原子模式，否则使用通用模式
        if (size == 1) {
            notary = notaryList.get(0);
        }
        if (size > 1) {
            notary = notaryList.get(random.nextInt(size));
        }
        // 通用模式,无公证人
        if (size < 1) {
            Map<String, Object> oriTxInfo = new HashMap<>(2);
            int money = 0;
            // 假定分类账账户为0xe3d59dc0327a346ecbcf45f3409f1040a192a4fd和0xc9e5ed88509b1943b8cdba6a4f786200802a6a73
            //3、 分类账向目标节点转账
            money = random.nextInt(5) + 1;
            try {
                while (tarAccount.getUseraddress() == "0xc9e5ed88509b1943b8cdba6a4f786200802a6a73") {
                    tarAccount = ethUser2.get(random.nextInt(10));
                }
                oriTxInfo = interledgerService.interledgerSend("0xc9e5ed88509b1943b8cdba6a4f786200802a6a73", "http://10.1.4.204:8549",
                       "UTC--2019-10-14T11-44-53.019947626Z--c9e5ed88509b1943b8cdba6a4f786200802a6a73.json", tarAccount.getUseraddress(),  BigInteger.valueOf(money), 1);
            }catch (Exception e) {
                System.out.println("目标节点所在区块链发起交易失败！！！");
                e.getMessage();
                e.printStackTrace();
            }
            //4、 分类账扣除源节点的托管资金money+1,其中奖励金为1
            try {
                while (oriAccount.getUseraddress() == "0xe3d59dc0327a346ecbcf45f3409f1040a192a4fd") {
                    oriAccount = ethUser1.get(random.nextInt(10));
                }
                oriTxInfo = interledgerService.interledgerSend(oriAccount.getUseraddress(), "http://10.1.4.192:8547", oriAccount.getUserkeyfilejson(), "0xe3d59dc0327a346ecbcf45f3409f1040a192a4fd",
                        BigInteger.valueOf(money + 1), 0);
            } catch (Exception e) {
                System.out.println("源节点所在区块链发起交易失败！！！");
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
        //3、 发起人向公证人转账
        Map<String, Object> oriTxInfo = new HashMap<>(2);
        int money = 0;
        try {
            //3.1 oriAccount向公证人转账  金额随机转1-5,其中奖励金为1
            money = random.nextInt(5) + 1;
            oriTxInfo = interledgerService.interledgerSend(oriAccount.getUseraddress(), "http://10.1.4.192:8547", oriAccount.getUserkeyfilejson(), notary.getUseraddress1(),
                    BigInteger.valueOf(money + 1), 0);
        } catch (Exception e) {
            System.out.println("源节点所在区块链发起交易失败！！！");
            e.printStackTrace();
        }
        //4、 公证人向目标节点转账
        try {
            interledgerService.interledgerSend(notary.getUseraddress2(), "http://10.1.4.204:8549",
                    notary.getUserkeyfilejson2(), tarAccount.getUseraddress(),  BigInteger.valueOf(money), 1);
        }catch (Exception e) {
            System.out.println("目标节点所在区块链发起交易失败！！！");
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
    @PostMapping("/notaryGroupSendMoney")
    @ApiOperation(value = "使用公证人组实现以太坊之间跨链转账，用于并发实验，交易金额随机生成")
    public String notaryGroupSendMoney() {
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

   /* public void wanweiChain() {
        Random random = new Random();
        // 1、随机选取源链的发起人(数据库\redis中源链用户一共10个)
        int sid = random.nextInt(10)+1;
        while (sid == 1) {
            sid = random.nextInt(10)+1;
        }
        List<EthUser1> ethUser1 = (List<EthUser1>)redisTemplate.opsForList().range("eth1UserInfo",0, -1).get(0);
        EthUser1 oriAccount = ethUser1.get(sid - 1);
        // 2、随机选取目标节点(数据库\redis中目标链用户一共10个)
        int tid = random.nextInt(10)+1;
        while (tid == 1) {
            tid = random.nextInt(10)+1;
        }
        List<EthUser2> ethUser2 = (List<EthUser2>)redisTemplate.opsForList().range("eth2UserInfo",0, -1).get(0);
        EthUser2 tarAccount = ethUser2.get(sid - 1);

        //3、 默认ethUser1、ethUser2的第一个用户为万维链的锁定账户Locked Account
        //3.1 oriAccount向万维链的锁定账户Locked Account转账  金额随机转1-5
        int money = random.nextInt(5)  + 1;
        Map<String, Object> oriTxInfo = new HashMap<>(2);
        try {
            oriTxInfo = wanWeiChainService.wanWeiChainSend(oriAccount.getUseraddress(), "http://10.1.4.192:8547", oriAccount.getUserkeyfilejson(), "0xe3d59dc0327a346ecbcf45f3409f1040a192a4fd",
                    BigInteger.valueOf(money), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //4、万维链证明节点（Voucher)验证交易---通过pos---TLF = checkCommitment()

        //5、



    }*/
}
