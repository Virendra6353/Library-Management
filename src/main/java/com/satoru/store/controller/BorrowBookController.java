package com.satoru.store.controller;


import com.satoru.store.model.BorrowBook;
import com.satoru.store.services.BorrowBookService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/borrowBook")
public class BorrowBookController {

    private final BorrowBookService borrowBookService;

    public BorrowBookController(BorrowBookService borrowBookService) {
        this.borrowBookService = borrowBookService;
    }

    @GetMapping
    public List<BorrowBook> getBookRecord(){
        return borrowBookService.getRecord();
    }
    @GetMapping("/{id}")
    public BorrowBook getBorrowId(@PathVariable Integer id){
        return borrowBookService.getRecordId(id);
    }

    @GetMapping("/student/{studentId}")
    public List<BorrowBook> getStudentRecord(@PathVariable Integer studentId){
        return borrowBookService.getStudentRecord(studentId);
    }

    @GetMapping("/book/{bookId}")
    public List<BorrowBook> getBookRecord(@PathVariable Integer bookId){
        return borrowBookService.getBookRecord(bookId);
    }

    @PostMapping
    public void addRecord(@RequestParam Integer studentId,@RequestParam Integer bookId){
        borrowBookService.borrowBook(studentId,bookId);
    }
    @PutMapping("/returnbook/{id}")
    public void returnBook(@PathVariable Integer id){
        borrowBookService.returnBook(id);
    }
}
