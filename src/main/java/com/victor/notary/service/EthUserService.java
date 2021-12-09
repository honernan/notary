package com.victor.notary.service;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-09-26-19-44
 * */

public interface EthUserService {
    // 判断指定区块链是否存在指定账户
    public boolean isExistence(String account, String address);
    // 加入公证人组的用户id插入公证人组的用户表
    public boolean joinNatoryGroup(String ethId, String hyperId)throws Exception;

    // 退出公证人组
    public boolean exitNatoryGroup(String ethId, String hyperId)throws Exception;
}
