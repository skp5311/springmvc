package com.skp.dao;

import java.util.List;

import com.skp.model.VerificationDO;

public interface VerificationDao extends BaseHibernateDao {

    List<VerificationDO> findByTel(String tel);

    VerificationDO findOneByTel(String tel);

    public int countByTel(String tel);

}
