package com.victor.notary.mapper;

import com.victor.notary.model.SecondPoint;

import java.util.List;

public interface SecondPointMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SecondPoint record);

    int insertSelective(SecondPoint record);

    SecondPoint selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SecondPoint record);

    int updateByPrimaryKey(SecondPoint record);

    /**查询所有
     * @return
     */
    List<SecondPoint> selectAll();

    /**查询所有公证人的总信誉值
     * @return
     */
    double selectTotalMoney();

    /**查询利润小于平均利润的公证人
     * @param money
     * @return
     */
    List<SecondPoint> selectLower(double money);

    /**查询利润大于平均利润的公证人
     * @param money
     * @return
     */
    List<SecondPoint> selectHigher(double money);

    /**获取小于等于平均利润值的所有公证人的信誉值总和
     * @param money
     * @return
     */
    double totalCreditOfLower(double money);

    /**获取大于平均利润值的所有公证人的信誉值总和
     * @param money
     * @return
     */
    double totalCreditOfHigher(double money);

    /**获取公证人组的交易总量=成功+失败
     * @return
     */
    int totalTrade();
}