package com.shoppingcart.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.shoppingcart.model.User;
import com.shoppingcart.repository.RoleRepository;
import com.shoppingcart.repository.UserRepository;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    private Optional<User> user = Optional.of(new User());

    @Before
    public void setUp() {
        User mUser = user.get();
        mUser.setName("UserName");
        when(userRepository.findByEmail(any())).thenReturn(user);
        when(userRepository.findByUsername(any())).thenReturn(user);
    }

    @After
    public void tearDown() {
        user = null;
    }

    @Test
    public void testFindByEmail() {
        Optional<User> optionalUser = userService.findByEmail("test@gmail.com");
        assertNotNull(optionalUser);
        assertEquals(optionalUser.get().getName(), user.get().getName());
    }

    @Test
    public void testFindByName() {
        Optional<User> optionalUser = userService.findByUsername("UserName");
        assertNotNull(optionalUser);
        assertEquals(optionalUser.get().getName(), user.get().getName());
    }

}
