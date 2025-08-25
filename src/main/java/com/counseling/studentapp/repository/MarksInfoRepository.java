package com.counseling.studentapp.repository;

import com.counseling.studentapp.model.MarksInfo;
import com.counseling.studentapp.model.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarksInfoRepository extends JpaRepository<MarksInfo, Long> {

    MarksInfo findByStudent(StudentInfo student);
}
