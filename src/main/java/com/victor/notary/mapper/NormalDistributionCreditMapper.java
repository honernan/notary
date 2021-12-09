package com.victor.notary.mapper;

import com.victor.notary.model.NormalDistributionCredit;
import java.util.List;

public interface NormalDistributionCreditMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(NormalDistributionCredit record);

    int insertSelective(NormalDistributionCredit record);

    NormalDistributionCredit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(NormalDistributionCredit record);

    int updateByPrimaryKey(NormalDistributionCredit record);
    /**根据公证人组表的id更新信誉值
     * @param id
     * @param endcredit
     * @return
     */
    int updateEndCreditById(int id, float endcredit);
    /**获取1链信誉值大于1链信誉平均值的几个账户
     * @return
     */
    List<NormalDistributionCredit> selectHighNotary();
    /**获取1链信誉值小于1链信誉平均值的几个账户
     * @return
     */
    List<NormalDistributionCredit> selectLowNotary();
    /**获取1链信誉值大于1链信誉平均值的几个账户
     * @return
     */
    List<NormalDistributionCredit> selectNotary();
    /**获取第一公证人
     * @return
     */
    List<NormalDistributionCredit> selectFirstNotary();

}