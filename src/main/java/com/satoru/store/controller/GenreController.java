package com.satoru.store.controller;


import com.satoru.store.model.Genre;
import com.satoru.store.services.GenreService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/genre")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> getAll(){
        return genreService.getAll();
    }

    @GetMapping("/{id}")
    public Genre getById(@PathVariable Integer id){
        return genreService.getGenreById(id);
    }
    @GetMapping("/name/{name}")
    public Genre getByName(@PathVariable String name){
        return genreService.getGenreByName(name);
    }

    @PostMapping
    public void addGenre(@RequestBody Genre genre){
        genreService.addGenre(genre);
    }
    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable Integer id){
        genreService.DeleteGenre(id);
    }

    @PutMapping("/{id}")
    public void updateGenre(@PathVariable Integer id,@RequestBody Genre genre){
        genreService.update(id,genre);
    }
}
