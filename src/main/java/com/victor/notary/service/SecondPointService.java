package com.victor.notary.service;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2020-02-04  10：37
 * */

import com.victor.notary.model.SecondPoint;

import java.util.List;

public interface SecondPointService {

    /**向secondpoint表插入数据
     * @param secondPoint
     * @return
     */
    public int insert(SecondPoint secondPoint);

    /**查询所有
     * @return
     */
    public List<SecondPoint> selecAll();

    /**查询所有公证人的总信誉值
     * @return
     */
    public double selectTotalMoney();

    /**查询利润小于平均利润的公证人
     * @param money
     * @return
     */
    public List<SecondPoint> selectLower(double money);

    /**查询利润大于平均利润的公证人
     * @param money
     * @return
     */
    public List<SecondPoint> selectHigher(double money);

    /**获取小于等于平均利润值的所有公证人的信誉值总和
     * @param money
     * @return
     */
    public double totalCreditOfLower(double money);

    /**获取大于平均利润值的所有公证人的信誉值总和
     * @param money
     * @return
     */
    public double totalCreditOfHigher(double money);

    /**获取公证人组的交易总量=成功+失败
     * @return
     */
    public int totalTrade();

    /**根据id更新
     * @param record
     * @return
     */
    public int updateByPrimaryKey(SecondPoint record);
}
