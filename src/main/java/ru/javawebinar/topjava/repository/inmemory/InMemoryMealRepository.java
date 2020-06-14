package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.Pair;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    public InMemoryMealRepository() {
        MealsUtil.MEALS.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            if (!repository.containsKey(userId)) {
                repository.put(userId, new HashMap<>());
            }
            repository.get(userId).put(meal.getId(), meal);
            log.info("save {}", meal);
            return meal;
        }
        // handle case: update, but not present in storage
        if (isBelongToUser(meal.getId(), userId)) {
            log.info("save {}", meal);
            return repository.get(userId).computeIfPresent(meal.getId(),
                    (id, oldMeal) -> meal);
        } else {
            log.info("Meal doesn't belong to user {}", meal);
            return null;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        if (isBelongToUser(id, userId)) {
            log.info("delete {}", repository.get(userId).get(id));
            return repository.get(userId).remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        if (isBelongToUser(id, userId)) {
            Meal meal = repository.get(userId).get(id);
            log.info("get {}", meal);
            return meal;
        }
        return null;
    }

    private boolean isBelongToUser(int id, int userId) {
        Map<Integer, Meal> map = repository.getOrDefault(userId, null);
        return map != null && map.containsKey(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("Return all rows");
        return getFiltered(LocalDate.MIN, LocalDate.MAX, userId);
    }

    @Override
    public List<Meal> getDateFiltered(LocalDate startDate, LocalDate endDate, int userId) {
        log.info("Return rows filtered by date");
        return getFiltered(startDate, endDate, userId);
    }

    @Override
    public List<Meal> getFiltered(LocalDate startDate, LocalDate endDate, int userId) {
        return repository.getOrDefault(userId, new HashMap<>()).values().stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate))
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(Collectors.toCollection(LinkedList::new));
    }
}

