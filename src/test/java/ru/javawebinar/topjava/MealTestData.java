package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final int NOT_FOUND = 10;
    public static final int FIRST_MEAL_ID = 100002;
    public static final int USER_ID = 100000;
    public static final int WRONG_USER_ID = 100001;
    public static final LocalDate START_DATE = LocalDate.of(2020, 1, 30);
    public static final LocalDate END_DATE = LocalDate.of(2020, 1, 30);

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final Meal TEST_MEAL1 = new Meal(FIRST_MEAL_ID, LocalDateTime.parse("2020-01-30 10:00:00", DTF), "Завтрак", 500);
    public static final Meal TEST_MEAL2 = new Meal(FIRST_MEAL_ID + 1, LocalDateTime.parse("2020-01-30 13:00:00", DTF), "Обед", 1000);
    public static final Meal TEST_MEAL3 = new Meal(FIRST_MEAL_ID + 2, LocalDateTime.parse("2020-01-30 20:00:00", DTF), "Ужин", 500);
    public static final Meal TEST_MEAL4 = new Meal(FIRST_MEAL_ID + 3, LocalDateTime.parse("2020-01-31 00:00:00", DTF), "Еда на граничное значение", 100);
    public static final Meal TEST_MEAL5 = new Meal(FIRST_MEAL_ID + 4, LocalDateTime.parse("2020-01-31 10:00:00", DTF), "Завтрак", 1000);
    public static final Meal TEST_MEAL6 = new Meal(FIRST_MEAL_ID + 5, LocalDateTime.parse("2020-01-31 13:00:00", DTF), "Обед", 500);
    public static final Meal TEST_MEAL7 = new Meal(FIRST_MEAL_ID + 6, LocalDateTime.parse("2020-01-31 20:00:00", DTF), "Ужин", 410);

    private MealTestData() {
    }

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.now(), "Test Meal", 2000);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(TEST_MEAL1);
        updated.setDescription("UpdatedMeal");
        updated.setCalories(10);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);

    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}
