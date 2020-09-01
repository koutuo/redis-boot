package com.roy.redisboot.controller;

import com.roy.redisboot.constants.Constants;
import com.roy.redisboot.util.MathUtils;
import com.roy.redisboot.util.RedisUtils;
import com.roy.redisboot.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/test",produces = "text/html;charset=utf-8")
public class TestController {
    @Autowired
    private RedisUtils redisUtils;

    @PostMapping("add")
    public String add(String k,String v){
        redisUtils.putValue(k,v,60);
        return "add_ok";
    }
    @GetMapping("get")
    public String get(String k){
       String v= redisUtils.getValue(k);
       if(v!=null){
           return "getOK"+v;
       }else {
           return "ERROR";
       }

    }
    @PostMapping("getLoginCode")
    public String getLoginCode(String phone){
        //产生随机码四位
        String value = MathUtils.random();
        //产生redis的key,项目编码：业务编码：手机号码：0（登录注册专用）
        String key = StringUtil.formatKeyWithPrefix(
                Constants.RedisKey.PROJECT_PRIFIX,
                Constants.RedisKey.SMS_PRIFIX,
                phone,
                Constants.Sms.CodeType.LOGIN_OR_REGISTER+""
        );
        //将key和随机代码存入redis,有效时长120
        redisUtils.putValue(key,value,120);


        return "sendLoginCodeOK";
    }


}
