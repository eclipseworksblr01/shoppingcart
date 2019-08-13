package com.shoppingcart.service;

import java.util.Optional;

import com.shoppingcart.model.User;

public interface UserService {

    public Optional<User> findByUsername(String username);

    public Optional<User> findByEmail(String email);

    public User saveUser(User user);

}
