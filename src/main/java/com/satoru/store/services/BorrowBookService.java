package com.satoru.store.services;


import com.satoru.store.model.Book;
import com.satoru.store.model.BorrowBook;
import com.satoru.store.model.Student;
import com.satoru.store.repository.BookRepositories;
import com.satoru.store.repository.BorrowBookRepositories;
import com.satoru.store.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowBookService{
    private final BorrowBookRepositories borrowBookRepositories;
    private final StudentRepository studentRepository;
    private final BookRepositories bookRepositories;

    public BorrowBookService(BorrowBookRepositories borrowBookRepositories, StudentRepository studentRepository, BookRepositories bookRepositories) {
        this.borrowBookRepositories = borrowBookRepositories;
        this.studentRepository = studentRepository;
        this.bookRepositories = bookRepositories;
    }
    public void borrowBook(Integer studentId,Integer bookId){
        Student student = studentRepository.findStudentById(studentId).orElseThrow(()->new IllegalStateException("Student Does Not Exists"));
        Book book = bookRepositories.findBookById(bookId).orElseThrow(()->new IllegalStateException("Book Does Not exists"));

        Optional<BorrowBook> active = borrowBookRepositories.findByBookIdAndReturnDateIsNull(bookId);
        if(active.isPresent()){
            throw new IllegalStateException("The Book is not available at the time ");
        }
        List<BorrowBook> activeStudent = borrowBookRepositories.findByStudentIdAndReturnDateIsNull(studentId);

        if(!activeStudent.isEmpty()){
            throw new IllegalStateException("The student has already borrowed a book");
        }

        BorrowBook borrowBook1 = new BorrowBook();
        borrowBook1.setBook(book);
        borrowBook1.setStudent(student);
        borrowBook1.setBorrowDate(LocalDate.now());
        borrowBookRepositories.save(borrowBook1);
    }
    public void returnBook(Integer borrowBookId){
         BorrowBook borrowBook = borrowBookRepositories.findById(borrowBookId).orElseThrow(()->new IllegalStateException("Borrow Record does Not exists"));
        borrowBook.setReturnDate(LocalDate.now());
        borrowBookRepositories.save(borrowBook);
    }
    public List<BorrowBook> getRecord(){
        return borrowBookRepositories.findAll();
    }
    public BorrowBook getRecordId(Integer id){
        return borrowBookRepositories.findById(id).orElseThrow(()->new IllegalStateException("The record Does not exists"));
    }
    public List<BorrowBook> getStudentRecord(Integer studentId){
        return borrowBookRepositories.findByStudentId(studentId);
    }
    public List<BorrowBook> getBookRecord(Integer bookId){
        return borrowBookRepositories.findByBookId(bookId);
    }
 }
