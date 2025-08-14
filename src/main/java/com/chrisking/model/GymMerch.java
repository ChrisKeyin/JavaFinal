package com.chrisking.model;

import java.math.BigDecimal;

public class GymMerch {
    private Integer merchId;
    private String merchName;
    private String merchType;
    private BigDecimal merchPrice;
    private int quantityInStock;

    public Integer getMerchId() { return merchId; }
    public void setMerchId(Integer merchId) { this.merchId = merchId; }

    public String getMerchName() { return merchName; }
    public void setMerchName(String merchName) { this.merchName = merchName; }

    public String getMerchType() { return merchType; }
    public void setMerchType(String merchType) { this.merchType = merchType; }

    public BigDecimal getMerchPrice() { return merchPrice; }
    public void setMerchPrice(BigDecimal merchPrice) { this.merchPrice = merchPrice; }

    public int getQuantityInStock() { return quantityInStock; }
    public void setQuantityInStock(int quantityInStock) { this.quantityInStock = quantityInStock; }

    @Override
    public String toString() {
        return "#" + merchId + " | " + merchName + " (" + merchType + ") | $" + merchPrice + " | qty=" + quantityInStock;
    }
}
