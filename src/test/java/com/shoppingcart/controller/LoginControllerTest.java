package com.shoppingcart.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {

    @InjectMocks
    LoginController loginController;

    @Mock
    UsernamePasswordAuthenticationToken principal;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testLogin() throws Exception {
        String loginUrl = loginController.login(null);
        assertNotNull(loginUrl);
        assertEquals(loginUrl, "/login");
    }

    @Test
    public void testLoginNoPrincipalAuth() throws Exception {
        String loginUrl = loginController.login(principal);
        assertNotNull(loginUrl);
        assertEquals(loginUrl, "redirect:/home");
    }

}
