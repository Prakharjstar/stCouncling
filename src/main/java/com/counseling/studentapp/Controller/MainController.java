package com.counseling.studentapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.counseling.studentapp.repository.UserRepository;

@Controller
public class MainController {
    @Autowired
private BCryptPasswordEncoder passwordEncoder;
 
    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

   // @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal Object principal) {
        String name = "User";

        if (principal instanceof OAuth2User) {
            // Logged in with Google
            name = ((OAuth2User) principal).getAttribute("name");
        } else if (principal instanceof UserDetails) {
            // Logged in with DB
            name = ((UserDetails) principal).getUsername();
        }

        model.addAttribute("name", name);
        return "dashboard";
    }
    @PostMapping("/register")
public String registerUser(@ModelAttribute com.counseling.studentapp.model.User user , Model model) {
    user.setRole("ROLE_STUDENT");
      user.setPassword(passwordEncoder.encode(user.getPassword())); 
    userRepository.save(user);
    model.addAttribute("message", "User registered successfully!");
    return "signup";
}

@GetMapping("/Contactus")
public String Contactus(){
    return "Contactus.html";
}
 @GetMapping("/privacy")
    public String privacyPolicy() {
        return "PrivacyPolicy.html"; 
    }

     @GetMapping("/terms")
    public String termsPage() {
        return "terms"; 
    }

}
