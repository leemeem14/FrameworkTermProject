package com.assignment.controller;

import com.assignment.dto.UserDTO;
import com.assignment.entity.Role;
import com.assignment.entity.User;
import com.assignment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signup(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String fullName,
            @RequestParam String role,
            RedirectAttributes redirectAttributes) {

        try {
            if (!password.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다");
                return "redirect:/signup";
            }

            UserDTO userDTO = UserDTO.builder()
                    .username(username)
                    .email(email)
                    .password(password)
                    .fullName(fullName)
                    .role(Role.valueOf(role.toUpperCase()))
                    .build();

            userService.registerUser(userDTO);
            redirectAttributes.addFlashAttribute("success", "회원가입이 완료되었습니다. 로그인 하세요.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/signup";
        }
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}
