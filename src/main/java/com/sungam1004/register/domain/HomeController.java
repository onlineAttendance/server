package com.sungam1004.register.domain;

import com.sungam1004.register.global.manager.BuildTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String getHome(Model model) {
        model.addAttribute("time", BuildTime.buildTime);
        return "home";
    }

    @PostMapping("/")
    @ResponseBody
    public String postHome(@RequestBody PostHome request) {
        return "받은 데이터 = " + request.getData();
    }
}
