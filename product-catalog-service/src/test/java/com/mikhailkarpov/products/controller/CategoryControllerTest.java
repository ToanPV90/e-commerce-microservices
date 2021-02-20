package com.mikhailkarpov.products.controller;

import com.mikhailkarpov.products.controller.mapper.CategoryMapper;
import com.mikhailkarpov.products.dto.CategoryDto;
import com.mikhailkarpov.products.persistence.entity.Category;
import com.mikhailkarpov.products.service.CategoryService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private CategoryMapper categoryMapper;

    @Autowired
    private MockMvc mockMvc;

    private final List<Category> categories = Arrays.asList(
            new Category("category 1"),
            new Category("category 2")
    );

    private final List<CategoryDto> dtoList = Arrays.asList(
            new CategoryDto(1, "category 1"),
            new CategoryDto(2, "category 2")
    );

    @Test
    void findAll_excludingSubcategories() throws Exception {

        when(categoryService.findAll(false)).thenReturn(categories);
        when(categoryMapper.map(categories.get(0))).thenReturn(dtoList.get(0));
        when(categoryMapper.map(categories.get(1))).thenReturn(dtoList.get(1));

        mockMvc.perform(get("/categories")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("category 1"))
                .andExpect(jsonPath("$[1].name").value("category 2"));

        verify(categoryService).findAll(false);
        verify(categoryMapper).map(categories.get(0));
        verify(categoryMapper).map(categories.get(1));
        verifyNoMoreInteractions(categoryService, categoryMapper);
    }

    @Test
    void findAll_includingSubcategories() throws Exception {

        when(categoryService.findAll(true)).thenReturn(categories);
        when(categoryMapper.map(categories.get(0))).thenReturn(dtoList.get(0));
        when(categoryMapper.map(categories.get(1))).thenReturn(dtoList.get(1));

        mockMvc.perform(get("/categories?includeSubdirectories={includeSubdirectories}", true)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("category 1"))
                .andExpect(jsonPath("$[1].name").value("category 2"));

        verify(categoryService).findAll(true);
        verify(categoryMapper, times(2)).map(any(Category.class));
        verifyNoMoreInteractions(categoryService, categoryMapper);
    }

    @Test
    void createCategory() throws Exception {

        when(categoryService.createCategory("category 1")).thenReturn(categories.get(0));
        when(categoryMapper.map(categories.get(0))).thenReturn(dtoList.get(0));

        mockMvc.perform(post("/categories?name={name}", "category 1")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/categories/1"))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("category 1"));

        verify(categoryService).createCategory("category 1");
        verify(categoryMapper).map(categories.get(0));
        verifyNoMoreInteractions(categoryService, categoryMapper);
    }

    @Test
    void findCategoryById() throws Exception {
        //when
        when(categoryService.findById(2)).thenReturn(categories.get(1));
        when(categoryMapper.map(categories.get(1))).thenReturn(dtoList.get(1));

        mockMvc.perform(get("/categories/{id}", 2)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("category 2"));

        verify(categoryService).findById(2);
        verify(categoryMapper).map(categories.get(1));
        verifyNoMoreInteractions(categoryService, categoryMapper);
    }

    @Test
    void renameCategory() throws Exception {
        Category renamed = new Category("renamed category");
        CategoryDto dto = new CategoryDto(2, "renamed category");

        when(categoryService.renameCategory(2, "renamed category")).thenReturn(renamed);
        when(categoryMapper.map(renamed)).thenReturn(dto);

        mockMvc.perform(put("/categories/{id}?name={name}", 2, "renamed category")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("renamed category"));

        verify(categoryService).renameCategory(2, "renamed category");
        verify(categoryMapper).map(renamed);
        verifyNoMoreInteractions(categoryService, categoryMapper);
    }

    @Test
    void findSubcategoriesByParentId() throws Exception {

        when(categoryService.findSubcategoriesByParentId(1)).thenReturn(Arrays.asList(categories.get(1)));
        when(categoryMapper.map(categories.get(1))).thenReturn(dtoList.get(1));

        mockMvc.perform(get("/categories/{id}/subcategories", 1)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("category 2"));

        verify(categoryService).findSubcategoriesByParentId(1);
        verify(categoryMapper).map(categories.get(1));
        verifyNoMoreInteractions(categoryService, categoryMapper);
    }

    @Test
    void createSubcategory() throws Exception {
        Category subcategory = new Category("subcategory");
        subcategory.setId(21);
        CategoryDto dto = new CategoryDto(21, "subcategory");

        when(categoryService.createSubcategory(11, "subcategory")).thenReturn(subcategory);
        when(categoryMapper.map(subcategory)).thenReturn(dto);

        mockMvc.perform(post("/categories/{id}/subcategories?name={name}", 11, "subcategory")
                .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/categories/21"))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(21))
                .andExpect(jsonPath("$.name").value("subcategory"));

        verify(categoryService).createSubcategory(11, "subcategory");
        verify(categoryMapper).map(subcategory);
        verifyNoMoreInteractions(categoryService, categoryMapper);
    }

    @Test
    void addProduct() throws Exception {

        mockMvc.perform(post("/categories/{id}/products?code={code}", 12, "ABC"))
                .andExpect(status().isOk());

        verify(categoryService).addProduct(12, "ABC");
        verifyNoMoreInteractions(categoryService, categoryMapper);
    }

    @Test
    void removeProduct() throws Exception {

        mockMvc.perform(delete("/categories/{id}/products/{code}", 12, "ABC"))
                .andExpect(status().isOk());

        verify(categoryService).removeProduct(12, "ABC");
        verifyNoMoreInteractions(categoryService, categoryMapper);
    }

    @Test
    void moveCategory() throws Exception {

        mockMvc.perform(post("/categories/{id}/move?destination={destination}", 12, 13))
                .andExpect(status().isOk());

        verify(categoryService).moveCategory(12, 13);
        verifyNoMoreInteractions(categoryService, categoryMapper);
    }
}