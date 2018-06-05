package dao;

import models.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import models.Category;

import static org.junit.Assert.*;

public class Sql2oCategoryDaoTest {

    private Sql2oCategoryDao categoryDao;
    private Sql2oTaskDao taskDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        categoryDao = new Sql2oCategoryDao(sql2o);
        taskDao = new Sql2oTaskDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingCategorySetsId() throws Exception {
        Category category = new Category("Home");
        Category newCategory = new Category("Yardwork");
        int originalCategoryId = category.getId();
        categoryDao.add(category);
        assertNotEquals(originalCategoryId, category.getId());
    }

    @Test
    public void existingCategorysCanBeFoundById() throws Exception {
        Category category = new Category("Home");
        categoryDao.add(category);
        Category foundCategory = categoryDao.findById(category.getId());
        assertEquals(category, foundCategory);
    }

    @Test
    public void addedCategorysAreReturnedFromgetAll() throws Exception {
        Category category = new Category ("Home");
        categoryDao.add(category);
        assertEquals(1, categoryDao.getAll().size());
    }

    @Test
    public void noCategorysReturnsEmptyList() throws Exception {
        assertEquals(0, categoryDao.getAll().size());
    }

    @Test
    public void updateACategory() throws Exception {
        Category category = new Category("Home");
        categoryDao.add(category);
        String expected = "Work";
        categoryDao.update(category.getId(), expected);
        assertEquals(expected, categoryDao.findById(category.getId()).getName());
    }

    @Test
    public void deleteACategoryUsingId() throws Exception {
        Category category = new Category("Home");
        categoryDao.add(category);
        categoryDao.deleteById(category.getId());
        assertEquals(0, categoryDao.getAll().size());
    }

    @Test
    public void deleteAllCategorysUsingclearAllCategorys() throws Exception {
        Category category = new Category("Home");
        Category otherCategory = new Category("Work");
        categoryDao.add(category);
        categoryDao.add(otherCategory);
        assertEquals(2, categoryDao.getAll().size());
        categoryDao.clearAllCategories();
        assertEquals(0, categoryDao.getAll().size());
    }

    @Test
    public void getAllTasksByCategoryReturnsTasksCorrectly() throws Exception {
        Category category = new Category("Home");
        categoryDao.add(category);
        int categoryId = category.getId();
        Task newTask = new Task("mow the lawn", categoryId);
        Task otherTask = new Task("pull weeds", categoryId);
        Task thirdTask = new Task("trim hedge", categoryId);
        taskDao.add(newTask);
        taskDao.add(otherTask);
        assertEquals(2, categoryDao.getAllTasksByCategory(categoryId).size());
        assertTrue(categoryDao.getAllTasksByCategory(categoryId).contains(newTask));
        assertTrue(categoryDao.getAllTasksByCategory(categoryId).contains(otherTask));
        assertFalse(categoryDao.getAllTasksByCategory(categoryId).contains(thirdTask));
    }
}