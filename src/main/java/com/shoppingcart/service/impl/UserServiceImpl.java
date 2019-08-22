package com.shoppingcart.service.impl;

import java.util.Collections;
import java.util.Optional;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.shoppingcart.logger.ILoggerUtil;
import com.shoppingcart.model.User;
import com.shoppingcart.repository.RoleRepository;
import com.shoppingcart.repository.UserRepository;
import com.shoppingcart.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    ILoggerUtil loggerUtil;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    private static final String USER_ROLE = "ROLE_USER";

    @Override
    public Optional<User> findByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            loggerUtil.log(Level.ERROR, "UserServiceImpl", "findByUsername", "start", "error", "The UserName is empty");
        }
        loggerUtil.log(Level.INFO, "UserServiceImpl", "findByUsername", "start", username);
        Optional<User> findByUsername = userRepository.findByUsername(username);
        loggerUtil.log(Level.INFO, "UserServiceImpl", "findByUsername", "success");
        return findByUsername;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            loggerUtil.log(Level.ERROR, "UserServiceImpl", "findByEmail", "start", "error", "The UserEmail is empty");
        }
        loggerUtil.log(Level.INFO, "UserServiceImpl", "findByEmail", "start", email);
        Optional<User> findByEmail = userRepository.findByEmail(email);
        loggerUtil.log(Level.INFO, "UserServiceImpl", "findByEmail", "success");
        return findByEmail;
    }

    @Override
    public User saveUser(User user) {
        if (user == null) {
            loggerUtil.log(Level.ERROR, "UserServiceImpl", "findByEmail", "start", "error", "The user entity is null");
        }
        loggerUtil.log(Level.INFO, "UserServiceImpl", "findByEmail", "start", user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(1);
        user.setRoles(Collections.singletonList(roleRepository.findByRole(USER_ROLE)));
        User saveAndFlush = userRepository.saveAndFlush(user);
        loggerUtil.log(Level.INFO, "UserServiceImpl", "findByEmail", "success");
        return saveAndFlush;
    }
}
