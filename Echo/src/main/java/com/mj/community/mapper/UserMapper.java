package com.mj.community.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mj.community.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
