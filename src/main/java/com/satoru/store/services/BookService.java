package com.satoru.store.services;


import com.satoru.store.model.Book;
import com.satoru.store.model.Student;
import com.satoru.store.repository.BookRepositories;
import com.satoru.store.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
       private  final BookRepositories bookRepositories;

    public BookService(BookRepositories bookRepositories) {
        this.bookRepositories = bookRepositories;
    }
    public void addBook(Book book){
        Optional<Book> bookOptional = bookRepositories.findBookByIsbn(book.getIsbn());
        if(bookOptional.isPresent()){
            throw new IllegalStateException("Book with "+ book.getIsbn()+" already exists");
        }
        bookRepositories.save(book);
    }
    public void deleteBook(Integer id){
        bookRepositories.deleteById(id);
    }
    public void updateBook(Integer id, Book updateBook){
        Book book = bookRepositories.findBookById(id).orElseThrow(()-> new IllegalStateException("Book does not exists"));
        book.setBookName(updateBook.getBookName());
        book.setAuthor(updateBook.getAuthor());
        book.setIsbn(updateBook.getIsbn());
        bookRepositories.save(book);
    }
    public List<Book> getBook(){
        return bookRepositories.findAll();
    }
    public Book getBookById(Integer id) {
        return bookRepositories.findById(id).orElseThrow(() -> new IllegalStateException("Book doesn't exist"));
    }
    public List<Book> getBookByAuthor(String author){
        return bookRepositories.findBookByAuthor(author);
    }
    public List<Book> getBookByName(String bookName){
        return bookRepositories.findBookByBookName(bookName);
    }
    public Book getBookByIsbn(String isbn){
        return bookRepositories.findBookByIsbn(isbn).orElseThrow(()->new IllegalStateException("Book doesn't exist"));
    }
 }
