package com.victor.notary.mapper;

import com.victor.notary.model.TxInfoBetweenEth;

public interface TxInfoBetweenEthMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TxInfoBetweenEth record);

    int insertSelective(TxInfoBetweenEth record);

    TxInfoBetweenEth selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TxInfoBetweenEth record);

    int updateByPrimaryKey(TxInfoBetweenEth record);
}