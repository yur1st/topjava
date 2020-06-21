package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Autowired
    private MealRepository repository;

    @Test
    public void get() {
        Meal meal = service.get(FIRST_MEAL_ID, USER_ID);
        assertMatch(meal, TEST_MEAL1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getNotFoundWrongUser() {
        assertThrows(NotFoundException.class, () -> service.get(FIRST_MEAL_ID, WRONG_USER_ID));
    }

    @Test
    public void delete() {
        service.delete(FIRST_MEAL_ID, USER_ID);
        assertNull(repository.get(FIRST_MEAL_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteNotFoundWrongUser() {
        assertThrows(NotFoundException.class, () -> service.delete(FIRST_MEAL_ID, WRONG_USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> filtered = service.getBetweenInclusive(START_DATE, END_DATE, USER_ID);
        assertMatch(filtered, TEST_MEAL3, TEST_MEAL2, TEST_MEAL1);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, TEST_MEAL7, TEST_MEAL6, TEST_MEAL5, TEST_MEAL4, TEST_MEAL3, TEST_MEAL2, TEST_MEAL1);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(FIRST_MEAL_ID, USER_ID), updated);
    }

    @Test
    public void updateWrongUser() {
        Meal updated = getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updated, WRONG_USER_ID));
    }

    @Test
    public void create() {
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }
}