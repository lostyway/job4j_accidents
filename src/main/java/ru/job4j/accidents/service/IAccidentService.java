package ru.job4j.accidents.service;

import ru.job4j.accidents.model.Accident;

import java.util.List;

public interface IAccidentService {
    List<Accident> getAllAccidents();
}
