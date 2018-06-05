package dao;

import models.Task;
import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class Sql2oTaskDaoTest {
    private Sql2oTaskDao taskDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        taskDao = new Sql2oTaskDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingCourseSetsId() throws Exception {
        Task task = new Task("mow the lawn", 1);
        int originalTaskId = task.getId();
        taskDao.add(task);
        assertNotEquals(originalTaskId, task.getId());
    }

    @Test
    public void existingTasksCanBeFoundById() throws Exception {
        Task task = new Task("mow the lawn", 1);
        taskDao.add(task);
        Task foundTask = taskDao.findById(task.getId());
        assertEquals(task, foundTask);
    }

    @Test
    public void addedTasksAreReturnedFromgetAll() throws Exception {
        Task task = new Task ("mow the lawn", 1);
        taskDao.add(task);
        assertEquals(1, taskDao.getAll().size());
    }

    @Test
    public void noTasksReturnsEmptyList() throws Exception {
        assertEquals(0, taskDao.getAll().size());
    }

    @Test
    public void updateATask() throws Exception {
        Task task = new Task("mow the lawn", 1);
        taskDao.add(task);
        String expected = "clean the dishes";
        taskDao.update(task.getId(), expected, task.getCategoryId());
        assertEquals(expected, taskDao.findById(task.getId()).getDescription());
    }

    @Test
    public void deleteATaskUsingId() throws Exception {
        Task task = new Task("mow the lawn", 1);
        taskDao.add(task);
        taskDao.deleteById(task.getId());
        assertEquals(0, taskDao.getAll().size());
    }

    @Test
    public void deleteAllTasksUsingclearAllTasks() throws Exception {
        Task task = new Task("mow the lawn", 1);
        Task otherTask = new Task("clean the dishes", 1);
        taskDao.add(task);
        taskDao.add(otherTask);
        assertEquals(2, taskDao.getAll().size());
        taskDao.clearAllTasks();
        assertEquals(0, taskDao.getAll().size());
    }

    @Test
    public void categoryIdIsReturnedCorrectly() throws Exception {
        Task task = new Task("mow the lawn", 1);
        int originalCatId = task.getCategoryId();
        taskDao.add(task);
        assertEquals(originalCatId, taskDao.findById(task.getId()).getCategoryId());
    }

    @Test
    public void updateChangesTaskContent() throws Exception {
        String initialDescription = "mow the lawn";
        Task task = new Task(initialDescription, 2);
        taskDao.add(task);
        taskDao.update(task.getId(), "brush the cat", 1);
        Task updatedTask = taskDao.findById(task.getId());
        assertNotEquals(initialDescription, updatedTask.getDescription());
        assertNotEquals(2, updatedTask.getCategoryId());
    }
}
