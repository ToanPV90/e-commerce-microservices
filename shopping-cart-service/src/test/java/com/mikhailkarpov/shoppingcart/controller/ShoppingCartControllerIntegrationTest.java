package com.mikhailkarpov.shoppingcart.controller;

import com.mikhailkarpov.shoppingcart.AbstractIntegrationTest;
import com.mikhailkarpov.shoppingcart.dto.ShoppingCart;
import com.mikhailkarpov.shoppingcart.dto.ShoppingCartItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ShoppingCartControllerIntegrationTest extends AbstractIntegrationTest {

    private final String URL = "/shopping-cart";

    @Autowired
    private RedisTemplate<String, ShoppingCart> redisTemplate;

    @Autowired
    private MockMvc mockMvc;

    private final List<ShoppingCartItem> items = Arrays.asList(
            new ShoppingCartItem("abc", 15),
            new ShoppingCartItem("xyz", 10));

    @Test
    void contextLoads() {
        assertNotNull(redisTemplate);
        assertNotNull(mockMvc);
    }

    @BeforeEach
    void saveShoppingCart() {
        assertNull(redisTemplate.opsForValue().get("100"));
        redisTemplate.opsForValue().set("100", new ShoppingCart("100", items));
        assertNotNull(redisTemplate.opsForValue().get("100"));
    }

    @AfterEach
    void deleteShoppingCart() {
        assertNotNull(redisTemplate.opsForValue().get("100"));
        redisTemplate.delete("100");
        assertNull(redisTemplate.opsForValue().get("100"));
    }

    @Test
    void givenEmptyCookie_whenGetCart_then200() throws Exception {
        //given
        MockHttpSession session = new MockHttpSession();

        //when then
        mockMvc.perform(get(URL)
                .session(session)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", startsWith("cartId=" + session.getId())))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(session.getId()))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items").isEmpty());

        assertNull(redisTemplate.opsForValue().get(session.getId()));
    }

    @Test
    void givenCookie_whenGetCart_then200() throws Exception {
        //given
        MockHttpSession session = new MockHttpSession();
        Cookie cookie = new Cookie("cartId", "100");

        //when then
        mockMvc.perform(get(URL)
                .session(session)
                .cookie(cookie)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", startsWith("cartId=100")))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("100"))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items", hasSize(2)));

        assertNull(redisTemplate.opsForValue().get(session.getId()));
    }

    @Test
    void givenNewSession_whenPostCart_then201() throws Exception {
        //given
        MockHttpSession session = new MockHttpSession();

        //then
        mockMvc.perform(post(URL)
                .session(session)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content("[" +
                        "{" +
                        "\"code\": \"abc\"," +
                        "\"quantity\": 150" +
                        "}," +
                        "{" +
                        "\"code\": \"xyz\"," +
                        "\"quantity\": 100" +
                        "}" +
                        "]"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/shopping-cart"))
                .andExpect(header().string("Set-Cookie", startsWith("cartId=" + session.getId())))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(session.getId()))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].code").value("abc"))
                .andExpect(jsonPath("$.items[0].quantity").value(150))
                .andExpect(jsonPath("$.items[1].code").value("xyz"))
                .andExpect(jsonPath("$.items[1].quantity").value(100));

        ShoppingCart savedCart = redisTemplate.opsForValue().get(session.getId());

        assertNotNull(savedCart);
        assertEquals(session.getId(), savedCart.getId());
        assertNotEquals("100", session.getId());
        assertEquals(150, savedCart.getItems().get(0).getQuantity());
        assertEquals(100, savedCart.getItems().get(1).getQuantity());

        redisTemplate.delete(session.getId());
        assertNull(redisTemplate.opsForValue().get(session.getId()));
    }

    @Test
    void givenCookieAndSavedShoppingCart_whenPostCart_then201() throws Exception {
        //given
        Cookie cookie = new Cookie("cartId", "100");

        //then
        mockMvc.perform(post(URL)
                .cookie(cookie)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content("[" +
                        "{" +
                        "\"code\": \"abc\"," +
                        "\"quantity\": 250" +
                        "}," +
                        "{" +
                        "\"code\": \"xyz\"," +
                        "\"quantity\": 150" +
                        "}" +
                        "]"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/shopping-cart"))
                .andExpect(header().string("Set-Cookie", startsWith("cartId=100")))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("100"))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].code").value("abc"))
                .andExpect(jsonPath("$.items[0].quantity").value(250))
                .andExpect(jsonPath("$.items[1].code").value("xyz"))
                .andExpect(jsonPath("$.items[1].quantity").value(150));

        ShoppingCart saved = redisTemplate.opsForValue().get("100");

        assertNotNull(saved.getItems());
        assertEquals("100", saved.getId());
        assertEquals(2, saved.getItems().size());
        assertEquals(250, saved.getItems().get(0).getQuantity());
        assertEquals(150, saved.getItems().get(1).getQuantity());
    }

    @Test
    void givenNewSession_whenDeleteCart_then204() throws Exception {
        mockMvc.perform(delete(URL))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenCookie_whenDeleteCart_then204_andDeleted() throws Exception {
        redisTemplate.opsForValue().set("200", new ShoppingCart("200", items));

        mockMvc.perform(delete(URL)
                .cookie(new Cookie("cartId", "200")))
                .andExpect(status().isNoContent());

        assertNull(redisTemplate.opsForValue().get("200"));
    }
}