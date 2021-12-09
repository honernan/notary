package com.victor.notary.controller;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2019-09-27  11：38
 * */

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api/login")
@Api(value = "用户登录",tags = "用户登录")
public class LoginController {

    @RequestMapping(value = "/login")
    public String userLogin() {

        return "login";
    }
}
