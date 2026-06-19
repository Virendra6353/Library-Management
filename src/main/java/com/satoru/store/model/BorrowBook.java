package com.satoru.store.model;


import jakarta.persistence.*;
import org.hibernate.annotations.AnyDiscriminatorImplicitValues;

import java.time.LocalDate;

@Entity
@Table
public class BorrowBook {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy =GenerationType.SEQUENCE)
    private Integer id;

    @Column(nullable = false)
    private LocalDate borrowDate;

    private LocalDate returnDate;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public BorrowBook(Integer id, LocalDate borrowDate, LocalDate returnDate, Student student, Book book) {
        this.id = id;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.student = student;
        this.book = book;
    }

    public BorrowBook() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return "BorrowBook{" +
                "id=" + id +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                ", student=" + student +
                ", book=" + book +
                '}';
    }
}