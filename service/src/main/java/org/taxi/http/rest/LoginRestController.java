package org.taxi.http.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.taxi.dto.LoginDto;


@Controller
@RequestMapping("/api/v1/login")
public class LoginRestController {

    @GetMapping
    public String loginPage(@ModelAttribute("user") LoginDto loginDto) {
        return "user/login";
    }

    @PostMapping
    public String login(Model model, @ModelAttribute("login") LoginDto loginDto) {
        return "user/login";
    }
}
