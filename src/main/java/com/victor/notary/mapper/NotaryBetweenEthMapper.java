package com.victor.notary.mapper;

import com.victor.notary.model.NotaryBetweenEth;

import java.util.List;

public interface NotaryBetweenEthMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(NotaryBetweenEth record);

    int insertSelective(NotaryBetweenEth record);

    /** 根据user1的id获取公证人组对象
     * @param userAddress1
     * @return
     */
    NotaryBetweenEth selectByUser1(String userAddress1);

    /**根据id获取公证人组对象
     * @param id
     * @return
     */
    NotaryBetweenEth selectById(int id);

    /**获取1链信誉值大于1链信誉平均值的几个账户
     * @return
     */
    List<NotaryBetweenEth> selectNotary(int id);

    /**统计公证人组用户数
     * @return
     */
    int countNotary();

    List<NotaryBetweenEth> selectAll();

    int updateByPrimaryKeySelective(NotaryBetweenEth record);

    int updateByPrimaryKey(NotaryBetweenEth record);
    /**根据用户1地址更新用户1余额
     * @param userAddress1
     * @param money1
     * @return
     */
    int updateMoney1ByUserAddr1(String userAddress1, String money1);
    /**根据用户1地址更新用户1余额
     * @param userAddress2
     * @param money2
     * @return
     */
    int updateMoney2ByUserAddr2(String userAddress2, String money2);

    /**根据公证人组表的id更新信誉值
     * @param id
     * @param credit1
     * @param credit2
     * @return
     */
    int updateCreditById(int id, int credit1, int credit2);
}