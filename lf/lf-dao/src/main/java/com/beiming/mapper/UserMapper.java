package com.beiming.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.beiming.BaseMapper;
import com.beiming.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User>{

  List<User> getUser();
  
  Integer getUser1(String id,String username);
  
  Integer getUser2(@Param("id") String id,@Param("password") String password);
}
