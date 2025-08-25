package com.counseling.studentapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.counseling.studentapp.model.MarksInfo;
import com.counseling.studentapp.model.StudentInfo;
import com.counseling.studentapp.repository.MarksInfoRepository;
import com.counseling.studentapp.repository.StudentInfoRepository;

import java.util.*;

@Controller
public class AdminController {

    @Autowired
    private StudentInfoRepository studentInfoRepository;

    @Autowired
    private MarksInfoRepository marksRepo;

    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(Model model) {
        List<StudentInfo> students = studentInfoRepository.findAll();
        students.sort((a, b) -> Double.compare(
                b.getTotalScore() != null ? b.getTotalScore() : 0,
                a.getTotalScore() != null ? a.getTotalScore() : 0));

        for (int i = 0; i < students.size(); i++) {
            students.get(i).setStudentRank(i + 1);
        }

        studentInfoRepository.saveAll(students);
        model.addAttribute("students", students);
        return "admin-dashboard";
    }

    @PostMapping("/admin/assign-branch/{id}")
    public String assignBranch(@PathVariable Long id, @RequestParam("allocatedBranch") String branch) {
        StudentInfo student = studentInfoRepository.findById(id).orElse(null);
        if (student != null) {
            student.setAllocatedBranch(branch.equals("None") ? null : branch);
            studentInfoRepository.save(student);
        }
        return "redirect:/admin/dashboard";  // or change to /admin/rankings if needed
    }

    @GetMapping("/admin/rankings")
    public String showRankings(Model model) {
        List<StudentInfo> students = studentInfoRepository.findAll();
        students.sort((a, b) -> Double.compare(
                b.getTotalScore() != null ? b.getTotalScore() : 0,
                a.getTotalScore() != null ? a.getTotalScore() : 0));

        for (int i = 0; i < students.size(); i++) {
            students.get(i).setStudentRank(i + 1);
        }

        studentInfoRepository.saveAll(students);
        model.addAttribute("students", students);
        return "admin-rankings";
    }

    @GetMapping("/admin/students")
    public String showAllStudents(Model model) {
        List<StudentInfo> students = studentInfoRepository.findAll();
        model.addAttribute("students", students);
        return "admin-students";
    }

    @GetMapping("/admin/show-marks")
    public String showMarks(Model model) {
        List<MarksInfo> marksList = marksRepo.findAll();
        model.addAttribute("marksList", marksList);
        return "admin-show-marks";
    }

    @GetMapping("/admin/all-recommendations")
    public String showAllRecommendations(Model model) {
        List<MarksInfo> marksList = marksRepo.findAll();

        List<Map<String, String>> recommendations = marksList.stream()
                .filter(m -> m != null && m.getStudent() != null)
                .map(m -> {
                    Map<String, String> data = new HashMap<>();
                    data.put("name", m.getStudent().getName());
                    data.put("marks10", m.getMarks10th());
                    data.put("marks12", m.getMarks12th());
                    data.put("entrance", m.getEntranceScore());
                    data.put("category", m.getCategory());

                    String recommendation;
                    try {
                        int entrance = Integer.parseInt(m.getEntranceScore());
                        String category = m.getCategory() != null ? m.getCategory().toLowerCase() : "general";

                        switch (category) {
                            case "general":
                                if (entrance >= 98)
                                    recommendation = "Eligible for Top IITs like Bombay, Delhi.";
                                else if (entrance >= 92)
                                    recommendation = "Eligible for NITs like Trichy, Warangal.";
                                else if (entrance >= 85)
                                    recommendation = "Consider IIITs or Tier-1 Private Colleges.";
                                else if (entrance >= 70)
                                    recommendation = "Consider Private Engineering or BCA/B.Sc CS.";
                                else
                                    recommendation = "Explore diploma, skill-based programs or alternative careers.";
                                break;

                            case "obc":
                                if (entrance >= 95)
                                    recommendation = "Eligible for Top IITs (OBC Quota).";
                                else if (entrance >= 85)
                                    recommendation = "Eligible for NITs (OBC Quota).";
                                else if (entrance >= 75)
                                    recommendation = "Consider IIITs or Tier-1 Private Colleges.";
                                else if (entrance >= 60)
                                    recommendation = "Consider Private Colleges or BCA/B.Sc.";
                                else
                                    recommendation = "Explore diploma or vocational paths.";
                                break;

                            case "sc":
                            case "st":
                                if (entrance >= 90)
                                    recommendation = "Eligible for Top IITs (SC/ST Quota).";
                                else if (entrance >= 75)
                                    recommendation = "Eligible for NITs (SC/ST Quota).";
                                else if (entrance >= 65)
                                    recommendation = "Consider IIITs or Private Colleges.";
                                else if (entrance >= 50)
                                    recommendation = "Explore State Colleges or BCA/B.Sc.";
                                else
                                    recommendation = "Explore skill-based and diploma programs.";
                                break;

                            default:
                                recommendation = "Category not recognized. Defaulting to General category logic.";
                                break;
                        }

                    } catch (NumberFormatException e) {
                        recommendation = "Invalid or missing marks.";
                    }

                    data.put("recommendation", recommendation);
                    return data;
                })
                .toList();

        model.addAttribute("recommendations", recommendations);
        return "admin-all-recommendations";
    }
}
