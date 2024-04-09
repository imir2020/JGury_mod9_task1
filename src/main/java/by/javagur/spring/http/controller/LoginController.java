package by.javagur.spring.http.controller;

import by.javagur.spring.dto.LoginDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {


    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }

//    @PreAuthorize("hasAnyAuthority('ADMIN','USER', 'OPERATOR')")
//    @PostMapping("/login")
//    public String login(Model model, @ModelAttribute("login") LoginDto loginDto) {
//        return "redirect:/login";
//    }
}
