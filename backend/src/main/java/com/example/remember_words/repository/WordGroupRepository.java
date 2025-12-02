package com.example.remember_words.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import com.example.remember_words.entity.User;
import com.example.remember_words.entity.WordGroup;

public interface WordGroupRepository extends JpaRepository<WordGroup, Long> {

    Optional<WordGroup> findByName(String name);
    
    Optional<WordGroup> findById(@NonNull Long id);

    List<WordGroup> findAllByUserOrderByIdAsc(User user);

}
