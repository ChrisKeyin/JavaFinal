package com.chrisking.dao;

import com.chrisking.model.GymMerch;
import java.math.BigDecimal;
import java.util.List;

public interface GymMerchDAO {
    GymMerch create(GymMerch m);
    boolean updatePrice(int merchId, BigDecimal newPrice);
    boolean updateQuantity(int merchId, int newQty);
    List<GymMerch> findAll();
    BigDecimal totalStockValue();
}
