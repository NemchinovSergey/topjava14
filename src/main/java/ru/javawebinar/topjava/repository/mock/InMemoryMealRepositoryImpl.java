package ru.javawebinar.topjava.repository.mock;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;

public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public void delete(int id, int userId) {
        Meal meal = repository.get(id);
        checkNotFound(meal, "Meal not found");
        if (meal != null && meal.getUserId() == userId) {
            repository.remove(id);
        }
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        if (meal != null && meal.getUserId() == userId) {
            return meal;
        }
        return null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.values()
                         .stream()
                         .filter(meal -> meal.getUserId() == userId)
                         .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                         .collect(Collectors.toList());
    }
}

