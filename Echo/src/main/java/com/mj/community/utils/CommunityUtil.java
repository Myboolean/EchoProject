package com.mj.community.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: pzj
 * Date: 2022/12/6
 * Time: 17:16
 * @author pzj
 */
public class CommunityUtil {
    /**
     * 生成随机字符串
     * @return 返回diy的字符串
     */

    public static String generateUUID(){
        // 去除随机生成字符串中的“-”
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 加密
     * @param key 要进行加密的字符串
     * @return
     */

    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}
