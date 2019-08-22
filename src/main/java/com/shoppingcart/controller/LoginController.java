package com.shoppingcart.controller;

import java.security.Principal;

import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.shoppingcart.logger.ILoggerUtil;

@Controller
public class LoginController {
    @Autowired
    ILoggerUtil loggerUtil;

    @GetMapping("/login")
    public String login(Principal principal) {
        loggerUtil.log(Level.INFO, "LoginController", "login", "start");
        if (principal != null) {
            return "redirect:/home";
        }
        loggerUtil.log(Level.INFO, "LoginController", "login", "success");
        return "/login";
    }

}
