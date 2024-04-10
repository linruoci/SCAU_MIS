package com.ruoci.mis.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoci.mis.common.enums.ResultCodeEnum;
import com.ruoci.mis.common.enums.RoleEnum;
import com.ruoci.mis.entity.Account;
import com.ruoci.mis.entity.Admin;
import com.ruoci.mis.entity.User;
import com.ruoci.mis.entity.Waiter;
import com.ruoci.mis.exception.CustomException;
import com.ruoci.mis.mapper.UserMapper;
import com.ruoci.mis.mapper.WaiterMapper;
import com.ruoci.mis.utils.TokenUtils;
import net.sf.jsqlparser.statement.select.Wait;
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
public class WaiterService {

    @Value("${server.port:9090}")
    private String port;

    @Value("${ip:localhost}")
    private String ip;

    @Resource
    private WaiterMapper waiterMapper;

    /**
     * 新增
     */
    public void add(User user) {
        Waiter dbWaiter = waiterMapper.selectByUsername(user.getUsername());
        if (ObjectUtil.isNotNull(dbWaiter)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(user.getName())) {
            user.setName(user.getUsername());
        }
        String http = "http://" + ip + ":" + port + "/files/";
        user.setAvatar(http + user.getAvatar());
        user.setRole(RoleEnum.WAITER.name());
        waiterMapper.insert(user);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        waiterMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            waiterMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(User user) {
        Waiter dbWaiter2 = waiterMapper.selectByUsername(user.getUsername());
        //  根据当前更新的用户的账号查询数据库  如果数据库存在跟当前更新用户一样账号的数据  那么当前的更新是不合法的  数据重复了
        if (ObjectUtil.isNotEmpty(dbWaiter2) && !Objects.equals(dbWaiter2.getId(), user.getId())) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        waiterMapper.updateById(dbWaiter2);
    }

    /**
     * 根据ID查询
     */
    public Waiter selectById(Integer id) {
        return waiterMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Waiter> selectAll(Waiter waiter) {
        return waiterMapper.selectAll(waiter);
    }

    /**
     * 分页查询
     */
    public PageInfo<Waiter> selectPage(Waiter waiter, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Waiter> list = waiterMapper.selectAll(waiter);
        return PageInfo.of(list);
    }

    /**
     * 用户登录
     */
    public Account login(Account account) {
        Account dbUser = this.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {   // 比较用户输入密码和数据库密码是否一致
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String tokenData = dbUser.getId() + "-" + RoleEnum.USER.name();
        String token = TokenUtils.createToken(tokenData, dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

    private Waiter selectByUsername(String username) {
        Waiter waiter = new Waiter();
        waiter.setUsername(username);
        List<Waiter> userList = this.selectAll(waiter);
        return CollUtil.isEmpty(userList) ? null : userList.get(0);
    }

    /**
     * 用户注册
     */
    public void register(Account account) {
        User user = new User();
        BeanUtils.copyProperties(account, user);  // 拷贝 账号和密码2个属性
        this.add(user);  // 添加账户信息
    }


    public void updatePassword(Account account) {
        Waiter waiter = waiterMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(waiter)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(waiter.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        waiter.setPassword(account.getNewPassword());
        waiterMapper.updateById(waiter);
    }


}