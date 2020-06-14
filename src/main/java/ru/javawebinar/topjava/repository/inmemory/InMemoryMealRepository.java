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
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private Map<Integer, Pair<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    public InMemoryMealRepository() {
        MealsUtil.MEALS.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew() && userId != 0) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), new Pair<>(userId, meal));
            log.info("save {}", meal);
            return meal;
        }
        // handle case: update, but not present in storage
        if (repository.containsKey(meal.getId()) &&
                repository.get(meal.getId()).first == userId) {
            log.info("save {}", meal);
            return repository.computeIfPresent(meal.getId(),
                    (id, mealPair) -> new Pair<>(userId, meal)).second;
        } else {
            log.info("Meal doesn't belong to user {}", meal);
            return null;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        if (isBelongToUser(id, userId)) {
            log.info("delete {}", repository.get(id).second);
            return repository.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        if (isBelongToUser(id, userId)) {
            Meal meal = repository.get(id).second;
            log.info("get {}", meal);
            return meal;
        }
        return null;
    }

    private boolean isBelongToUser(int id, int userId) {
        if (!repository.containsKey(id)) {
            return false;
        }
        return repository.get(id).first == userId;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("Return all rows");
        return getDateFiltered(LocalDate.MIN, LocalDate.MAX, userId);
    }

    @Override
    public Collection<Meal> getDateFiltered(LocalDate startDate, LocalDate endDate, int userId) {
        log.info("Return rows filtered by date");
        return repository.values().stream()
                .filter(pair -> pair.first == userId)
                .map(pair -> pair.second)
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate))
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(Collectors.toCollection(LinkedList::new));
    }
}

