package com.victor.notary.service.impl;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-09-26-19-43
 * */

import com.victor.notary.service.EthUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.http.HttpService;

import java.util.List;

@Service(value = "userService")
public class EthUserServiceImpl implements EthUserService {
    /*@Resource
    private UserMapper userMapper;*/

    @Override
    // 判断指定区块链是否存在指定账户
    public boolean isExistence(String account, String address) {
        // connect to node
        Web3j web3 = Web3j.build(new HttpService(address));
        EthAccounts ethAccounts = null;
        try {
            ethAccounts =  web3.ethAccounts().send();
        }catch (Exception e){
            e.getMessage();
        }

        List<String> accountList = ethAccounts.getResult();
        boolean flag = accountList.contains(account);

        return flag;
    }

    // 加入公证人组
    @Override
    @Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public boolean joinNatoryGroup(String ethId, String hyperId) throws Exception{
       /* Date date = new Date();
        User user = new User();
        user.setEntertime(date);
        user.setEthereumid(ethId);
        user.setHyperledgerid(hyperId);
        int nums = 0;
        try {
            nums = userMapper.insert(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (nums <=0)
            return false;*/
        return true;
    }

    // 退出公证人组
    @Override
    @Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public boolean exitNatoryGroup(String ethId, String hyperId)throws Exception {
        int nums = 0;
       /* try {
            nums = userMapper.deleteByEthidOrHyperId(hyperId, ethId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }*/
        if (nums <= 0)
            return false;
        return true;
    }
}
