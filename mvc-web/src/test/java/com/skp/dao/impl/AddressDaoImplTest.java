package com.skp.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.skp.dao.AddressDao;
import com.skp.model.TAddress;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "classpath:application.xml" })
public class AddressDaoImplTest {

    @Autowired
    private AddressDao addressDao;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSave() {
        TAddress addr = new TAddress(UUID.randomUUID().toString().replace("-", ""));
        addr.setUserId("123456");
        addr.setConsignee("河马5");
        addr.setMobile("18712345678");
        String id = (String) addressDao.save(addr);
        System.out.println(id);
    }

    @Test
    public void testFind() {
        TAddress address = (TAddress) addressDao.findById("baecbc64a3c546349ff601a6f53051ad");
        if (null != address) {
            System.out.println(address.toString());
            address.setCounty("中国");
            address.setProvince("北京");
            address.setCity("北京");
            addressDao.update(address);
            System.out.println(address.toString());
        } else {
            System.out.println("...................no address");
        }
    }

    @Test
    public void testFindByUserId() {
        List<TAddress> addrs = addressDao.findByUserId("123456");
        System.out.println("........" + addrs.size());
        for (TAddress addr : addrs) {
            System.out.println(addr.toString());
        }
    }

    @Test
    public void testCountByUserId() {
        Integer total = addressDao.countByUserId("123456");
        System.out.println("address total: " + total);
    }

}
