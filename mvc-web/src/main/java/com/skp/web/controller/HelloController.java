package com.skp.web.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.skp.dao.VerificationDao;
import com.skp.model.VerificationDO;

//@Controller
@RestController
public class HelloController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
    @Autowired
    VerificationDao             verficationDao;

    /**
     * 1. 使用RequestMapping注解来映射请求的URL
     * 2. 返回值会通过视图解析器解析为实际的物理视图, 对于InternalResourceViewResolver视图解析器，会做如下解析
     * 通过prefix+returnVal+suffix 这样的方式得到实际的物理视图，然后会转发操作
     * "/WEB-INF/views/success.jsp"
     * @return
     */
    @RequestMapping("/helloworld")
    public String hello() {
        System.out.println("hello world");
        return "success";
    }

    @RequestMapping("/helloworld2")
    public JSONObject helloto() {
        logger.info("sdfkasdfjl");
        System.out.println("hello world");
        JSONObject result = new JSONObject();
        result.put("skp", "aa");
        return result;
    }

    public static int getRandNum(int min, int max) {
        int randNum = min + (int) (Math.random() * ((max - min) + 1));
        return randNum;
    }

    @RequestMapping("/getVerficationCode")
    public JSONObject getVerification(String tel) {
        JSONObject result = new JSONObject();
        int count = verficationDao.countByTel(tel);
        if (count > 10) {
            result.put("success", false);
            result.put("msg", "超过短信发送数量");
            return result;
        }
        int num = getRandNum(1, 999999);
        result.put("success", true);
        result.put("verficationCode", num);
        result.put("msg", "有效期10分钟");
        VerificationDO verfication = new VerificationDO();
        verfication.setTel(tel);
        verfication.setVerification(num);
        verfication.setCreateTime(new Date());
        verficationDao.save(verfication);
        return result;
    }

    @RequestMapping("/checkVerficationCode")
    public JSONObject checkVerficationCode(String tel, Integer verficationCode) {
        JSONObject result = new JSONObject();
        VerificationDO verifiDO = verficationDao.findOneByTel(tel);
        if (null == verifiDO) {
            result.put("success", false);
            result.put("msg", "验证码不存在");
            return result;
        }

        if (verficationCode == verifiDO.getVerification()) {
            Date date = new Date();
            long m = 0;
            m = date.getTime() - verifiDO.getCreateTime().getTime();
            long minutes = (m % (1000 * 60 * 60)) / (1000 * 60);
            if (minutes > 10) {
                result.put("success", false);
                result.put("msg", "验证码失效");
            } else {

                result.put("success", true);
                result.put("msg", "通过验证");
            }
        } else {
            result.put("success", false);
            result.put("msg", "验证错误");
        }
        return result;

    }
}
