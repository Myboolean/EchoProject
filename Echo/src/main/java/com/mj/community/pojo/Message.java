package com.mj.community.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: pzj
 * Date: 2022/12/6
 * Time: 18:47
 * @author pzj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String conversationId;//会话ID
    private String content;
    private int status;
    private Date createTime;
}
