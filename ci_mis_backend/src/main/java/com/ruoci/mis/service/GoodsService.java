package com.ruoci.mis.service;

import cn.hutool.core.util.ObjectUtil;
import com.ruoci.mis.common.enums.OrderStatusEnum;
import com.ruoci.mis.common.enums.RoleEnum;
import com.ruoci.mis.entity.*;
import com.ruoci.mis.mapper.GoodsMapper;
import com.ruoci.mis.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品信息业务处理
 **/
@Service
public class GoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private CategoryService categoryService;

    @Resource
    private OrdersItemService ordersItemService;

    @Resource
    private OrdersService ordersService;

    /**
     * 新增
     */
    public void add(Goods goods) {
        //查询分类的数据
        Category category = categoryService.selectById(goods.getCategoryId());
        goodsMapper.insert(goods);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        // 校验权限
        goodsMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        // 校验权限
        for (Integer id : ids) {
            goodsMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Goods goods) {
        // 校验权限
        goodsMapper.updateById(goods);
    }

    /**
     * 根据ID查询
     */
    public Goods selectById(Integer id) {
        Goods goods = goodsMapper.selectById(id);
        wrapGoods(goods);
        return goods;
    }

    /**
     * 查询所有
     */
    public List<Goods> selectAll(Goods goods) {
        // 拿到当前的登录用户信息
        Account currentUser = TokenUtils.getCurrentUser();
        String role = currentUser.getRole();
        if (!RoleEnum.ADMIN.name().equals(role)) {  // 如果是商家的话   只能查询自己的分类
            throw new RuntimeException("无访问权限!");
        }
        List<Goods> goodsList = goodsMapper.selectAll(goods);
        for (Goods g : goodsList) {
            wrapGoods(g);
        }
        return goodsList;
    }

    /**
     * 分页查询
     */
    public PageInfo<Goods> selectPage(Goods goods, Integer pageNum, Integer pageSize) {
        // 拿到当前的登录用户信息
        Account currentUser = TokenUtils.getCurrentUser();
        String role = currentUser.getRole();
        if (!RoleEnum.ADMIN.name().equals(role)) {  // 如果是商家的话   只能查询自己的分类
            throw new RuntimeException("无访问权限!");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> list = goodsMapper.selectAll(goods);
        for (Goods g : list) {
            wrapGoods(g);
        }
        return PageInfo.of(list);
    }

    /**
     * 设置商品的额外信息
     */
    private Goods wrapGoods(Goods goods) {
        if (goods == null) {
            return null;
        }
        BigDecimal actualPrice = goods.getPrice().multiply(BigDecimal.valueOf(goods.getDiscount())).setScale(2, RoundingMode.UP);
        goods.setActualPrice(actualPrice);
        int saleCount = 0;
        List<OrdersItem> ordersItemList = ordersItemService.selectByGoodsId(goods.getId());
        List<OrdersItem> usageOrdersItemList = new ArrayList<>();
        for (OrdersItem ordersItem : ordersItemList) {
            // 筛选出有效订单的商品销售额
            Integer orderId = ordersItem.getOrderId();
            Orders orders = ordersService.selectById(orderId);
            if (orders == null) {
                continue;
            }
            if (OrderStatusEnum.NO_COMMENT.getValue().equals(orders.getStatus()) || OrderStatusEnum.DONE.getValue().equals(orders.getStatus())) {
                usageOrdersItemList.add(ordersItem);
            }
        }
        // 聚合函数查出订单的商品数量
        saleCount += usageOrdersItemList.stream().map(OrdersItem::getNum).reduce(Integer::sum).orElse(0);
        goods.setSaleCount(saleCount);
        return goods;
    }


    private GoodsDTO wrapGoods(GoodsDTO goods) {
        if (goods == null) {
            return null;
        }
        BigDecimal actualPrice = goods.getPrice().multiply(BigDecimal.valueOf(goods.getDiscount())).setScale(2, RoundingMode.UP);
        goods.setActualPrice(actualPrice);
        int saleCount = 0;
        List<OrdersItem> ordersItemList = ordersItemService.selectByGoodsId(goods.getId());
        List<OrdersItem> usageOrdersItemList = new ArrayList<>();
        for (OrdersItem ordersItem : ordersItemList) {
            // 筛选出有效订单的商品销售额
            Integer orderId = ordersItem.getOrderId();
            Orders orders = ordersService.selectById(orderId);
            if (orders == null) {
                continue;
            }
            if (OrderStatusEnum.NO_COMMENT.getValue().equals(orders.getStatus()) || OrderStatusEnum.DONE.getValue().equals(orders.getStatus())) {
                usageOrdersItemList.add(ordersItem);
            }
        }
        // 聚合函数查出订单的商品数量
        saleCount += usageOrdersItemList.stream().map(OrdersItem::getNum).reduce(Integer::sum).orElse(0);
        goods.setSaleCount(saleCount);
        return goods;
    }


    public PageInfo<GoodsDTO> selectAllExceptStatus(GoodsDTO goodsDTO,Integer pageNum, Integer pageSize) {
        // 拿到当前的登录用户信息;
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsDTO> list = goodsMapper.selectAllExceptStatus(goodsDTO);
        for (GoodsDTO g : list) {
            wrapGoods(g);
        }
        return PageInfo.of(list);
    }
}