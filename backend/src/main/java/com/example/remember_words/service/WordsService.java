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
    private final CurrentUserService currentUserService;

    public WordsService(WordsRepository wordsRepository, WordGroupRepository wordGroupRepository, UserRepository userRepository, CurrentUserService currentUserService) {
        this.wordsRepository = wordsRepository;
        this.wordGroupRepository = wordGroupRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public Words save(String foreignWord, String translatedWord, Long groupId) {
        User currentUser = getCurrentUser();

        WordGroup wordGroup = null;
        if (groupId != null) {
            wordGroup = wordGroupRepository.findByIdAndUser(groupId, currentUser)
                                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Word group not found"));
        }

        Words words = new Words(foreignWord, translatedWord, wordGroup, currentUser);

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
    public Words deleteWords(long id) {
        User user = getCurrentUser();
        Words words = getWordsByIdAndUser(id, user);
        wordsRepository.deleteById(words.getId());
        logger.info("Deleted words | Foreign word : {} | translated word: {}", words.getForeignWord(), words.getForeignWord());
        return words;
    }

    @Transactional
    public Words updateWords(long id, String foreignWord, String translatedWord) {
        User user = getCurrentUser();
        Words words = getWordsByIdAndUser(id, user);
        logger.info("Updating | Foreign Word: {} -> {} | Translated word: {} -> {}",
                words.getForeignWord(), foreignWord, words.getTranslatedWord(), translatedWord);

        words.setForeignWord(foreignWord);
        words.setTranslatedWord(translatedWord);
        return words;
    }

    @Transactional(readOnly = true)
    public List<Words> findAllWords() {
        User user = getCurrentUser();
        return wordsRepository.findAllByUserOrderByIdAsc(user);
    }

    @Transactional(readOnly = true)
    public List<WordGroup> findAllGroups() {
        User user = getCurrentUser();
        return wordGroupRepository.findAllByUserOrderByIdAsc(user);
    }

    @Transactional(readOnly = true)
    public List<Words> findWordsByIds(List<Long> ids) {
        User user = getCurrentUser();
        return wordsRepository.findAllByIdInAndUser(ids, user);
    }

    @Transactional(readOnly = true)
    public List<Words> findWordsByGroup(Long groupId) {
        User user = getCurrentUser();
        WordGroup group = wordGroupRepository.findByIdAndUser(groupId, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));
        return wordsRepository.findByGroupIdAndUserOrderByIdAsc(group, user);
    }

    private User getCurrentUser() {
        String username = currentUserService.getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private Words getWordsByIdAndUser(long id, User user) {
        return wordsRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Words not found"));
    }


}
