package com.skp.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.skp.dao.BaseHibernateDao;

public abstract class BaseHibernateDaoImpl implements BaseHibernateDao {

    protected String          CLASS_NAME;

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public HibernateTemplate getHibernateTemplate() {
        return this.hibernateTemplate;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Object save(Object o) {
        return this.getHibernateTemplate().save(o);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(Object o) {
        this.getHibernateTemplate().delete(o);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(Object o) {
        this.getHibernateTemplate().update(o);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Object findById(final String idValue) {
        return this.getHibernateTemplate().get(CLASS_NAME, new String(idValue));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List findAll() {
        return this.getHibernateTemplate().find("select acc from " + CLASS_NAME + " as acc");
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void saveOrUpdate(Object o) {
        this.getHibernateTemplate().saveOrUpdate(o);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int excuteSQLUpdate(final String sql, final Object[] obj) {
        /*
         * try { SQLQuery q =
         * this.getHibernateTemplate().getSessionFactory().getCurrentSession().
         * createSQLQuery(sql);
         * 
         * if (obj != null && obj.length > 0) { for (int i = 0; i < obj.length;
         * i++) {//与JDBC不同的是，此处参数的索引是以0开始，而JDBC的PreparedStatement设置参数的索引是以1开始
         * q.setParameter(i, obj[i]); } } return q.executeUpdate(); } catch
         * (HibernateException e) {
         * 
         * e.printStackTrace(); return 0; }
         */

        return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
            public Integer doInHibernate(Session session) throws HibernateException {
                SQLQuery q = session.createSQLQuery(sql);
                if (obj != null && obj.length > 0) {
                    for (int i = 0; i < obj.length; i++) {// 与JDBC不同的是，此处参数的索引是以0开始，而JDBC的PreparedStatement设置参数的索引是以1开始
                        q.setParameter(i, obj[i]);
                    }
                }
                return q.executeUpdate();
            }
        });
    }

}
