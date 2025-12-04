package com.example.remember_words.controller;

import com.example.remember_words.dto.GroupDto;
import com.example.remember_words.entity.WordGroup;
import com.example.remember_words.entity.Words;
import com.example.remember_words.service.WordGroupService;
import com.example.remember_words.service.WordsService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/word-groups")
public class WordGroupController {

    private final Logger log = LoggerFactory.getLogger(WordGroupController.class);
    private final WordGroupService wordGroupService;
    private final WordsService wordsService;

    public WordGroupController(WordGroupService wordGroupService, WordsService wordsService) {
        this.wordGroupService = wordGroupService;
        this.wordsService = wordsService;
    }

    @PostMapping("/{groupName}")
    public ResponseEntity<WordGroup> createWordGroup(@PathVariable String groupName) {
        try {
            WordGroup newGroup = wordGroupService.createWordsGroup(groupName);
            return ResponseEntity.ok(newGroup);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/words")
    public ResponseEntity<List<Words>> getWordsByGroup(@RequestParam Long groupId) {
        List<Words> words = wordsService.findWordsByGroup(groupId);
        for (Words word : words) {
        log.info("Words found= {} | {}", word.getForeignWord(), word.getTranslatedWord());
        }
        return ResponseEntity.ok(words);
    }

    @GetMapping("/groups")
    public ResponseEntity<List<WordGroup>> allGroups() {
        List<WordGroup> words = wordsService.findAllGroups();
        for (WordGroup wordGroup : words) {
            log.info("Groups found={}", wordGroup.getName());
        }
        return ResponseEntity.ok(words);
    }

    @PutMapping("/addToGroup/{groupId}/{wordId}")
    public ResponseEntity<Void> updateWordGroup(@PathVariable Long groupId,
                                                @PathVariable Long wordId) {
        wordGroupService.addWordsToGroup(wordId, groupId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/rename")
    public ResponseEntity<Void> renameWordGroup(@Valid @RequestBody GroupDto dto)  {
        wordGroupService.renameWordGroup(dto.getId(), dto.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{wordId}")
    public ResponseEntity<Void> removeWordGroup(@PathVariable Long wordId) {
        wordGroupService.removeWordsFromGroup(wordId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteGroup/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long groupId) {
        wordGroupService.deleteGroup(groupId);
        return ResponseEntity.ok().build();
    }

}
