package com.victor.notary.service;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-10-14  20：32
 * */

import com.victor.notary.model.NotaryBetweenEth;
import java.util.List;

public interface NotaryBetweenEthService {
    /**根据源（第一条）以太坊的用户id获取目标以太坊链的用户id
     * @param userAddress1
     */
    public NotaryBetweenEth getNotaryByUserAddress1(String userAddress1);

    /**根据源（公证人组id获取公证人组信息
     * @param id
     * @return
     */
    public NotaryBetweenEth getNotaryById(int id);
    /**统计公证人组用户数
     * @return
     */
    public int getNotarysNum();
    /** 获取所有公证人组信息
     * @return
     */
    public List<NotaryBetweenEth> getAll();

    /**获取1链信誉值大于1链信誉平均值的几个账户
     * @param fromId
     * @return
     */
    public List<NotaryBetweenEth> getNotaryByCredit(int fromId);
    /**添加公证人
     * @param notaryBetweenEth
     * @return
     */
    public boolean addNotary(NotaryBetweenEth notaryBetweenEth);
}
