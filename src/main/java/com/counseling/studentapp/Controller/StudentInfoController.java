package com.counseling.studentapp.Controller;

import com.counseling.studentapp.model.StudentInfo;
import com.counseling.studentapp.repository.StudentInfoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StudentInfoController {

    @Autowired
    private StudentInfoRepository studentInfoRepo;

    @GetMapping("/student-info")
    public String showForm(Model model, @AuthenticationPrincipal Object principal) {
        String name = "", email = "";
        if (principal instanceof OAuth2User) {
            name = ((OAuth2User) principal).getAttribute("name");
            email = ((OAuth2User) principal).getAttribute("email");
        } else if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
            name = email; 
        }

        StudentInfo student = new StudentInfo();
        student.setEmail(email);
        student.setName(name);
        model.addAttribute("student", student);

        return "student_info";
    }

    @PostMapping("/submit-info")
public String saveStudentInfo(@ModelAttribute StudentInfo student) {
    try {
        double tenth = Double.parseDouble(student.getMarks10());
        double twelfth = Double.parseDouble(student.getMarks12());
        double score = (tenth * 0.4) + (twelfth * 0.6);  // example formula
        student.setTotalScore(score);
    } catch (NumberFormatException e) {
        student.setTotalScore(0.0);
    }

    studentInfoRepo.save(student);
    return "redirect:/student/dashboard";
}
}
