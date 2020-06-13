package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Pair;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Pair<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        int userId = SecurityUtil.authUserId();
        if (meal.isNew() && userId != 0) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), new Pair<>(userId, meal));
            return meal;
        }
        // handle case: update, but not present in storage
        if (repository.get(meal.getId()).first == userId) {
            return repository.computeIfPresent(meal.getId(),
                    (id, mealPair) -> new Pair<>(userId, meal)).second;
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(int id) {
        if (isBelongToUser(id)) {
            return repository.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int id) {
        if (isBelongToUser(id)) {
            return repository.get(id).second;
        }
        return null;
    }

    private boolean isBelongToUser(int id) {
        return repository.get(id).first == SecurityUtil.authUserId();
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.values().stream()
                .filter(pair -> pair.first == SecurityUtil.authUserId())
                .map(pair -> pair.second)
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(Collectors.toCollection(LinkedList::new));

    }
}

