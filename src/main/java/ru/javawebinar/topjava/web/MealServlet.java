package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.MealRepositoryImpl;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private static final String ACTION_ADD = "add";
    private static final String ACTION_EDIT = "edit";
    private static final String ACTION_DELETE = "delete";

    private MealRepository repository;

    @Override
    public void init() throws ServletException {
        super.init();
        repository = new MealRepositoryImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        log.info("action parameter: {}", action);

        if (Objects.equals(action, ACTION_ADD)) {
            Meal meal = new Meal(LocalDateTime.now(), "", 500);

            request.setAttribute("meal", meal);
            request.getRequestDispatcher("/mealCard.jsp").forward(request, response);
        } else if (Objects.equals(action, ACTION_EDIT)) {
            Integer id = Integer.valueOf(request.getParameter("id"));

            request.setAttribute("meal", repository.get(id));
            request.getRequestDispatcher("/mealCard.jsp").forward(request, response);
        } else if (Objects.equals(action, ACTION_DELETE)) {
            Integer id = Integer.valueOf(request.getParameter("id"));
            log.info("Delete meal: {}", id);
            repository.delete(id);
            response.sendRedirect("meals");
        } else {
            List<MealWithExceed> filteredWithExceeded = MealsUtil.getFilteredWithExceeded(repository.getAll(),
                                                                                          LocalTime.MIN, LocalTime.MAX,
                                                                                          2000);
            request.setAttribute("meals", filteredWithExceeded);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        log.info("do post meal");

        String id = req.getParameter("id");
        String dateTime = req.getParameter("dateTime");
        String description = req.getParameter("description");
        String calories = req.getParameter("calories");

        if (id == null || id.isEmpty()) {
            Meal meal = new Meal(LocalDateTime.parse(dateTime, TimeUtil.formatter), description,
                                 Integer.valueOf(calories));
            log.info("Create meal: {}", meal);
            repository.save(meal);
        } else {
            Meal meal = new Meal(LocalDateTime.parse(dateTime, TimeUtil.formatter), description,
                                 Integer.valueOf(calories));
            meal.setId(Integer.valueOf(id));
            log.info("Update meal: {}", meal);
            repository.save(meal);
        }

        resp.sendRedirect("meals");
    }
}
