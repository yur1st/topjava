package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.CrudInterface;
import ru.javawebinar.topjava.dao.MealsMockDB;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    private CrudInterface<Meal> dao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        dao = new MealsMockDB();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward;
        String action;
        if (request.getParameter("action") != null) {
            action = request.getParameter("action");
        } else {
            action = "listMeals";
        }

        switch (action.toLowerCase()) {
            case "delete": {
                int mealId = Integer.parseInt(request.getParameter("id"));
                dao.delete(mealId);
                log.debug("deleted meal with id = {}", mealId);
                request.setAttribute(MEALS, getAllMealsTo());
                response.sendRedirect(MEALS);
                break;
            }
            case "edit": {
                int mealId = Integer.parseInt(request.getParameter("id"));
                Meal meal = dao.getById(mealId);
                log.debug("edit meal with id = {}", mealId);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher(INSERT_OR_EDIT).forward(request, response);
                break;
            }
            case "listmeals": {
                request.setAttribute(MEALS, getAllMealsTo());
                log.debug("forward to meals");
                request.getRequestDispatcher(LIST_MEALS).forward(request, response);
                break;
            }
            default: {
                log.debug("forward to insert/edit");
                request.getRequestDispatcher(INSERT_OR_EDIT).forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        int id = 0;
        String strId = request.getParameter("id");
        if (strId != null && !strId.isEmpty()) {
            id = Integer.parseInt(strId);
        }
        String description = request.getParameter("description");
        LocalDateTime date = LocalDateTime.parse(request.getParameter("dateTime"), dateTimeFormatter);
        int calories = 0;
        calories = Integer.parseInt(request.getParameter("calories"));

        if (id == 0) {
            dao.add(new Meal(MealsMockDB.NEW_ID.get(), date, description, calories));
            log.debug("add new meal with id = {}", MealsMockDB.NEW_ID);
        } else {
            dao.update(id, new Meal(id, date, description, calories));
            log.debug("update meal with id = {}", id);
        }

        response.sendRedirect(MEALS);
    }

    private List<MealTo> getAllMealsTo() {
        return MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES);
    }
}
