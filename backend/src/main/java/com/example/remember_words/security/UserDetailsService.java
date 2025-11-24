package com.example.remember_words.security;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsService {
    
    UserDetailsService loadUserByUsername(String username) 
    throws UsernameNotFoundException;

}
