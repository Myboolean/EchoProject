package com.mj.community.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mj.community.mapper.UserMapper;
import com.mj.community.pojo.User;
import com.mj.community.service.user.RegisterService;
import com.mj.community.utils.CommunityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.mj.community.utils.CommunityConstant.*;

/**
 * Created by IntelliJ IDEA.
 * User: pzj
 * Date: 2022/12/6
 * Time: 16:58
 * @author pzj
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private JavaMailSender mailSender;
    /**
     * 用户注册
     * @param user 注册的用户信息
     * @return
     */
    @Override
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        // 传过来的user为空
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        // 1.判空值
        if (StringUtils.isBlank(user.getUsername())) {
            map.put(USERNAME_MSG, "账号不能为空");
            return map;
        }

        if (StringUtils.isBlank(user.getPassword())) {
            map.put(PASSWORD_MSG, "密码不能为空");
            return map;
        }

        //2.长度处理
        if (user.getUsername().length() < 8) {
            map.put(USERNAME_MSG, "账号长度不能小于8");
            return map;
        }

        if(user.getPassword().length() < 8) {
            map.put(PASSWORD_MSG, "密码的长度不能小于8");
            return map;
        }

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername());
        User u = userMapper.selectOne(wrapper);
        if (u != null) {
            map.put(USERNAME_MSG, "用户已存在");
            return map;
        }

        // 验证邮箱是否存在
        QueryWrapper<User> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("email", user.getEmail());
        User e = userMapper.selectOne(wrapper1);
        if (e != null) {
            map.put(EMAIL_MSG, "该邮箱已被注册");
            return map;
        }
        // 注册用户

        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));  // 随机生成salt
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt())); // 加盐加密
        user.setType(0); // 默认为普通用户
        user.setStatus(0); // 默认未激活
        user.setActivationCode(CommunityUtil.generateUUID()); // 随机生成激活码

        // 随机头像， 用户登陆之后可以自行修改
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        try {
            userMapper.insert(user);
        } catch (Exception ex) {
            map.put(ERROR_MSG, "服务器出现错误，请稍后重试");
            return map;
        }
        // 给用户发送激活邮件
        //创建邮件
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        //设置收件人
        mailMessage.setTo(user.getEmail());
        //设置发件人
        mailMessage.setFrom("1565905154@qq.com");
        //设置邮件标题
        mailMessage.setSubject("Echo activation code");
        //设置邮件内容
        mailMessage.setText(CommunityUtil.generateUUID().substring(0, 4).toUpperCase(Locale.ROOT));
        //发送邮件
        mailSender.send(mailMessage);
        map.put(SUCCESS_MSG, "注册成功");
        return map;
    }


}
