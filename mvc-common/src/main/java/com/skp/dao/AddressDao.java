package com.skp.dao;

import java.util.List;

import com.skp.model.TAddress;

public interface AddressDao extends BaseHibernateDao {

    List<TAddress> findByUserId(String userId);

    int countByUserId(String userId);

}
