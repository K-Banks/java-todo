package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void NewCategoryObjectGetsCorrectlyCreate_true() {
        Category category = new Category("test");
        assertTrue(category instanceof Category);
    }

    @Test
    public void getName() {
        Category category = new Category("test");
        assertTrue(category.getName().equals("test"));
    }

    @Test
    public void setName() {
        Category category = new Category("test");
        category.setName("string");
        assertEquals("string", category.getName());
    }
}