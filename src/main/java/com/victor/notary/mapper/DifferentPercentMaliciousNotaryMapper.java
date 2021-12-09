package com.victor.notary.mapper;

import com.victor.notary.model.DifferentPercentMaliciousNotary;

import java.util.List;

public interface DifferentPercentMaliciousNotaryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DifferentPercentMaliciousNotary record);

    int insertSelective(DifferentPercentMaliciousNotary record);

    DifferentPercentMaliciousNotary selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DifferentPercentMaliciousNotary record);

    int updateByPrimaryKey(DifferentPercentMaliciousNotary record);

    /**根据id更新five字段
     * @param record
     * @return
     */
    int updateFiveByPrimaryKey(DifferentPercentMaliciousNotary record);

    /**根据id更新ten字段
     * @param record
     * @return
     */
    int updateTenByPrimaryKey(DifferentPercentMaliciousNotary record);

    /**根据id更新thirty字段
     * @param record
     * @return
     */
    int updateThirtyByPrimaryKey(DifferentPercentMaliciousNotary record);

    /**根据id更新fourty字段
     * @param record
     * @return
     */
    int updateFourtyByPrimaryKey(DifferentPercentMaliciousNotary record);
    /**获取第一公证人
     * @return
     */
    List<DifferentPercentMaliciousNotary> selectFirstNotary();

    /**根据公证人组表的id更新信誉值
     * @param id
     * @param credit
     * @return
     */
    int updateCreditById(int id, float credit);
    /**获取1链信誉值大于1链信誉平均值的几个账户,且不是参加过交易的恶意节点
     * * @param num
     * @return
     */
    List<DifferentPercentMaliciousNotary> selectNotary(String num);

    /**统计five字段为1的记录数
     * @return
     */
    int countFiveNum();

    /**统计ten字段为1的记录数
     * @return
     */
    int countTenNum();

    /**统计thirty字段为1的记录数
     * @return
     */
    int countThirtyNum();

    /**统计fourty字段为1的记录数
     * @return
     */
    int countFourtyNum();

    /**获取1链信誉值大于1链信誉平均值的几个账户
     * @return
     */
    List<DifferentPercentMaliciousNotary> selectHighNotary();
    /**获取1链信誉值小于1链信誉平均值的几个账户
     * @return
     */
    List<DifferentPercentMaliciousNotary> selectLowNotary();
}