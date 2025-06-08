package ru.job4j.accidents.repository;

import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface IAccidentRepository {
    List<Accident> getAllAccidents();

    Accident create(Accident accident);

    Optional<Accident> findById(int id);

    boolean update(Accident accident);

    List<AccidentType> getAllTypes();

    List<Rule> getAllRules();

    AccidentType getTypeById(int id);

    Set<Rule> getRulesById(Set<Integer> rulesId);

    default void setTypeAndRules(Accident accident) {
        if (accident.getType() != null) {
            accident.setType(getTypeById(accident.getType().getId()));
        } else {
            accident.setType(new AccidentType(0, "Не указано"));
        }

        if (accident.getRules() != null) {
            Set<Integer> rulesIds = accident.getRules().stream().map(Rule::getId).collect(Collectors.toSet());
            accident.setRules(getRulesById(rulesIds));
        }
    }
}
