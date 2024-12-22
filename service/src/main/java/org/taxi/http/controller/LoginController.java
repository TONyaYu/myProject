package org.taxi.http.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.taxi.dto.LoginDto;
import org.taxi.entity.User;
import org.taxi.service.UserService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;
    @GetMapping
    public String loginPage(@ModelAttribute("user") LoginDto loginDto) {
        return "user/login";
    }

    @PostMapping
    public String login(@ModelAttribute("login") LoginDto loginDto, HttpSession session) {
        // Проверяем, существует ли пользователь с таким email и паролем
        Optional<User> user = userService.findByEmail(loginDto.getEmail());

        if (user.isPresent()) {
            // Сохраняем пользователя в сессии
            session.setAttribute("user", user.get());
            return "redirect:/users/" + user.get().getId(); // Перенаправляем на страницу пользователя
        } else {
            // Если аутентификация не удалась, возвращаем на страницу логина
            return "redirect:/login";
        }
    }
}
