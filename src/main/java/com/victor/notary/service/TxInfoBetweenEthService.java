package com.victor.notary.service;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-10-14  20：24
 * */

import com.victor.notary.model.TxInfoBetweenEth;

/*
* 以太坊跨以太坊的交易信息
* */
public interface TxInfoBetweenEthService {
    /**交易插入交易表
     * @param txInfo
     */
    public void addTx(TxInfoBetweenEth txInfo);
}
