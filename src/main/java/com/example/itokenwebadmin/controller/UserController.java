package com.example.itokenwebadmin.controller;

import com.example.itokenwebadmin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    @Autowired
    private AdminService adminService;

    @RequestMapping(value = {"/login"})
    public String index(){
        //String json = adminService.login();

        //System.out.println(json);

        return "index";
    }
}
