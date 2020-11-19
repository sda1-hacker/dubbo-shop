package com.dubbo.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dubbo.shop.entity.TUser;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sda1
 * @since 2020-11-18
 */
public interface ITUserService extends IService<TUser> {

    TUser checkLogin(String userName, String password);
}
