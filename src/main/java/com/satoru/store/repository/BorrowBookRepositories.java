package com.satoru.store.repository;

import com.satoru.store.model.BorrowBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowBookRepositories extends JpaRepository<BorrowBook,Integer> {
    List<BorrowBook> findByStudentId(Integer studentId);
    List<BorrowBook> findByBookId(Integer bookId);
    List<BorrowBook> findByStudentIdAndReturnDateIsNull(Integer studentId);

    Optional<BorrowBook> findByBookIdAndReturnDateIsNull(Integer bookId);
}
