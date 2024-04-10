package com.ruoci.mis.mapper;

import com.ruoci.mis.entity.Tables;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作tables相关数据接口
 */
public interface TablesMapper {

    /**
     * 新增
     */
    int insert(Tables tables);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Tables tables);

    /**
     * 根据ID查询
     */
    Tables selectById(Integer id);

    /**
     * 查询所有
     */
    List<Tables> selectAll(Tables tables);

}