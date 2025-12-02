package com.example.remember_words.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.remember_words.entity.User;
import com.example.remember_words.entity.WordGroup;
import com.example.remember_words.entity.Words;

public interface WordsRepository extends JpaRepository<Words, Long> {
    List<Words> findByGroupId(WordGroup groupId);
    List<Words> findAllByOrderByIdAsc();
    List<Words> findByGroupIdOrderByIdAsc(WordGroup group);
    Optional<Words> findByIdAndUserUsername(Long id, User username);
}
