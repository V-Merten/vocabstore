package com.example.remember_words.service;

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
public class WordGroupService {

    private final Logger logger = LoggerFactory.getLogger(WordGroupService.class);
    private final WordGroupRepository wordGroupRepository;
    private final WordsRepository wordsRepository;
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;

    public WordGroupService(WordGroupRepository wordGroupRepository, WordsRepository wordsRepository, CurrentUserService currentUserService, UserRepository userRepository) {
        this.wordGroupRepository = wordGroupRepository;
        this.wordsRepository = wordsRepository;
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
    }

    @Transactional
    public WordGroup createWordsGroup(String groupName) {
        User currentUser = getCurrentUser();
        WordGroup wordGroup = new WordGroup();
        wordGroup.setName(groupName.trim());
        wordGroup.setUser(currentUser);
        wordGroupRepository.save(wordGroup);

        logger.info("New group has been created: user={}, groupName={}",
                groupName, wordGroup.getName());

        return wordGroup;
    }

    @Transactional
    public Words addWordsToGroup(Long wordsId, Long groupId) {
        Words existingWord = findWords(wordsId);
        WordGroup existingGroup = findGroup(groupId);
        existingWord.setGroupId(existingGroup);
        logger.info("Word added to group: word={}, group={}", existingWord.getForeignWord(), existingGroup.getName());
        return wordsRepository.save(existingWord);
    }

    @Transactional
    public Words removeWordsFromGroup(Long wordsId) {
        Words existingWord = findWords(wordsId);
        existingWord.setGroupId(null);
        logger.info("Word removed from group: word={}", existingWord.getForeignWord());
        return wordsRepository.save(existingWord);
    }

    @Transactional
    public WordGroup renameWordGroup(Long GroupId, String newWordGroupName) {
        WordGroup existingGroup = findGroup(GroupId);
        logger.info("Group renamed: old group name= {} |  new group name={}", existingGroup.getName(), newWordGroupName);
        existingGroup.setName(newWordGroupName.trim());
        return wordGroupRepository.save(existingGroup);
    }

    @Transactional
    public void deleteGroup(Long groupId) {
        WordGroup existingGroup = findGroup(groupId);
        wordGroupRepository.delete(existingGroup);
        logger.info("Group deleted: id={} | name={}", existingGroup.getId(), existingGroup.getName());
    }

    private Words findWords (Long wordsId) {
        User currentUser = getCurrentUser();
        return wordsRepository.findByIdAndUser(wordsId, currentUser)
            .orElseThrow(() -> new RuntimeException("Word not found"));
    }

    private WordGroup findGroup (Long groupId) {
        User currentUser = getCurrentUser();
        return wordGroupRepository.findByIdAndUser(groupId, currentUser)
            .orElseThrow(() -> new RuntimeException("Group not found"));
    }

    private User getCurrentUser() {
        String username = currentUserService.getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
