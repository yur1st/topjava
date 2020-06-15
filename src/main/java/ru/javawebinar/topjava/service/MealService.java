package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {
    private static final Logger log = LoggerFactory.getLogger(MealService.class);
    private final MealRepository repository;

    public MealService(MealRepository repository) {
        log.info("Create MealRepository");
        this.repository = repository;
    }

    public Meal create(Meal meal, int userId) {
        log.info("Create meal");
        return repository.save(meal, userId);
    }

    public void delete(int id, int userId) {
        log.info("Delete meal");
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Meal get(int id, int userId) {
        log.info("Get meal");
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public List<Meal> getAll(int userId) {
        log.info("Get all meals");
        return repository.getAll(userId);
    }

    public void update(Meal meal, int userId) {
        log.info("Update meal");
        checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    public List<Meal> getDateFiltered(LocalDate startDate, LocalDate endDate, int userId) {
        log.info("Get filtered meals");
        return repository.getDateFiltered(startDate, endDate, userId);
    }
}