package com.mj.community.service.user;

import com.mj.community.pojo.User;

import java.util.Map;

/**
 * @author pzj
 */
public interface RegisterService {
    Map<String, Object> register(User user);
}
