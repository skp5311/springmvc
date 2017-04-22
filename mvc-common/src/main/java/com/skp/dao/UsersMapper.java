package com.skp.dao;

import com.skp.entity.Users;

public interface UsersMapper {
    public int insert(Users user);

    public int update(Users user);

    public int delete(int memberId);

    public int updatePwd(int memberId, String password);

    public Users queryOneByMemberId(int memberId);

}
