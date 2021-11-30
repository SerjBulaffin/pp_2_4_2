package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import web.model.User;
import web.service.RoleServiceImpl;
import web.service.UserDetailsServiceImpl;

import java.security.Principal;

@Controller
public class UserController {

    private UserDetailsServiceImpl userDetailsService;
    private RoleServiceImpl roleService;

    @Autowired
    public UserController(UserDetailsServiceImpl userDetailsService, RoleServiceImpl roleService) {
        this.userDetailsService = userDetailsService;
        this.roleService = roleService;
    }

    @GetMapping("/user")
    public String userPage(Model model, Principal principal) {
        User user = userDetailsService.getUserByName(principal.getName());
        model.addAttribute("user", user);

        return "user_user";
    }
}
