package com.satoru.store.model;

import jakarta.persistence.*;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "book_isbn", columnNames = "isbn")
        }
)
public class Book{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String bookName;


    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String isbn;


//    @Column(nullable = false)
    @ManyToOne
    @JoinColumn(name = "Genre_id",nullable = false)
    private Genre genre;


    public Book() {
    }

    public Book(Integer id, String bookName, String author, Genre genre,String isbn) {
        this.id = id;
        this.bookName = bookName;
        this.genre = genre;
        this.author = author;
        this.isbn = isbn;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}
