package com.mj.community.service.user;

import com.mj.community.pojo.LoginTicket;
import com.mj.community.pojo.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author pzj
 */
public interface UserManager {

    LoginTicket findLoginTicket(String ticket);
    int updateHeader(int userId, String headerUrl);

    int updatePassword(int userId, String newPassword);
    User getCache(int userId);
    User initCache(int userId);
    void clearCache(int userId);
    Collection<? extends GrantedAuthority> getAuthorities(int userId);
}
