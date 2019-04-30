package com.beiming.service;

import java.util.List;
import com.beiming.dto.request.InsertUserRequestDTO;
import com.beiming.entity.User;

public interface UserService {
	
  /**
   * 获取用户列表
   * @return
   */
	List<User> getUserList();
	/**
	 * 获取用户列表
	 * @return
	 */
	List<User> getUserList1();
	
	/**
	 * 新增用户
	 * @param user
	 */
	void insertUser(InsertUserRequestDTO user);
	
	/**
	 * 删除用户
	 * @param userId
	 */
	void deleteUser(String userId);

}
