package com.victor.notary.controller;/*
 * @description
 *
 * @author victor_Liu
 *
 *@create: 2020-05-21  16：28
 * */

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HelloCotroller {
    @RequestMapping(value = "/hello")
    public String hello(Model model) {
        Map<String, String> map = new HashMap();
        map.put("name", "victor");
        map.put("type", "thymeleaf");
        model.addAllAttributes(map);

        return "hello";
    }
}
