package com.mj.community.service.impl.user;

import com.mj.community.mapper.UserMapper;
import com.mj.community.pojo.User;
import com.mj.community.service.user.ActivationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mj.community.utils.CommunityConstant.*;

/**
 * Created by IntelliJ IDEA.
 * User: pzj
 * Date: 2022/12/6
 * Time: 19:03
 * @author pzj
 */
@Service
public class ActivationServiceImpl implements ActivationService {

    @Autowired
    private UserMapper userMapper;
    /**
     * 激活用户
     *
     * @param userId 用户 id
     * @param code   激活码
     * @return
     */
    @Override
    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            // 用户已经激活
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) { //TODO 这里状态激活码需要对应
            // 修改用户状态为激活态
            user.setStatus(1); // 已激活状态
            userMapper.updateById(user);

            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }
}
