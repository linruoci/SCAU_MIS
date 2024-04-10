package com.ruoci.mis.service;

import cn.hutool.core.date.DateUtil;
import com.ruoci.mis.common.enums.OrderStatusEnum;
import com.ruoci.mis.common.enums.RoleEnum;
import com.ruoci.mis.entity.Account;
import com.ruoci.mis.entity.Comment;
import com.ruoci.mis.entity.Orders;
import com.ruoci.mis.mapper.CommentMapper;
import com.ruoci.mis.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 评价表业务处理
 **/
@Service
public class CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private OrdersService ordersService;

    /**
     * 新增
     */
    @Transactional
    public void add(Comment comment) {
        comment.setTime(DateUtil.now());
        Orders orders = ordersService.selectById(comment.getOrderId());
        if (orders != null) {
            comment.setBusinessId(orders.getBusinessId());
            orders.setStatus(OrderStatusEnum.DONE.getValue());  // 设置订单状态并更新
            ordersService.updateById(orders);
        }
        Account currentUser = TokenUtils.getCurrentUser();
        comment.setUserId(currentUser.getId());
        commentMapper.insert(comment);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        commentMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            commentMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Comment comment) {
        commentMapper.updateById(comment);
    }

    /**
     * 根据ID查询
     */
    public Comment selectById(Integer id) {
        return commentMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Comment> selectAll(Comment comment) {
        return commentMapper.selectAll(comment);
    }

    /**
     * 分页查询
     */
    public PageInfo<Comment> selectPage(Comment comment, Integer pageNum, Integer pageSize) {
        // 拿到当前的登录用户信息
        Account currentUser = TokenUtils.getCurrentUser();
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> list = commentMapper.selectAll(comment);
        return PageInfo.of(list);
    }

    public List<Comment> selectByBusinessId(Integer businessId) {
        return commentMapper.selectByBusinessId(businessId);
    }
}