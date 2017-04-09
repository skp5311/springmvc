package com.skp.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.skp.dao.AddressDao;
import com.skp.model.TAddress;

@Repository
public class AddressDaoImpl extends BaseHibernateDaoImpl implements AddressDao {

    public AddressDaoImpl() {
        this.CLASS_NAME = TAddress.class.getName();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<TAddress> findByUserId(String userId) {
        String sql = "select T from " + CLASS_NAME + " T where UserId=? and Status=?";
        return (List<TAddress>) getHibernateTemplate().find(sql, userId, 1);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public int countByUserId(String userId) {
        String sql = "select count(*) from " + CLASS_NAME + " where UserId=? ";
        List<Long> clist = (List<Long>) getHibernateTemplate().find(sql, userId);
        return clist != null ? clist.get(0).intValue() : 0;
    }

}
