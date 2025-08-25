package com.counseling.studentapp.Controller;

import com.counseling.studentapp.model.MarksInfo;
import com.counseling.studentapp.model.StudentInfo;
import com.counseling.studentapp.repository.MarksInfoRepository;
import com.counseling.studentapp.repository.StudentInfoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import java.security.Principal;

@Controller
public class DashBoardController {

    @Autowired
    private StudentInfoRepository studentInfoRepo;

    @Autowired
    private MarksInfoRepository marksInfoRepo;

@GetMapping("/student/dashboard")
public String showStudentDashboard(Model model, @AuthenticationPrincipal Object principal) {
    String name = "User";
    String email = null;

    if (principal instanceof OAuth2User) {
        name = ((OAuth2User) principal).getAttribute("name");
        email = ((OAuth2User) principal).getAttribute("email");
    } else if (principal instanceof UserDetails) {
        name = ((UserDetails) principal).getUsername();
        email = name; // assuming username is email
    }

    model.addAttribute("name", name);

    if (email != null) {
        StudentInfo student = studentInfoRepo.findByEmail(email);
        if (student != null) {
            model.addAttribute("allocatedBranch", student.getAllocatedBranch());
        } else {
            model.addAttribute("allocatedBranch", "Not Found");
        }
    } else {
        model.addAttribute("allocatedBranch", "Not Logged In");
    }

    return "dashboard"; // your dashboard.html file
}

}