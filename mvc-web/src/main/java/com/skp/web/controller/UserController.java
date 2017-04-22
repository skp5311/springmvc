package com.skp.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.skp.dao.Mobile2UidMapper;
import com.skp.entity.Mobile2Uid;

@RestController
public class UserController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private Mobile2UidMapper    m2uidMapper;

    @RequestMapping("/muid")
    public JSONObject saveMobile2Uid(String tel, Integer uid) {
        JSONObject result = new JSONObject();
        Mobile2Uid m2Uid = new Mobile2Uid(tel, uid);
        int id = m2uidMapper.insert(m2Uid);
        result.put("success", true);
        result.put("m2uid", id);
        return result;
    }

}
