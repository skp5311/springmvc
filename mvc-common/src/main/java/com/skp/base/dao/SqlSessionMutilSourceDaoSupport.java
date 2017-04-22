package com.skp.base.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.dao.support.DaoSupport;

import com.skp.util.DBHash;

/**
 * @author Jin Song
 * 分表规则
 * 端口0：0~127
 * 端口1：128~255
 * 端口2：256~383
 * 端口3：384~511
 * ...
 */
public class SqlSessionMutilSourceDaoSupport extends DaoSupport {

    /**
     * 注入的session factory，关联用户中心业务的data source
     */
    private List<SqlSessionFactory> sessionFactoryList;

    /**
     * 注入的session factory，关联uid备份 的data source
     */
    private SqlSessionFactory       sqlSessionFactoryIdBackup;

    /**
     * 库个数
     */
    private int                     dbCount;

    /**
     * 表个数
     */
    private int                     tableCount = 1;

    /**
     * 用于用户中心业务的sql session
     */
    private SqlSession[]            sqlSessions;

    public SqlSession[] getSqlSessionList() {
        return this.sqlSessions;
    }

    public SqlSession getSqlSession(String key) {
        return this.sqlSessions[getDbIndex(key)];
    }

    public SqlSession getSqlSession(long key) {
        return this.sqlSessions[getDbIndex(key)];
    }

    public SqlSession getSqlSessionByIndex(Integer index) {
        return this.sqlSessions[index];
    }

    /**
     * 根据key(String类型)获取数据库的编号
     * @param key 用于分库的key
     * @return 数据库的编号
     */
    public int getDbIndex(String key) {
        return getTableIndex(key) / tableCount;
    }

    /**
     * 根据key(long 类型)获取数据库的编号
     * @param key 用于分库的key
     * @return 数据库的编号
     */
    public int getDbIndex(long key) {
        return getTableIndex(key) / tableCount;
    }

    /**
     * 根据key(String类型)获取表的编号
     * @param key 用于分表的key
     * @return 表的编号
     */
    public int getTableIndex(String key) {
        return DBHash.getHash((key + "").getBytes(), dbCount, tableCount);
    }

    /**
     * 根据key(long 类型)获取表的编号
     * @param key 用于分表的key
     * @return 表的编号
     */
    public int getTableIndex(long key) {
        return DBHash.getHash(Long.toString(key).getBytes(), dbCount, tableCount);
    }

    /**
     * @return the sessionFactoryList
     */
    public List<SqlSessionFactory> getSessionFactoryList() {
        return sessionFactoryList;
    }

    /**
     * @param sessionFactoryList the sessionFactoryList to set
     */
    public void setSessionFactoryList(List<SqlSessionFactory> sessionFactoryList) {
        this.sessionFactoryList = sessionFactoryList;

        sqlSessions = new SqlSession[sessionFactoryList.size()];
        for (int i = 0; i < sqlSessions.length; i++)
            sqlSessions[i] = new SqlSessionTemplate(sessionFactoryList.get(i));

        dbCount = sqlSessions.length;
    }

    /**
     * 用于备份uid 的sql session
     */
    private SqlSession sqlSessionsIdBackup;

    public SqlSessionFactory getSqlSessionFactoryIdBackup() {
        return sqlSessionFactoryIdBackup;
    }

    public void setSqlSessionFactoryIdBackup(SqlSessionFactory sqlSessionFactoryIdBackup) {
        this.sqlSessionFactoryIdBackup = sqlSessionFactoryIdBackup;
        sqlSessionsIdBackup = new SqlSessionTemplate(sqlSessionFactoryIdBackup);
    }

    public SqlSession getSqlSessionsIdBackup() {
        return sqlSessionsIdBackup;
    }

    public void setSqlSessionsIdBackup(SqlSession sqlSessionsIdBackup) {
        this.sqlSessionsIdBackup = sqlSessionsIdBackup;
    }

    @Override
    protected void checkDaoConfig() throws IllegalArgumentException {

    }

    public int getTableCount() {
        return tableCount;
    }

    public void setTableCount(int tableCount) {
        this.tableCount = tableCount;
    }

    public int getDbCount() {
        return dbCount;
    }

    public void setDbCount(int dbCount) {
        this.dbCount = dbCount;
    }
}
