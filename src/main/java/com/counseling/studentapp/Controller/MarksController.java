package com.counseling.studentapp.Controller;

import com.counseling.studentapp.model.MarksInfo;
import com.counseling.studentapp.model.StudentInfo;
import com.counseling.studentapp.repository.MarksInfoRepository;
import com.counseling.studentapp.repository.StudentInfoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@Controller
public class MarksController {

    @Autowired
    private StudentInfoRepository studentRepo;

    @Autowired
    private MarksInfoRepository marksRepo;

   
    @GetMapping("/marks-info")
    public String showMarksForm(Model model) {
        model.addAttribute("marks", new MarksInfo());
        model.addAttribute("students", studentRepo.findAll());
        return "marks-form"; 
    }

    @PostMapping("/marks-info")
public String submitMarks(@ModelAttribute MarksInfo marksInfo, Model model) {
    // Save the submitted marks info first
    MarksInfo savedMarks = marksRepo.save(marksInfo);

    // Now safely get the generated ID
    Long id = savedMarks.getId();

    // Redirect to student-specific recommendation page
    return "redirect:/student-recommendation/" + id;
}


    @GetMapping("/marks-dashboard")
    public String viewAllMarks(Model model) {
        model.addAttribute("marksList", marksRepo.findAll());
        return "marks-dashboard";
    }


   @GetMapping("/student-recommendation/{id}")
public String showStudentRecommendation(@PathVariable Long id, Model model) {
    Optional<MarksInfo> optionalMarks = marksRepo.findById(id);

    if (!optionalMarks.isPresent()) {
        model.addAttribute("message", "Marks not found for this student.");
        return "recommendation-result";
    }

    MarksInfo marks = optionalMarks.get();

    int tenth, twelfth, entrance;
    String category = marks.getCategory();
    String recommendation;

    try {
        tenth = Integer.parseInt(marks.getMarks10th());
        twelfth = Integer.parseInt(marks.getMarks12th());
        entrance = Integer.parseInt(marks.getEntranceScore());
    } catch (NumberFormatException e) {
        model.addAttribute("message", "Invalid marks format. Please enter valid numbers.");
        return "recommendation-result";
    }

    // Category-based JEE score recommendation
    if (category == null) category = "General";

    switch (category.toLowerCase()) {
        case "general":
            if (entrance >= 98) recommendation = "Eligible for Top IITs like Bombay, Delhi.";
            else if (entrance >= 92) recommendation = "Eligible for NITs like Trichy, Warangal.";
            else if (entrance >= 85) recommendation = "Consider IIITs or Tier-1 Private Colleges.";
            else if (entrance >= 70) recommendation = "Consider Private Engineering or BCA/B.Sc CS.";
            else recommendation = "Explore diploma, skill-based programs or alternative careers.";
            break;

        case "obc":
            if (entrance >= 95) recommendation = "Eligible for Top IITs (OBC Quota).";
            else if (entrance >= 85) recommendation = "Eligible for NITs (OBC Quota).";
            else if (entrance >= 75) recommendation = "Consider IIITs or Tier-1 Private Colleges.";
            else if (entrance >= 60) recommendation = "Consider Private Colleges or BCA/B.Sc.";
            else recommendation = "Explore diploma or vocational paths.";
            break;

        case "sc":
        case "st":
            if (entrance >= 90) recommendation = "Eligible for Top IITs (SC/ST Quota).";
            else if (entrance >= 75) recommendation = "Eligible for NITs (SC/ST Quota).";
            else if (entrance >= 65) recommendation = "Consider IIITs or Private Colleges.";
            else if (entrance >= 50) recommendation = "Explore State Colleges or BCA/B.Sc.";
            else recommendation = "Explore skill-based and diploma programs.";
            break;

        default:
            recommendation = "Category not recognized. Defaulting to General category logic.";
            break;
    }

    // Save recommendation
    marks.setRecommendation(recommendation);
    marksRepo.save(marks); // update record

    model.addAttribute("marks", marks);
    model.addAttribute("recommendation", recommendation);
    return "recommendation-result";
}
}