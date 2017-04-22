package com.skp.dao.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.skp.base.dao.SqlSessionMutilSourceDaoSupport;
import com.skp.dao.Mobile2UidMapper;
import com.skp.entity.Mobile2Uid;

@Repository
public class Mobile2UidMapperImpl implements Mobile2UidMapper {

    private static final Logger             logger    = LoggerFactory.getLogger(Mobile2UidMapperImpl.class);
    private String                          namespace = "com.skp.dao.Mobile2UidMapper";

    @Resource
    private SqlSessionMutilSourceDaoSupport daoSupport;

    @Override
    public int insert(Mobile2Uid m2Uid) {
        SqlSession session = daoSupport.getSqlSessionsIdBackup();
        int affectRows = session.insert(namespace + ".insert", m2Uid);
        logger.info("insert into users{}", JSON.toJSONString(m2Uid));
        return affectRows;
    }

    @Override
    public Mobile2Uid queryOneByMobile(String mobileNo) {
        Map<String, Object> uidMap = new HashMap<String, Object>();
        uidMap.put("mobileNo", mobileNo);
        SqlSession session = daoSupport.getSqlSessionsIdBackup();
        Mobile2Uid m2uid = (Mobile2Uid) session.selectOne(namespace + ".queryOneByMobile", uidMap);
        return m2uid;
    }

}
