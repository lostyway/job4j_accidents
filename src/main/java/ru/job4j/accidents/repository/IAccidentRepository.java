package ru.job4j.accidents.repository;

import ru.job4j.accidents.model.Accident;

import java.util.List;
import java.util.Optional;

public interface IAccidentRepository {
    List<Accident> getAllAccidents();

    Optional<Accident> create(Accident accident);

    Optional<Accident> findById(int id);

    boolean update(Accident accident);
}
