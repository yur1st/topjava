package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealsMockDB;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final String INSERT_OR_EDIT = "/meal.jsp";
    private static final String LIST_MEALS = "/meals.jsp";
    private static final String MEALS = "meals";
    private static final int CALORIES = 2000;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final MealsMockDB dao;

    public MealServlet() {
        super();
        dao = new MealsMockDB();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward;
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")){
            int mealId = 0;
            try {
                mealId = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException e) {
                log.error("Entered Id is not a number", e);
            }
            dao.delete(mealId);
            log.debug("delete meal with id = {}", mealId);
            forward = LIST_MEALS;
            request.setAttribute(MEALS, getAllMealsTo());
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int mealId = 0;
            try {
                mealId = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException e) {
                log.error("Entered Id is not a number", e);
            }
            Meal meal = dao.getById(mealId);
            log.debug("edit meal with id = {}", mealId);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("listMeals")){
            forward = LIST_MEALS;
            request.setAttribute(MEALS, getAllMealsTo());
            log.debug("forward to meals");
        } else {
            forward = INSERT_OR_EDIT;
            log.debug("forward to insert/edit");
        }
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("Wrong encoding", e);
        }
        int id = 0;
        String strId = request.getParameter("id");
        if (strId != null && !strId.isEmpty()) {
            try {
                id = Integer.parseInt(strId);
            } catch (NumberFormatException e) {
                log.error("Wrong number", e);
            }
        }
        String description = request.getParameter("description");
        LocalDateTime date = LocalDateTime.parse(request.getParameter("dateTime"), dateTimeFormatter);
        int calories = 0;
        try {
            calories = Integer.parseInt(request.getParameter("calories"));
        } catch (NumberFormatException e) {
            log.error("Wrong number", e);
        }

        if(id == 0)
        {
            dao.add(new Meal(MealsMockDB.NEW_ID.getAndIncrement(), date, description, calories));
            log.debug("add new meal with id = {}", id);
        }
        else
        {
            dao.update(id, new Meal(id, date, description, calories));
            log.debug("update meal with id = {}", id);
        }

        RequestDispatcher view = request.getRequestDispatcher(LIST_MEALS);
        request.setAttribute(MEALS, getAllMealsTo());
        view.forward(request, response);
    }

    private List<MealTo> getAllMealsTo() {
        return MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES);
    }
}
