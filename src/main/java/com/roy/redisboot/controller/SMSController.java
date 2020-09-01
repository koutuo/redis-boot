package com.roy.redisboot.controller;

import com.roy.redisboot.constants.Constants;
import com.roy.redisboot.util.MathUtils;
import com.roy.redisboot.util.RedisUtils;
import com.roy.redisboot.util.SMSUtils;
import com.roy.redisboot.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/sms",produces = "text/html;charset=utf-8")
public class SMSController {
    @Autowired
    private SMSUtils smsUtils;
    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping("sendSMS")
    public String sendSMS(String phone){
        //产生四位随机码
        String value = MathUtils.random();
        //产生redis的key，项目编码:业务编码:手机号码:0(登录注册专用)
        String key= StringUtil.formatKeyWithPrefix(
                Constants.RedisKey.PROJECT_PRIFIX,
                Constants.RedisKey.SMS_PRIFIX,
                phone,
                Constants.Sms.CodeType.LOGIN_OR_REGISTER+"");

        //将key和随机码存入redis，有效时间120秒
        redisUtils.putValue(key,value,120);
        //发送腾讯短信
        boolean flag = smsUtils.sendTencentSMS(phone,value,"2");
        if (flag){
            return "send_OK";
        }
        return "send_FAIL";

    }


}
