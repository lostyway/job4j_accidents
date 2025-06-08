package ru.job4j.accidents.service;

import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.util.List;
import java.util.Set;

public interface IAccidentService {
    List<Accident> getAllAccidents();

    Accident create(Accident accident, Set<Integer> rIds);

    Accident findById(int id);

    void update(Accident accident, Set<Integer> rIds);

    List<AccidentType> getAllTypes();

    List<Rule> getAllRules();

    void validateFields(Accident accident);
}