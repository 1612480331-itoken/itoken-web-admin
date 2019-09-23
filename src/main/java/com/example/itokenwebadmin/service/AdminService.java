package com.example.itokenwebadmin.service;

import com.example.itokenwebadmin.service.fallBack.AdminServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "itoken-service-admin",fallback = AdminServiceFallBack.class)   //服务提供者名称和回调函数
public interface AdminService {

    //对应服务提供者controller中api接口
    @RequestMapping("/login")
    public String login();
}
