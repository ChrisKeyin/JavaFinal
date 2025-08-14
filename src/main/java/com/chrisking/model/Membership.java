package com.chrisking.model;

import java.time.OffsetDateTime;

public class Membership {
    private Integer membershipId;
    private String membershipType;
    private String membershipDescription;
    private double membershipCost;
    private int memberId;
    private OffsetDateTime purchasedAt;

    public Integer getMembershipId() { return membershipId; }
    public void setMembershipId(Integer membershipId) { this.membershipId = membershipId; }

    public String getMembershipType() { return membershipType; }
    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }

    public String getMembershipDescription() { return membershipDescription; }
    public void setMembershipDescription(String membershipDescription) { this.membershipDescription = membershipDescription; }

    public double getMembershipCost() { return membershipCost; }
    public void setMembershipCost(double membershipCost) { this.membershipCost = membershipCost; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public OffsetDateTime getPurchasedAt() { return purchasedAt; }
    public void setPurchasedAt(OffsetDateTime purchasedAt) { this.purchasedAt = purchasedAt; }

    @Override
    public String toString() {
        return "#" + membershipId + " | " + membershipType + " | $" + membershipCost + " | memberId=" + memberId + " | " + purchasedAt;
    }
}
