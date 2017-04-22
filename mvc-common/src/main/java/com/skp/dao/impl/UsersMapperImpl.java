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
import com.skp.dao.UsersMapper;
import com.skp.entity.Users;

@Repository
public class UsersMapperImpl implements UsersMapper {

    private static final Logger             logger    = LoggerFactory.getLogger(UsersMapperImpl.class);
    private String                          namespace = "com.skp.dao.UsersMapper";

    @Resource
    private SqlSessionMutilSourceDaoSupport daoSupport;

    @Override
    public int insert(Users user) {
        Integer uid = user.getUid();
        user.setTableIndex(daoSupport.getTableIndex(uid));
        SqlSession session = daoSupport.getSqlSession(uid);
        int affectRows = session.insert(namespace + ".insert", user);
        logger.info("insert into users{}", JSON.toJSONString(user));
        return affectRows;
    }

    @Override
    public int update(Users user) {
        Integer uid = user.getUid();
        user.setTableIndex(daoSupport.getTableIndex(uid));
        SqlSession session = daoSupport.getSqlSession(uid);
        int affectRows = session.update(namespace + ".update", user);
        logger.info("update users{}", JSON.toJSONString(user));
        return affectRows;
    }

    @Override
    public Users queryOneByMemberId(int memberId) {
        int tableIndex = daoSupport.getTableIndex(memberId);
        Map<String, Object> uidMap = new HashMap<String, Object>();
        uidMap.put("tableIndex", tableIndex);
        uidMap.put("uid", memberId);
        SqlSession session = daoSupport.getSqlSession(memberId);
        Users user = (Users) session.selectOne(namespace + ".queryOneByUid", uidMap);
        return user;
    }

    @Override
    public int delete(int uid) {
        int tableIndex = daoSupport.getTableIndex(uid);
        Map<String, Object> uidMap = new HashMap<String, Object>();
        uidMap.put("tableIndex", tableIndex);
        uidMap.put("uid", uid);
        SqlSession session = daoSupport.getSqlSession(uid);
        int affectRows = session.update(namespace + ".delete", uidMap);
        logger.info("delete user:{}", JSON.toJSONString(uidMap));
        return affectRows;
    }

    @Override
    public int updatePwd(int uid, String password) {
        int tableIndex = daoSupport.getTableIndex(uid);
        Map<String, Object> pwdMap = new HashMap<String, Object>();
        pwdMap.put("tableIndex", tableIndex);
        pwdMap.put("uid", uid);
        pwdMap.put("password", password);
        SqlSession session = daoSupport.getSqlSession(uid);
        int affectRows = session.update(namespace + ".updatePwd", pwdMap);
        logger.info("update password:{}", JSON.toJSONString(pwdMap));
        return affectRows;
    }

}
