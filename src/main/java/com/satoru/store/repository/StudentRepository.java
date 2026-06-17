package com.satoru.store.repository;
import com.satoru.store.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;


import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findStudentByEmail(String email);

    Optional<Student> findStudentById(Integer Id);

    List<Student> findStudentByFirstName(String firstName);
    List<Student> findStudentByAge(Integer age);
}
