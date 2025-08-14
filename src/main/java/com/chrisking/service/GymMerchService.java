package com.chrisking.service;

import com.chrisking.dao.GymMerchDAO;
import com.chrisking.dao.jdbc.GymMerchJdbcDAO;
import com.chrisking.model.GymMerch;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;
import com.chrisking.util.AppLog;

public class GymMerchService {
    private final GymMerchDAO dao = new GymMerchJdbcDAO();
    private static final Logger log = AppLog.get(GymMerchService.class);

    public GymMerch addItem(String name, String type, BigDecimal price, int qty) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name required");
        if (type == null || type.isBlank()) throw new IllegalArgumentException("Type required");
        if (price == null || price.signum() < 0) throw new IllegalArgumentException("Price must be >= 0");
        if (qty < 0) throw new IllegalArgumentException("Quantity must be >= 0");

        GymMerch m = new GymMerch();
        m.setMerchName(name);
        m.setMerchType(type);
        m.setMerchPrice(price);
        m.setQuantityInStock(qty);

        GymMerch saved = dao.create(m);
        log.info("Merch added: id=" + saved.getMerchId() +
                 ", name=" + name + ", type=" + type +
                 ", price=" + price + ", qty=" + qty);
        return saved;
    }

    public boolean setPrice(int merchId, BigDecimal newPrice) {
        if (newPrice == null || newPrice.signum() < 0) throw new IllegalArgumentException("Price must be >= 0");
        boolean ok = dao.updatePrice(merchId, newPrice);
        log.info("Merch price update: id=" + merchId + ", newPrice=" + newPrice + ", success=" + ok);
        return ok;
    }

    public boolean setQuantity(int merchId, int newQty) {
        if (newQty < 0) throw new IllegalArgumentException("Quantity must be >= 0");
        boolean ok = dao.updateQuantity(merchId, newQty);
        log.info("Merch qty update: id=" + merchId + ", newQty=" + newQty + ", success=" + ok);
        return ok;
    }

    public List<GymMerch> listAll() {
        return dao.findAll();
    }

    public BigDecimal totalStockValue() {
        return dao.totalStockValue();
    }
}
