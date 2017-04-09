package com.skp.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.skp.dao.VerificationDao;
import com.skp.model.VerificationDO;

@Repository
public class VerficationDaoImpl extends BaseHibernateDaoImpl implements VerificationDao {

    public VerficationDaoImpl() {
        this.CLASS_NAME = VerificationDO.class.getName();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<VerificationDO> findByTel(String tel) {
        String sql = "select T from " + CLASS_NAME + " T where tel=? ";
        return (List<VerificationDO>) getHibernateTemplate().find(sql, tel);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public VerificationDO findOneByTel(String tel) {
        String sql = "select T from " + CLASS_NAME + " T where tel=? order by create_time desc  ";
        getHibernateTemplate().setMaxResults(1);
        return getHibernateTemplate().find(sql, tel) == null ? null : (VerificationDO) getHibernateTemplate().find(sql, tel).get(0);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public int countByTel(String tel) {
        String sql = "select count(*) from " + CLASS_NAME + " where tel=? where to_days(create_time) = to_days(now()) ";
        List<Long> clist = (List<Long>) getHibernateTemplate().find(sql, tel);
        return clist != null ? clist.get(0).intValue() : 0;
    }

}
