package com.dubbo.shop.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dubbo.shop.entity.TUser;
import com.dubbo.shop.mapper.TUserMapper;
import com.dubbo.shop.service.ITUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sda1
 * @since 2020-11-18
 */
@Component
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements ITUserService {

    @Resource
    private TUserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public TUser checkLogin(String userName, String password) {

        TUser currUser = userMapper.findUserByUserName(userName);

        if(currUser != null){
            if(passwordEncoder.matches(password, currUser.getPassword())){
                return currUser;
            }
        }
        return null;
    }
}
