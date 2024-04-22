package com.ruoci.mis.entity;

import java.math.BigDecimal;

/**
 * @Author: ruoci
 **/
public class OrdersVo {

    private String orderNo;
    private BigDecimal totalPrice;
    private String goodsName;


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
}
