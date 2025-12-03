package com.example.remember_words.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.remember_words.entity.User;
import com.example.remember_words.entity.WordGroup;

public interface WordGroupRepository extends JpaRepository<WordGroup, Long> {
    
    Optional<WordGroup> findByIdAndUser(Long id, User user);

    List<WordGroup> findAllByUserOrderByIdAsc(User user);

}
