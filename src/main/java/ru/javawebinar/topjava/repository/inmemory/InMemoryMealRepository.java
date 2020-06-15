package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    public InMemoryMealRepository() {
        MealsUtil.USER1_TEST_MEALS.forEach(meal -> save(meal, 1));
        MealsUtil.USER2_TEST_MEALS.forEach(meal -> save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            getUserMeals(userId).put(meal.getId(), meal);
            log.info("save {}", meal);
            return meal;
        }
        // handle case: update, but not present in storage

        if (getUserMeals(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal) != null) {
            log.info("save {}", meal);
            return meal;
        } else {
            log.info("Meal doesn't belong to user {}", meal);
            return null;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        if (getUserMeals(userId).remove(id) != null) {
            log.info("delete meal #{} belonged to user {}", id, userId);
            return true;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal;
        if ((meal = getUserMeals(userId).get(id)) != null) {
            log.info("get {}", meal);
            return meal;
        }
        return null;
    }

    private Map<Integer, Meal> getUserMeals(int userId) {
        return repository.computeIfAbsent(userId, uId -> new HashMap<>());
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("Return all rows");
        return getFilteredByPredicate(userId, meal -> true);
    }

    @Override
    public List<Meal> getDateFiltered(LocalDate startDate, LocalDate endDate, int userId) {
        log.info("Return rows filtered by date");
        if (!endDate.equals(LocalDate.MAX)) {
            endDate = endDate.plusDays(1);
        }
        LocalDate finalEndDate = endDate;
        return getFilteredByPredicate(userId, meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, finalEndDate));
    }

    private List<Meal> getFilteredByPredicate(int userId, Predicate<Meal> filter) {
        return repository.getOrDefault(userId, new HashMap<>()).values().stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(Collectors.toCollection(LinkedList::new));
    }
}

