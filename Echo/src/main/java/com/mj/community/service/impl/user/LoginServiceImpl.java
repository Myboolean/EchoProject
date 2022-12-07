package com.mj.community.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mj.community.mapper.UserMapper;
import com.mj.community.pojo.LoginTicket;
import com.mj.community.pojo.User;
import com.mj.community.service.user.LoginService;
import com.mj.community.utils.CommunityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.mj.community.utils.CommunityConstant.PASSWORD_MSG;
import static com.mj.community.utils.CommunityConstant.USERNAME_MSG;

/**
 * Created by IntelliJ IDEA.
 * User: pzj
 * Date: 2022/12/6
 * Time: 15:04
 * @author pzj
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    UserMapper userMapper;

    /**
     * 用戶登录(为用户创建凭证)
     * @param username 用户名
     * @param password 密码
     * @param expiredSeconds 多少秒之后凭证国企
     * @return Map<String, Object> 返回错误提示信息以及 ticket（凭证）
     */

    @Override
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();
        // 1.空值处理

        if(StringUtils.isBlank(username)) {
            map.put(USERNAME_MSG, "账号不能为空");
            return map;
        }

        if(StringUtils.isBlank(password)) {
            map.put(PASSWORD_MSG, "密码不能为空");
            return map;
        }
        //2.长度处理
        if (username.length() < 8) {
            map.put(USERNAME_MSG, "账号长度不能小于8");
            return map;
        }

        if(password.length() < 8) {
            map.put(PASSWORD_MSG, "密码的长度不能小于8");
            return map;
        }
        // 验证账号
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            map.put(USERNAME_MSG, "该账号不存在");
            return map;
        }

        // 验证状态

        if (user.getStatus() == 0) {
            map.put(USERNAME_MSG, "账号未激活");
            return map;
        }
        // 验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put(PASSWORD_MSG, "密码错误");
            return map;
        }

        // 用户名以及密码正确，生成凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID()); // 使用UUID生成随机凭证
        loginTicket.setStatus(0); // 设置凭证状态为有效， 登出的时候设置为无效
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000L)); // 设置凭证到期时间
        // 还需要存入redis

        map.put("ticket", loginTicket.getTicket());
        return map;
    }
}
