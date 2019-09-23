package com.example.itokenwebadmin.service.consumer.hystrix;


import com.example.itokenwebadmin.utils.JsonUtil;
import com.example.itokenwebadmin.utils.ResultUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

public class FallBack {

    public static String badGateWay(){
        try {
            return JsonUtil.objectToString(ResultUtil.error(502,"内部错误"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
