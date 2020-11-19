package com.dubbo.shop.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author sda1
 * @since 2020-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("userName")
    private String username;

    private String password;

    @TableField("realName")
    private String realname;

    private String flag;

    private String status;

    private String address;

    private String tel;

    private String email;


}
