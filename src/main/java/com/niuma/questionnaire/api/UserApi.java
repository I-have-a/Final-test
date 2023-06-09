package com.niuma.questionnaire.api;

import com.niuma.questionnaire.common.Code;
import com.niuma.questionnaire.common.R;
import com.niuma.questionnaire.entity.Answer;
import com.niuma.questionnaire.entity.Test;
import com.niuma.questionnaire.entity.Title;
import com.niuma.questionnaire.entity.User;
import com.niuma.questionnaire.service.AnswerService;
import com.niuma.questionnaire.service.TestService;
import com.niuma.questionnaire.service.TitleService;
import com.niuma.questionnaire.service.UserService;
import com.niuma.questionnaire.utils.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserApi {
    @Autowired
    UserService userService;

    @Autowired
    TitleService titleService;

    @Autowired
    TestService testService;

    @Autowired
    AnswerService answerService;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("login")
    public R<String> login(String account, String password) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("account", account);
        map.put("password", password);
        return R.success(userService.login(map), Code.SUCCESS, "登陆成功");
    }

    @PostMapping("signup")
    public R<Boolean> signup(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        Integer signup = userService.signup(user);
        if (signup == null || signup == 0) return R.error(Code.ISNULL, "注册失败");
        return R.success(true, Code.SUCCESS, "注册成功");
    }

    @GetMapping("test")
    public R<List<Test>> test(HttpServletRequest request) {
        User user = Common.JwtUser(request);
        List<Test> tests = testService.getTest(user);
        if (tests == null) return R.error(Code.ISNULL, "没有");
        return R.success(tests, Code.SUCCESS, "完成");
    }

    @GetMapping("title")
    public R<List<Title>> title(HttpServletRequest request) {
        User user = Common.JwtUser(request);
        List<Title> titles = titleService.getTitle(user);
        if (titles == null) R.error(Code.ISNULL, "没有");
        return R.success(titles, Code.SUCCESS, "完成");
    }

    @GetMapping("answer")
    public R<List<Answer>> answer(Long TID, HttpServletRequest request) {
        User user = Common.JwtUser(request);
        HashMap<String, Object> map = new HashMap<>();
        map.put("UID", user.getId());
        map.put("TID", TID);
        List<Answer> answers = answerService.getAnswer(map);
        return R.error(Code.ISNULL, "开发中");
    }
}
