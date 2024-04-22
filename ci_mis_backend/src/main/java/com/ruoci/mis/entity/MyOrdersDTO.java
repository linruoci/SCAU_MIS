package com.ruoci.mis.entity;

/**
 * @Author: ruoci
 **/

public class MyOrdersDTO {

    private Integer goodsId;
    private Integer nums;

    public MyOrdersDTO(Integer goodsId, Integer nums) {
        this.goodsId = goodsId;
        this.nums = nums;
    }

    public MyOrdersDTO() {
    }

    @Override
    public String toString() {
        return "MyOrdersDTO{" +
                "goodsId=" + goodsId +
                ", nums=" + nums +
                '}';
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getNums() {
        return nums;
    }

    public void setNums(Integer nums) {
        this.nums = nums;
    }
}
