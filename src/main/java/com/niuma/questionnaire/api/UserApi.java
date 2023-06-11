package com.niuma.questionnaire.api;

import com.niuma.questionnaire.common.Code;
import com.niuma.questionnaire.common.R;
import com.niuma.questionnaire.entity.Answer;
import com.niuma.questionnaire.entity.Test;
import com.niuma.questionnaire.entity.Title;
import com.niuma.questionnaire.entity.User;
import com.niuma.questionnaire.service.*;
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
import java.util.stream.Collectors;

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

    @Autowired
    OptionService optionService;

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
        String userID = Common.JwtUser(request);
        List<Test> tests = testService.getTest(userID);
        if (tests == null) return R.error(Code.ISNULL, "没有");
        return R.success(tests, Code.SUCCESS, "完成");
    }

    @GetMapping("title")
    public R<List<Title>> title(HttpServletRequest request) {
        String userID = Common.JwtUser(request);
        HashMap<String,Object> map = new HashMap<>();
        map.put("UID",userID);
        List<Title> titles = titleService.getTitle(map);
        if (titles == null) return R.error(Code.ISNULL, "没有");
        titles.forEach(title -> {
            HashMap<String,Object> t = new HashMap<>();
            t.put("TiID",title.getId());
            title.setOptions(optionService.titleOption(t));
        });
        return R.success(titles, Code.SUCCESS, "完成");
    }

    @GetMapping("answer")
    public R<List<Title>> answer(Long TID, HttpServletRequest request) {
        String userID = Common.JwtUser(request);
        HashMap<String, Object> map = new HashMap<>();
        map.put("UID", Integer.parseInt(userID));
        map.put("TID", TID);
        List<Answer> answers = answerService.getAnswer(map);
        List<Title> titles = titleService.getTitle(map);
        titles.forEach(title -> {
            List<Answer> newA = answers.stream().filter(answer -> answer.getTID() == title.getId()).collect(Collectors.toList());
            title.setAnswers(newA);
        });
        return R.success(titles, Code.SERVER, "完成");
    }

    @GetMapping("reply")
    public R<Title> reply() {

        return R.error(Code.ISNULL, "开发中");
    }
}








