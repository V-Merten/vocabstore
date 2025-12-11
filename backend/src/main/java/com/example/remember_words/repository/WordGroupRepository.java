package com.example.remember_words.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.remember_words.entity.User;
import com.example.remember_words.entity.WordGroup;
import com.example.remember_words.entity.Words;

@Repository
public interface WordGroupRepository extends JpaRepository<WordGroup, Long> {

    void deleteAllGroupsByUser(User user);
    
    Optional<WordGroup> findByIdAndUser(Long id, User user);

    List<WordGroup> findAllByUserOrderByIdAsc(User user);

}
