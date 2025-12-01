package com.example.remember_words.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.remember_words.entity.User;
import com.example.remember_words.entity.WordGroup;
import com.example.remember_words.entity.Words;
import com.example.remember_words.repository.UserRepository;
import com.example.remember_words.repository.WordGroupRepository;
import com.example.remember_words.repository.WordsRepository;

@Service
public class WordsService {

    private final Logger logger = LoggerFactory.getLogger(WordsService.class);
    private final WordsRepository wordsRepository;
    private final WordGroupRepository wordGroupRepository;
    private final UserRepository userRepository;

    public WordsService(WordsRepository wordsRepository, WordGroupRepository wordGroupRepository, UserRepository userRepository) {
        this.wordsRepository = wordsRepository;
        this.wordGroupRepository = wordGroupRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Words save(String foreignWord, String translatedWord, Long groupId, String username) {
        WordGroup wordGroup = null;
        if (groupId != null) {
            wordGroup = wordGroupRepository.findById(groupId).orElse(null);
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Words words = new Words(foreignWord, translatedWord, wordGroup, user);

        if (wordGroup != null) {
            logger.info("Saved new words | Foreign word={} | Translated word={} | Group={}",
                    foreignWord, translatedWord, wordGroup.getName());
        } else {
            logger.info("Saved new words | Foreign word={} | Translated word={} | Group=None",
                    foreignWord, translatedWord);
        }

        return wordsRepository.save(words);
    }


    @Transactional
    public void deleteWords(long id) {
        Words words = wordsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Words not found"));
        wordsRepository.deleteById(words.getId());
        logger.info("Deleted words | Foreign word : {} | translated word: {}", words.getForeignWord(), words.getForeignWord());
    }

    @Transactional
    public void updateWords(long id, String foreignWord, String translatedWord) {
        Words words = wordsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Words not found"));

        logger.info("Updating | Foreign Word: {} -> {} | Translated word: {} -> {}",
                words.getForeignWord(), foreignWord, words.getTranslatedWord(), translatedWord);

        words.setForeignWord(foreignWord);
        words.setTranslatedWord(translatedWord);
        wordsRepository.save(words);
    }

    public List<Words> findAllWords() {
        return wordsRepository.findAllByOrderByIdAsc();
    }

    public List<WordGroup> findAllGroups() {
        return wordGroupRepository.findAllByOrderByIdAsc();
    }

    public List<Words> findWordsByIds(List<Long> ids) {
        return wordsRepository.findAllById(ids);
    }

    public List<Words> findWordsByGroup(Long groupId) {
        WordGroup group = wordGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return wordsRepository.findByGroupIdOrderByIdAsc(group);
    }


}
