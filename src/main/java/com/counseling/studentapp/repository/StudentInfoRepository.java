package com.counseling.studentapp.repository;

import com.counseling.studentapp.model.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentInfoRepository extends JpaRepository<StudentInfo, Long> {
    StudentInfo findByEmail(String email); 
}
