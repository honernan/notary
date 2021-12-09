package com.victor.notary.mapper;

import com.victor.notary.model.EthUser2;

import java.util.List;

public interface EthUser2Mapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EthUser2 record);

    int insertSelective(EthUser2 record);

    EthUser2 selectByPrimaryKey(Integer id);

    /** 根据用户地址获取用户信息
     * @param userAddress
     * @return
     */
    EthUser2 selectByUserAddress(String userAddress);

    int updateByPrimaryKeySelective(EthUser2 record);

    /**根据用户地址更新用户余额
     * @param userAddress
     * @param money
     * @return
     */
    int updateMoneyByUserAddr(String userAddress, String money);
    /**获取所有的用户对象
     * @return
     */
    List<EthUser2> selectAll();
}