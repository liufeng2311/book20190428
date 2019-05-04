package com.beiming.service;

import com.beiming.entity.User;

/**
 * aop引入该接口
 * @author LiuFeng
 *
 */
public interface UserValidatorService {

  boolean validator(User user);
}
