package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = {Profiles.DATAJPA})
public class DataJpaUserServiceTest extends UserServiceTest {

    @Test
    public void getWithMeals() throws Exception {
        User user = service.getWithMeals(ADMIN_ID);
        List<Meal> meals = user.getMeals();
        USER_MATCHER_WITHOUT_MEALS.assertMatch(user, ADMIN);
        MEAL_MATCHER.assertMatch(meals, ADMIN_MEAL2, ADMIN_MEAL1);
    }
}
