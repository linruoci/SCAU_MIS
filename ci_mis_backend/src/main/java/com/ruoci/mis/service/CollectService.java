package com.ruoci.mis.service;

import cn.hutool.core.date.DateUtil;
import com.ruoci.mis.common.enums.RoleEnum;
import com.ruoci.mis.entity.Account;
import com.ruoci.mis.entity.Business;
import com.ruoci.mis.entity.Collect;
import com.ruoci.mis.mapper.CollectMapper;
import com.ruoci.mis.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 业务处理
 **/
@Service
public class CollectService {

    @Resource
    private CollectMapper collectMapper;

    @Resource
    private BusinessService businessService;

    /**
     * 新增
     */
    public void add(Collect collect) {
        collectMapper.insert(collect);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        collectMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            collectMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Collect collect) {
        collectMapper.updateById(collect);
    }

    /**
     * 根据ID查询
     */
    public Collect selectById(Integer id) {
        return collectMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Collect> selectAll(Collect collect) {
        List<Collect> collects = collectMapper.selectAll(collect);
        for (Collect c : collects) {
            Business business = businessService.selectById(c.getBusinessId());
            c.setBusiness(business);
        }
        return collects;
    }

    /**
     * 分页查询
     */
    public PageInfo<Collect> selectPage(Collect collect, Integer pageNum, Integer pageSize) {
        // 拿到当前的登录用户信息
        Account currentUser = TokenUtils.getCurrentUser();
        PageHelper.startPage(pageNum, pageSize);
        List<Collect> list = collectMapper.selectAll(collect);
        return PageInfo.of(list);
    }

    public Collect selectByUserIdAndBusinessId(Integer userId, Integer businessId) {
        return collectMapper.selectByUserIdAndBusinessId(userId, businessId);
    }

    public void saveCollect(Collect collect) {
        Collect dbCollect = this.selectByUserIdAndBusinessId(collect.getUserId(), collect.getBusinessId());
        if (dbCollect != null) {  // 说明收藏过了
            this.deleteById(dbCollect.getId());  //删除收藏
        } else {
            // 新的收藏
            collect.setTime(DateUtil.now());
            this.add(collect);
        }
    }
}