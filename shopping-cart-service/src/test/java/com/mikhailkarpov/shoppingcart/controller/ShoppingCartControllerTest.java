package com.mikhailkarpov.shoppingcart.controller;

import com.mikhailkarpov.shoppingcart.dto.ShoppingCart;
import com.mikhailkarpov.shoppingcart.dto.ShoppingCartItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ShoppingCartController.class)
class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenEmptySession_whenGetCart_then200() throws Exception {
        //given
        MockHttpSession session = new MockHttpSession();

        //then
        mockMvc.perform(get("/shopping-cart")
                .session(session)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.items").isEmpty());

        Object cart = session.getAttribute("cart");
        assertNotNull(cart);
        assertTrue(((ShoppingCart) cart).getItems().isEmpty());
    }

    @Test
    void givenSession_whenGetCart_then200() throws Exception {
        //given
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", createStubCart());

        //when then
        mockMvc.perform(get("/shopping-cart")
                .session(session)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].code").value("abc"))
                .andExpect(jsonPath("$.items[0].quantity").value(10))
                .andExpect(jsonPath("$.items[1].code").value("xyz"))
                .andExpect(jsonPath("$.items[1].quantity").value(20));

        Object cart = session.getAttribute("cart");
        assertNotNull(cart);
        assertEquals(2, ((ShoppingCart) cart).getItems().size());
    }

    @Test
    void givenEmptySession_whenCreateCart_then201() throws Exception {
        //given
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/shopping-cart")
                .session(session)
                .contentType(APPLICATION_JSON)
                .content("{" +
                        "\"items\": [" +
                        "{" +
                        "\"code\": \"abc\"," +
                        "\"quantity\": 12" +
                        "}," +
                        "{" +
                        "\"code\": \"xyz\"," +
                        "\"quantity\": 14" +
                        "}" +
                        "]" +
                        "}")
                .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/shopping-cart"))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].code").value("abc"))
                .andExpect(jsonPath("$.items[0].quantity").value(12))
                .andExpect(jsonPath("$.items[1].code").value("xyz"))
                .andExpect(jsonPath("$.items[1].quantity").value(14));

        Object cart = session.getAttribute("cart");
        assertNotNull(cart);
        assertEquals(2, ((ShoppingCart) cart).getItems().size());
    }

    @Test
    void givenSession_whenCreateCart_then409() throws Exception {
        //given
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", createStubCart());

        //then
        mockMvc.perform(post("/shopping-cart")
                .session(session)
                .contentType(APPLICATION_JSON)
                .content("{" +
                        "\"items\": [" +
                        "{" +
                        "\"code\": \"abc\"," +
                        "\"quantity\": 12" +
                        "}," +
                        "{" +
                        "\"code\": \"xyz\"," +
                        "\"quantity\": 14" +
                        "}" +
                        "]" +
                        "}")
                .accept(APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void givenSession_whenUpdateCart_then200() throws Exception {
        //given
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", createStubCart());

        //when then
        mockMvc.perform(MockMvcRequestBuilders.put("/shopping-cart")
                .session(session)
                .contentType(APPLICATION_JSON)
                .content("{" +
                        "\"items\": [" +
                        "{" +
                        "\"code\": \"abc\"," +
                        "\"quantity\": 14" +
                        "}," +
                        "{" +
                        "\"code\": \"xyz\"," +
                        "\"quantity\": 44" +
                        "}," +
                        "{" +
                        "\"code\": \"efg\"," +
                        "\"quantity\": 24" +
                        "}" +
                        "]" +
                        "}")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].code").value("abc"))
                .andExpect(jsonPath("$.items[0].quantity").value(14))
                .andExpect(jsonPath("$.items[1].code").value("xyz"))
                .andExpect(jsonPath("$.items[1].quantity").value(44))
                .andExpect(jsonPath("$.items[2].code").value("efg"))
                .andExpect(jsonPath("$.items[2].quantity").value(24));

        Object cart = session.getAttribute("cart");
        assertNotNull(cart);
        assertEquals(3, ((ShoppingCart) cart).getItems().size());
    }

    @Test
    void givenEmptySession_whenUpdateCart_then200() throws Exception {
        //given
        MockHttpSession session = new MockHttpSession();

        //when then
        mockMvc.perform(MockMvcRequestBuilders.put("/shopping-cart")
                .session(session)
                .contentType(APPLICATION_JSON)
                .content("{" +
                        "\"items\": [" +
                        "{" +
                        "\"code\": \"abc\"," +
                        "\"quantity\": 10" +
                        "}," +
                        "{" +
                        "\"code\": \"xyz\"," +
                        "\"quantity\": 20" +
                        "}" +
                        "]" +
                        "}")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].code").value("abc"))
                .andExpect(jsonPath("$.items[0].quantity").value(10))
                .andExpect(jsonPath("$.items[1].code").value("xyz"))
                .andExpect(jsonPath("$.items[1].quantity").value(20));

        Object cart = session.getAttribute("cart");
        assertNotNull(cart);
        assertEquals(2, ((ShoppingCart) cart).getItems().size());
    }

    @Test
    void givenSession_whenDeleteCart_then204() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", createStubCart());

        mockMvc.perform(delete("/shopping-cart")
                .session(session))
                .andExpect(status().isNoContent());

        assertNull(session.getAttribute("cart"));
    }

    private ShoppingCart createStubCart() {
        return new ShoppingCart(Arrays.asList(
                new ShoppingCartItem("abc", 10),
                new ShoppingCartItem("xyz", 20)
        ));
    }
}