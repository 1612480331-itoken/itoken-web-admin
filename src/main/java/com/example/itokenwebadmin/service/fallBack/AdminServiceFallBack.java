package com.example.itokenwebadmin.service.fallBack;

import com.example.itokenwebadmin.service.AdminService;
import com.example.itokenwebadmin.utils.ResultUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class AdminServiceFallBack implements AdminService {
    @Override
    public String login() {
        ObjectMapper objectMapper=new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(ResultUtil.error(502,"内部错误"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
