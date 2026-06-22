package com.satoru.store.repository;

import com.satoru.store.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre,Integer> {

    Optional<Genre> findGenreByName(String name);
    Optional<Genre> findGenreById(Integer id);
}
