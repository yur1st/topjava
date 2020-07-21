package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
@RequestMapping("/meals")
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    MealService service;

    @GetMapping
    public String getAll(Model model) {
        int userId = authUserId();
        log.info("getAll for user {}", userId);
        model.addAttribute("meals",
                MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @GetMapping(value = "/delete/{id}")
    public String delete(@PathVariable int id) {
        int userId = authUserId();
        service.delete(id, userId);
        log.info("get meal {} for user {}", id, userId);
        return "redirect:/meals";
    }

    @GetMapping(value = "/create", params = "new")
    public String showForm(Model model) {
        model.addAttribute("meal",
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }

    @PostMapping(value = "/create")
    public ModelAndView create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        checkNew(meal);
        log.info("create {} for user {}", meal, userId);
        service.create(meal, userId);
        return new ModelAndView("redirect:/meals");
    }

    @GetMapping(value = "/update/{id}")
    public String showUpdateForm(Model model, @PathVariable int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get meal {} for user {}", id, userId);
        Meal meal = service.get(id, userId);
        assureIdConsistent(meal, id);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping(value = "/update/{id}")
    public ModelAndView update(Meal meal, @PathVariable int id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(meal, id);
        log.info("update {} for user {}", meal, userId);
        service.update(meal, userId);
        return new ModelAndView("redirect:/meals");
    }

    @GetMapping(value = "/filter", params = {"startDate", "endDate", "startTime", "endTime"})
    public ModelAndView getBetween(String startDate, String endDate, String startTime, String endTime, Model model) {
        LocalDate sDate = DateTimeUtil.parseLocalDate(startDate);
        LocalDate eDate = DateTimeUtil.parseLocalDate(endDate);
        LocalTime sTime = DateTimeUtil.parseLocalTime(startTime);
        LocalTime eTime = DateTimeUtil.parseLocalTime(endTime);
        int userId = SecurityUtil.authUserId();

        log.info("getBetween dates({} - {}) time({} - {}) for user {}", sDate, eDate, sTime, eTime, userId);
        List<Meal> mealsDateFiltered = service.getBetweenInclusive(sDate, eDate, userId);
        model.addAttribute("meals",
                MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), sTime, eTime));
        return new ModelAndView("meals");
    }


}
