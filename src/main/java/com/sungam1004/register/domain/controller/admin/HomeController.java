package com.sungam1004.register.domain.controller.admin;

import com.sungam1004.register.global.manager.BuildTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String getHome(Model model) {
        model.addAttribute("time", BuildTime.buildTime);
        return "home";
    }

}
