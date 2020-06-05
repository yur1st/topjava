package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MealsMockDB implements CRUDInterface<Meal>{

    private static final List<Meal> mealList = new CopyOnWriteArrayList<>();

    static {
        mealList.addAll(Arrays.asList(
                new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        ));
    }

    public static final AtomicInteger NEW_ID = new AtomicInteger(mealList.size()+1);

    @Override
    public void add(Meal meal) {
        mealList.add(meal);
    }

    @Override
    public void delete(int id) {
        mealList.removeIf(meal -> meal.getId() == id);
    }

    @Override
    public void update(int id, Meal meal) {
        for (Meal m : mealList) {
            if (m.getId() == id) {
                m.setDescription(meal.getDescription());
                m.setDateTime(meal.getDateTime());
                m.setCalories(meal.getCalories());
                break;
            }
        }
    }

    @Override
    public List<Meal> getAll() {
        return mealList;
    }

    @Override
    public Meal getById(int id) {
        for (Meal meal : mealList) {
            if (meal.getId() == id) {
                return meal;
            }
        }
        return null;
    }
}
