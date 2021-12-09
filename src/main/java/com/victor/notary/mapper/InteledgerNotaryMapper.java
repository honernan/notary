package com.victor.notary.mapper;

import com.victor.notary.model.InteledgerNotary;
import org.hibernate.validator.constraints.EAN;

import java.util.List;

public interface InteledgerNotaryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InteledgerNotary record);

    int insertSelective(InteledgerNotary record);

    InteledgerNotary selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InteledgerNotary record);

    int updateByPrimaryKey(InteledgerNotary record);


    /**根据交易参与者的fmax选择公证人,只有一个公证人，即只有一步跨链
     * @param oriFmax
     * @param tarFmax
     * @param oriUseraddress
     * @param tarUseraddress
     * @return
     */
    List<InteledgerNotary> selectByFmax(int oriFmax,int tarFmax, String oriUseraddress, String tarUseraddress);
}