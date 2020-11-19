package com.dubbo.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dubbo.shop.entity.TUser;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sda1
 * @since 2020-11-18
 */
public interface TUserMapper extends BaseMapper<TUser> {

    TUser findUserByUserName(String userName);

}
