package com.victor.notary.service.impl;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-10-14  20：24
 * */

import com.victor.notary.mapper.TxInfoBetweenEthMapper;
import com.victor.notary.model.TxInfoBetweenEth;
import com.victor.notary.service.TxInfoBetweenEthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service(value = "txInfoBetweenEthService")
public class TxInfoBetweenEthServiceImpl implements TxInfoBetweenEthService {
    @Resource
    private TxInfoBetweenEthMapper txInfoBetweenEthMapper;

    @Override
    public void addTx(TxInfoBetweenEth txInfo) {
        txInfoBetweenEthMapper.insert(txInfo);
    }
}
