package com.victor.notary.mapper;

import com.victor.notary.model.EthUser1;
import com.victor.notary.model.NotaryBetweenEth;

import java.util.List;

public interface EthUser1Mapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EthUser1 record);

    int insertSelective(EthUser1 record);

    EthUser1 selectByPrimaryKey(Integer id);/** 根据用户地址获取用户信息
     * @param userAddress
     * @return
     */
    EthUser1 selectByUserAddress(String userAddress);

    int updateByPrimaryKeySelective(EthUser1 record);


    /**根据用户地址更新用户余额
     * @param userAddress
     * @param money
     * @return
     */
    int updateMoneyByUserAddr(String userAddress, String money);

    /**获取所有的用户对象
     * @return
     */
    List<EthUser1> selectAll();
}