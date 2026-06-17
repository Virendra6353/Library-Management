package com.satoru.store.controller;

import java.util.*;
import com.satoru.store.model.Student;
import com.satoru.store.services.StudentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student")
public class StudentController {

    private final StudentServices studentServices;
    @Autowired
    public StudentController(StudentServices studentServices) {
        this.studentServices = studentServices;
    }
    @GetMapping
    public List<Student> getStudents(){
        return studentServices.getStudent();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Integer id){
        return studentServices.getStudentById(id);
    }

    @GetMapping("/email/{email}")
    public Student getStudentByEmail(@PathVariable String email){
        return studentServices.getStudentByEmail(email);
    }

    @GetMapping("/name/{firstName}")
    public List<Student> getStudentByName(@PathVariable String firstName){
        return studentServices.getStudentByName(firstName);
    }

    @PostMapping
    public void addStudent(@RequestBody Student student){
        studentServices.addStudent(student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Integer id){
        studentServices.deleteStudent(id);
    }

    @PutMapping("/{id}")
    public void updateStudent(@PathVariable Integer id,@RequestBody Student student){
        studentServices.updateStudent(id,student);
    }
}

