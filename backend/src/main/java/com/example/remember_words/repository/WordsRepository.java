package com.example.remember_words.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.remember_words.entity.User;
import com.example.remember_words.entity.WordGroup;
import com.example.remember_words.entity.Words;

public interface WordsRepository extends JpaRepository<Words, Long> {
    List<Words> findByGroupIdAndUser(WordGroup groupId, User user);
    List<Words> findAllByUserOrderByIdAsc(User user);
    List<Words> findByGroupIdAndUserOrderByIdAsc(WordGroup group, User user);
    List<Words> findAllByIdInAndUser(List<Long> ids, User user);

    Optional<Words> findByIdAndUser(Long id, User username);
}
