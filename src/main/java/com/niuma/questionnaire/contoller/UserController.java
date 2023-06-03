package com.niuma.questionnaire.contoller;

import com.niuma.questionnaire.common.Code;
import com.niuma.questionnaire.common.R;
import com.niuma.questionnaire.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("login")
    public R<String> login() {
        return R.error("在做", Code.ISNULL);
    }
}
