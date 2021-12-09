package com.victor.notary.service.impl;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2020-02-04  10：38
 * */

import com.victor.notary.mapper.SecondPointMapper;
import com.victor.notary.model.SecondPoint;
import com.victor.notary.service.SecondPointService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service(value = "secondPointService")
public class SecondPointServiceImpl implements SecondPointService {

    @Resource
    private SecondPointMapper secondPointMapper;

    @Override
    public int insert(SecondPoint secondPoint) {
        int result = secondPointMapper.insert(secondPoint);
        return result;
    }

    @Override
    public List<SecondPoint> selecAll() {
        List<SecondPoint> result = secondPointMapper.selectAll();
        return result;
    }

    @Override
    public double selectTotalMoney() {
        double result = secondPointMapper.selectTotalMoney();
        return result;
    }

    @Override
    public List<SecondPoint> selectLower(double money) {
        List<SecondPoint> resultList = secondPointMapper.selectLower(money);
        return resultList;
    }
    @Override
    public List<SecondPoint> selectHigher(double money) {
        return secondPointMapper.selectHigher(money);
    }

    @Override
    public double totalCreditOfLower(double money) {
        double result = secondPointMapper.totalCreditOfLower(money);
        return result;
    }

    @Override
    public double totalCreditOfHigher(double money) {
        return secondPointMapper.totalCreditOfHigher(money);

    }

    @Override
    public int totalTrade(){
        return secondPointMapper.totalTrade();
    }

    @Override
    public int updateByPrimaryKey(SecondPoint record) {
        return secondPointMapper.updateByPrimaryKey(record);
    }

}
