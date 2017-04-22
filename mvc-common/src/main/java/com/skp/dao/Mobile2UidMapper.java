package com.skp.dao;

import com.skp.entity.Mobile2Uid;

public interface Mobile2UidMapper {
    public int insert(Mobile2Uid m2Uid);

    public Mobile2Uid queryOneByMobile(String mobileNo);

}
