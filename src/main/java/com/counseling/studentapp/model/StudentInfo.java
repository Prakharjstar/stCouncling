package com.counseling.studentapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_info", schema = "student") // specify PostgreSQL schema
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String dob;
    private String marks10;
    private String marks12;
    private String entranceMarks;

    private String branch;
    private String address;

    private Double totalScore;

    @Column(name = "student_rank")
    private Integer studentRank;

    private String branchPreference1;
    private String branchPreference2;

    private String allocatedBranch;
}
