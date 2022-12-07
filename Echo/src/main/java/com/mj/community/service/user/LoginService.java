package com.mj.community.service.user;

import java.util.Map;

/**
 * @author pzj
 */
public interface LoginService {
    Map<String, Object> login(String username, String password, int expiredSeconds);
}
