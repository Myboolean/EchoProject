package com.mj.community.service.impl.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mj.community.pojo.LoginTicket;
import com.mj.community.service.user.LogoutService;
import com.mj.community.utils.RedisKeyUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: pzj
 * Date: 2022/12/6
 * Time: 19:13
 */
@Service
public class LogoutServiceImpl implements LogoutService {


    @Autowired
    private StringRedisTemplate redisTemplate;

    private ObjectMapper mapper = new ObjectMapper();
    /**
     * 用户退出（将凭证状态设为无效）
     *
     * @param ticket
     */
    @SneakyThrows
    @Override
    public void logout(String ticket) {
        // 修改（先删除再插入）对应用户在 redis 中的凭证状态
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        String tick = redisTemplate.opsForValue().get(redisKey);

        LoginTicket loginTicket = mapper.readValue(tick, LoginTicket.class);
        loginTicket.setStatus(1);
        String s = mapper.writeValueAsString(loginTicket);
        redisTemplate.opsForValue().set(redisKey, s);
    }
}
