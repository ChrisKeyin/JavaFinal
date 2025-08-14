package com.chrisking.dao;

import com.chrisking.model.Membership;

import java.util.List;

public interface MembershipDAO {
    Membership create(Membership m);
    List<Membership> findByMemberId(int memberId);
    double totalRevenue();
    List<Membership> findAll();
}
