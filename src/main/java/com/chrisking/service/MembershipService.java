package com.chrisking.service;

import com.chrisking.dao.MembershipDAO;
import com.chrisking.dao.jdbc.MembershipJdbcDAO;
import com.chrisking.model.Membership;

import java.util.List;
import java.util.logging.Logger;
import com.chrisking.util.AppLog;

public class MembershipService {
    private final MembershipDAO dao = new MembershipJdbcDAO();
    private static final Logger log = AppLog.get(MembershipService.class);

    public Membership purchase(int memberId, String type, String desc, double cost) {
        if (type == null || type.isBlank()) throw new IllegalArgumentException("Type required");
        if (cost < 0) throw new IllegalArgumentException("Cost must be >= 0");

        Membership m = new Membership();
        m.setMemberId(memberId);
        m.setMembershipType(type);
        m.setMembershipDescription(desc);
        m.setMembershipCost(cost);

        Membership saved = dao.create(m);
        log.info("Membership purchased: memberId=" + memberId +
                 ", type=" + type + ", cost=" + cost +
                 ", id=" + saved.getMembershipId());
        return saved;
    }

    public List<Membership> getForMember(int memberId) {
        return dao.findByMemberId(memberId);
    }

    public double getTotalRevenue() {
        return dao.totalRevenue();
    }

    public List<Membership> getAll() {
        return dao.findAll();
    }
}
