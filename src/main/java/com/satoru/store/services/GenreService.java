package com.satoru.store.services;


import com.satoru.store.model.Book;
import com.satoru.store.model.Genre;
import com.satoru.store.repository.GenreRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    private final GenreRepository genreRepository;


    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> getAll(){
        return genreRepository.findAll();
    }
    public Genre getGenreById(Integer id){
        return genreRepository.findGenreById(id).orElseThrow(()->new IllegalStateException("Genre Did Not exists"));
    }
    public Genre getGenreByName(String name){
        return genreRepository.findGenreByName(name).orElseThrow(()->new IllegalStateException("Genre Did not exists"));
    }
    public void addGenre(Genre genre){
        Optional<Genre> genreOptional = genreRepository.findGenreByName(genre.getName());
        if(genreOptional.isPresent()){
            throw new IllegalStateException("Genre Already Exists");
        }
        genreRepository.save(genre);
    }
    public void DeleteGenre(Integer id){
        genreRepository.deleteById(id);
    }
    public void update(Integer id,Genre updateGenre){
        Genre genre = genreRepository.findGenreById(id).orElseThrow(()->new IllegalStateException("Genre Did not exists"));
        genre.setName(updateGenre.getName());
        genreRepository.save(genre);
    }
}
