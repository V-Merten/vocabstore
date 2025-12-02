package com.example.remember_words.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.example.remember_words.entity.User;
import com.example.remember_words.entity.WordGroup;
import com.example.remember_words.entity.Words;
import com.example.remember_words.repository.UserRepository;
import com.example.remember_words.repository.WordGroupRepository;
import com.example.remember_words.repository.WordsRepository;

@ExtendWith(MockitoExtension.class)
class WordsServiceTest {

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WordsRepository wordsRepository;

    @Mock
    private WordGroupRepository wordGroupRepository;

    @InjectMocks
    private WordsService wordsService;

    private static final String USERNAME = "john";
    private static final Long USER_ID = 10L;
    private static final Long GROUP_ID = 5L;

    private User createUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setUsername(USERNAME);
        user.setEmail("john@example.com");
        user.setPassword("encoded");
        return user;
    }

    private WordGroup createGroup(User user) {
        WordGroup group = new WordGroup();
        group.setId(GROUP_ID);
        group.setName("My group");
        group.setUser(user);
        return group;
    }

    @Test
    void save_shouldCreateWordsWithUserAndGroup(){
        User user = createUser();
        WordGroup group = createGroup(user);

        String foreignWord = "Hola";
        String translatedWord = "Hello";

        when(currentUserService.getCurrentUsername()).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(wordGroupRepository.findById(GROUP_ID)).thenReturn(Optional.of(group));
        when(wordsRepository.save(any(Words.class))).thenAnswer(invocation -> {Words word = invocation.getArgument(0); word.setId(1L); return word;});

        Words savedWords = wordsService.save(foreignWord, translatedWord, GROUP_ID);

        assertNotNull(savedWords.getId());
        assertEquals(group, savedWords.getGroupId());
        assertEquals(foreignWord, savedWords.getForeignWord());
        assertEquals(translatedWord, savedWords.getTranslatedWord());
        assertEquals(user, savedWords.getUser());

        verify(wordGroupRepository).findById(GROUP_ID);
        verify(userRepository).findByUsername(USERNAME);
        verify(wordsRepository).save(any(Words.class));
    }

    @Test
    void save_shouldCreateWordsWithUserAndNoGroup(){
        User user = createUser();
        String foreignWord = "Hola";
        String translatedWord = "Hello";

        when(currentUserService.getCurrentUsername()).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(wordsRepository.save(any(Words.class))).thenAnswer(invocation -> {Words word = invocation.getArgument(0); word.setId(1L); return word;});

        Words savedWords = wordsService.save(foreignWord, translatedWord, GROUP_ID);

        assertNotNull(savedWords.getId());
        assertEquals(savedWords.getGroupId(), null);
        assertEquals(foreignWord, savedWords.getForeignWord());
        assertEquals(translatedWord, savedWords.getTranslatedWord());
        assertEquals(user, savedWords.getUser());

        verify(userRepository).findByUsername(USERNAME);
        verify(wordsRepository).save(any(Words.class));
    }

    @Test
    void save_shouldThrowWhenUserNotFound() {
        when(currentUserService.getCurrentUsername()).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> wordsService.save("Hallo", "Hello", null)
        );

        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void deleteWords_shouldDeleteWordsWhenFound() {
        User user = createUser();
        Words words = new Words();
        words.setId(1L);
        words.setForeignWord("Bonjour");
        words.setTranslatedWord("Hello");
        words.setUser(user);

        when(currentUserService.getCurrentUsername()).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(wordsRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(words));

        Words deletedWords = wordsService.deleteWords(1L);

        assertEquals(words, deletedWords);
        assertEquals("Hello", deletedWords.getTranslatedWord());
        
        verify(wordsRepository).deleteById(words.getId());
    }

    @Test
    void deleteWords_shouldThrowWhenWordsNotFound() {
        User user = createUser();
        when(currentUserService.getCurrentUsername()).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(wordsRepository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> wordsService.deleteWords(1L)
        );

        assertEquals(404, ex.getStatusCode().value());
    }

}
