package com.mj.community.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: pzj
 * Date: 2022/12/6
 * Time: 17:25
 * @author pzj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginTicket {
    private int id;
    private int userId;
    private String ticket; // 凭证
    private int status;
    private Date expired;
}
