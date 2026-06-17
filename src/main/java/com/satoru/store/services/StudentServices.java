package com.satoru.store.services;
import java.util.*;
import com.satoru.store.model.Student;
import com.satoru.store.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServices {


private final StudentRepository studentRepository;

@Autowired
public StudentServices(StudentRepository studentRepository){
    this.studentRepository = studentRepository;
}

public void addStudent(Student student){
    Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
    if(studentOptional.isPresent()){
        throw new IllegalStateException("Email already exist, Try with a new Email");
    }
    studentRepository.save(student);
}
public void deleteStudent(Integer id){
    studentRepository.deleteById(id);
}

public List<Student> getStudent(){
    return studentRepository.findAll();
}
public Student getStudentById(Integer id){
    return studentRepository.findStudentById(id).orElseThrow(()->new IllegalStateException("Student does not exist"));
}
public List<Student> getStudentByName(String name){
    return studentRepository.findStudentByFirstName(name);
}
public Student getStudentByEmail(String email){
    return studentRepository.findStudentByEmail(email).orElseThrow(() -> new IllegalStateException("Student Does not exist with"+ email+ " id"));
}
public void updateStudent(Integer id,Student updatedStudent){
    Student student = studentRepository.findById(id).orElseThrow(()->new IllegalStateException("Student does not exists"));

    student.setFirstName(updatedStudent.getFirstName());
    student.setLastName(updatedStudent.getLastName());
    student.setEmail(updatedStudent.getEmail());
    studentRepository.save(student);
}
}