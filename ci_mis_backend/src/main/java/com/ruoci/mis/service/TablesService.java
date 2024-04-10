package com.ruoci.mis.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoci.mis.common.enums.ResultCodeEnum;
import com.ruoci.mis.common.enums.RoleEnum;
import com.ruoci.mis.entity.Account;
import com.ruoci.mis.entity.Tables;
import com.ruoci.mis.exception.CustomException;
import com.ruoci.mis.mapper.TablesMapper;
import com.ruoci.mis.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 用户表业务处理
 **/
@Service
public class TablesService {

    @Value("${server.port:9090}")
    private String port;

    @Value("${ip:localhost}")
    private String ip;

    @Resource
    private TablesMapper tablesMapper;

    /**
     * 新增
     */
    public void add(Tables tables) {
        tablesMapper.insert(tables);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        tablesMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            tablesMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Tables tables) {
        Tables dbTables2 = tablesMapper.selectById(tables.getId());
        //  根据当前更新的用户的账号查询数据库  如果数据库存在跟当前更新用户一样账号的数据  那么当前的更新是不合法的  数据重复了
        if (ObjectUtil.isNotEmpty(dbTables2) && !Objects.equals(dbTables2.getId(), tables.getId())) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        tablesMapper.updateById(tables);
    }

    /**
     * 根据ID查询
     */
    public Tables selectById(Integer id) {
        return tablesMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Tables> selectAll(Tables tables) {
        return tablesMapper.selectAll(tables);
    }

    /**
     * 分页查询
     */
    public PageInfo<Tables> selectPage(Tables tables, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Tables> list = tablesMapper.selectAll(tables);
        return PageInfo.of(list);
    }




}