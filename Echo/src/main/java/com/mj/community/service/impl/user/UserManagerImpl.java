package com.mj.community.service.impl.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mj.community.mapper.UserMapper;
import com.mj.community.pojo.LoginTicket;
import com.mj.community.pojo.User;
import com.mj.community.service.user.UserManager;
import com.mj.community.utils.CommunityUtil;
import com.mj.community.utils.RedisKeyUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.mj.community.utils.CommunityConstant.*;

/**
 * Created by IntelliJ IDEA.
 * User: pzj
 * Date: 2022/12/6
 * Time: 19:26
 * @author pzj
 */
@Service
public class UserManagerImpl implements UserManager {


    // 使用StringRedisTemplate规范， 需要用ObjectMapper序列化和反序列化
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    private ObjectMapper mapper = new ObjectMapper();
    /**
     * 根据 ticket 查询 LoginTicket 信息
     *
     * @param ticket
     * @return
     */
    @SneakyThrows
    @Override
    public LoginTicket findLoginTicket(String ticket) {
        String ticketKey = RedisKeyUtil.getTicketKey(ticket);
        String tick = redisTemplate.opsForValue().get(ticketKey);
        return mapper.readValue(tick, LoginTicket.class);
    }
    /**
     * 修改用户头像
     *
     * @param userId
     * @param headerUrl
     * @return
     */
    @Override
    public int updateHeader(int userId, String headerUrl) {
        User user = userMapper.selectById(userId);
        user.setHeaderUrl(headerUrl);
        return userMapper.updateById(user);
    }
    /**
     * 修改用户密码（对新密码加盐加密存入数据库）
     *
     * @param userId
     * @param newPassword 新密码
     * @return
     */
    @Override
    public int updatePassword(int userId, String newPassword) {
        User user = userMapper.selectById(userId);
        // 重新加盐加密
        newPassword = CommunityUtil.md5(newPassword + user.getSalt());
        clearCache(userId);
        user.setPassword(newPassword);
        return userMapper.updateById(user);
    }
    /**
     * 优先从缓存中取值
     *
     * @param userId
     * @return
     */
    @SneakyThrows
    @Override
    public User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        String  userJson = redisTemplate.opsForValue().get(redisKey);
        return mapper.readValue(userJson, User.class);
    }
    /**
     * 缓存中没有该用户信息时，则将其存入缓存
     *
     * @param userId
     * @return
     */
    @SneakyThrows
    @Override
    public User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String userKey = RedisKeyUtil.getUserKey(userId);
        String userJson = mapper.writeValueAsString(user);
        redisTemplate.opsForValue().set(userKey, userJson, 3600, TimeUnit.SECONDS);
        return user;
    }
    /**
     * 用户信息变更时清除对应缓存数据
     *
     * @param userId
     */
    @Override
    public void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }
    /**
     * 获取某个用户的权限
     *
     * @param userId
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(int userId) {
        User user = this.findUserById(userId);
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                switch (user.getType()) {
                    case 1:
                        return AUTHORITY_ADMIN;
                    case 2:
                        return AUTHORITY_MODERATOR;
                    default:
                        return AUTHORITY_USER;
                }
            }
        });
        return list;
    }
    /**
     * 根据 Id 查询用户
     *
     * @param userId
     * @return
     */
    private User findUserById(int userId) {
        // return userMapper.selectById(id);
        User user = getCache(userId); // 优先从缓存中查询数据
        if (user == null) {
            user = initCache(userId);
        }
        return user;
    }
}
