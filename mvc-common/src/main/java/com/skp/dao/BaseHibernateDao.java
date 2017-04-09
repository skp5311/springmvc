package com.skp.dao;

import java.util.List;

import org.springframework.orm.hibernate5.HibernateTemplate;

public interface BaseHibernateDao {

	public HibernateTemplate getHibernateTemplate();

	public Object save(Object o);

	public void delete(Object o);

	public void update(Object o);

	public Object findById(final String idValue);

	public List findAll();

	public void saveOrUpdate(Object o);

	public int excuteSQLUpdate(final String sql, final Object[] obj);

}
