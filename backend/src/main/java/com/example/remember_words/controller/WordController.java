package com.example.remember_words.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.remember_words.dto.CreateWordDto;
import com.example.remember_words.entity.Words;
import com.example.remember_words.service.WordsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class WordController {

    private final Logger log = LoggerFactory.getLogger(WordController.class);
    private final WordsService wordsService;

    public WordController(WordsService wordsService) {
        this.wordsService = wordsService;
    }

    @PutMapping
    public ResponseEntity<Words> updateWords(@RequestBody Words words) {
        try{
            wordsService.updateWords(words.getId(), words.getForeignWord(), words.getTranslatedWord());
            return new ResponseEntity<>(words, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/words")
    public ResponseEntity<Words> saveWords(
            @Valid @RequestBody CreateWordDto createWordDto) {
        try {
            Words newWords = wordsService.save(
                                            createWordDto.getForeignWord(), 
                                            createWordDto.getTranslatedWord(), 
                                            createWordDto.getGroupId());
                                            
            log.info("Created new words | Foreign word={} | Translated word={}",
                    newWords.getForeignWord(), newWords.getTranslatedWord());
            return ResponseEntity.ok(newWords);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping
    public ResponseEntity<List<Words>> getAllWords() {
        try {
            List<Words> words = wordsService.findAllWords();
            for (Words word : words) {
                log.info("Requested word={} | {}", word.getForeignWord(), word.getTranslatedWord());
            }
            return ResponseEntity.ok(words);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Words> deleteAllWords(@PathVariable long id) {
        wordsService.deleteWords(id);
        return ResponseEntity.ok().build();
    }


}
