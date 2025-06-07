package ru.job4j.accidents.service;

import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.util.List;

public interface IAccidentService {
    List<Accident> getAllAccidents();

    Accident create(Accident accident);

    Accident findById(int id);

    boolean update(Accident accident);

    List<AccidentType> getAllTypes();

    List<Rule> getAllRules();
}