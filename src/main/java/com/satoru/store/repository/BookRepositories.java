package com.satoru.store.repository;

import com.satoru.store.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepositories extends JpaRepository<Book,Integer> {
    Optional<Book> findBookById(Integer id);
    Optional<Book> findBookByIsbn(String isbn);

    List<Book> findBookByBookName(String bookName);
    List<Book> findBookByAuthor(String author);
    List<Book> findBookByGenreId(Integer genreId);
}
