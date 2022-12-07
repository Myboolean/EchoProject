package com.mj.community.controller.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * User: pzj
 * Date: 2022/12/6
 * Time: 15:03
 * @author pzj
 */
@RestController
public class LoginController {

    @GetMapping("hello")
    public String hello(){
        return "Hello";
    }
}
