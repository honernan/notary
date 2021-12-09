package com.victor.notary.mapper;

import com.victor.notary.model.SamebeginOfCredit;

import java.util.List;

public interface SamebeginOfCreditMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SamebeginOfCredit record);

    int insertSelective(SamebeginOfCredit record);

    SamebeginOfCredit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SamebeginOfCredit record);

    int updateByPrimaryKey(SamebeginOfCredit record);
    /**根据公证人组表的id更新信誉值
     * @param id
     * @param endcredit
     * @return
     */
    int updateEndCreditById(int id, float endcredit);
    /**获取1链信誉值大于1链信誉平均值的几个账户
     * @return
     */
    List<SamebeginOfCredit> selectNotary();
    /**获取第一公证人
     * @return
     */
    List<SamebeginOfCredit> selectFirstNotary();
}