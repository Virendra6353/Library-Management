package com.satoru.store.controller;


import com.satoru.store.model.Book;
import com.satoru.store.services.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    @GetMapping
    public List<Book> getBooks(){
        return bookService.getBook();
    }
    @GetMapping("/{id}")
    public Book getById(@PathVariable Integer id){
        return bookService.getBookById(id);
    }
    @GetMapping("/isbn/{isbn}")
    public Book getBookByIsbn(@PathVariable String isbn){
        return bookService.getBookByIsbn(isbn);
    }
    @GetMapping("/bookName/{bookName}")
    public List<Book> getBookByName(@PathVariable String bookName){
        return bookService.getBookByName(bookName);
    }

    @GetMapping("/author/{author}")
    public List<Book> getBookByAuthor(@PathVariable String author){
        return bookService.getBookByAuthor(author);
    }

    @PostMapping
    public void addBook(@RequestBody Book book){
        bookService.addBook(book);
    }
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Integer id){
        bookService.deleteBook(id);
    }
    @PutMapping("/{id}")
    public void updateBook(@PathVariable Integer id,@RequestBody Book book){
        bookService.updateBook(id,book);
    }

    @GetMapping("/genre/{id}")
    public List<Book> getBookByGenre(@PathVariable Integer id){
        return bookService.getBookByGenre(id);
    }

}
