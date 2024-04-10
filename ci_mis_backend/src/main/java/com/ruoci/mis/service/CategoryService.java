package com.ruoci.mis.service;

import com.ruoci.mis.common.enums.RoleEnum;
import com.ruoci.mis.entity.Account;
import com.ruoci.mis.entity.Category;
import com.ruoci.mis.mapper.CategoryMapper;
import com.ruoci.mis.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品分类业务处理
 **/
@Service
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private BusinessService businessService;

    /**
     * 新增
     */
    public void add(Category category) {
        categoryMapper.insert(category);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        categoryMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            categoryMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Category category) {
        // 校验权限
        categoryMapper.updateById(category);
    }

    /**
     * 根据ID查询
     */
    public Category selectById(Integer id) {
        return categoryMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Category> selectAll(Category category) {
        // 拿到当前的登录用户信息
        return categoryMapper.selectAll(category);
    }

    /**
     * 分页查询
     */
    public PageInfo<Category> selectPage(Category category, Integer pageNum, Integer pageSize) {
        // 拿到当前的登录用户信息
        Account currentUser = TokenUtils.getCurrentUser();
        PageHelper.startPage(pageNum, pageSize);
        List<Category> list = categoryMapper.selectAll(category);
        return PageInfo.of(list);
    }

}