package com.victor.notary.service.impl;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-10-14  20：35
 * */

import com.victor.notary.mapper.NotaryBetweenEthMapper;
import com.victor.notary.model.NotaryBetweenEth;
import com.victor.notary.service.NotaryBetweenEthService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service(value = "NotaryBetweenEthService")
public class NotaryBetweenEthServiceImpl implements NotaryBetweenEthService {
    @Resource
    private NotaryBetweenEthMapper notaryBetweenEthMapper;

    /**根据1链地址获取公证人组系统信息
     * @param userAddress1
     * @return
     */
    @Override
    public NotaryBetweenEth getNotaryByUserAddress1(String userAddress1) {
        NotaryBetweenEth notaryBetweenEth = notaryBetweenEthMapper.selectByUser1(userAddress1);
        return notaryBetweenEth;
    }

    /**根据源（公证人组id获取公证人组信息
     * @param id
     * @return
     */
    @Override
    public NotaryBetweenEth getNotaryById(int id) {
        return notaryBetweenEthMapper.selectById(id);
    }

    /**统计公证人组用户数
     * @return
     */
    @Override
    public int getNotarysNum() {
        return notaryBetweenEthMapper.countNotary();
    }

    /** 获取所有公证人组信息
     * @return
     */
    @Override
    public List<NotaryBetweenEth> getAll() {
        return notaryBetweenEthMapper.selectAll();
    }
    /**获取1链信誉值大于1链信誉平均值的几个账户
     * @param fromId
     * @return
     */
    @Override
    public List<NotaryBetweenEth> getNotaryByCredit(int fromId) {
        return notaryBetweenEthMapper.selectNotary(fromId);
    }

    /**添加公证人
     * @param notaryBetweenEth
     * @return
     */
    @Override
    public boolean addNotary(NotaryBetweenEth notaryBetweenEth) {
        try {
            notaryBetweenEthMapper.insert(notaryBetweenEth);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
