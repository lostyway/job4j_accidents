package ru.job4j.accidents.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.exception.SaveNotSuccessException;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Setter
@Getter
public class AccidentMem implements IAccidentRepository {
    private final Map<Integer, Accident> accidents = new ConcurrentHashMap<>();

    private final Map<Integer, AccidentType> types = Map.of(
            1, new AccidentType(1, "Две машины"),
            2, new AccidentType(2, "Машина и человек"),
            3, new AccidentType(3, "Машина и велосипед"),
            4, new AccidentType(4, "Машина и природа"),
            5, new AccidentType(5, "Массовое ДТП"),
            6, new AccidentType(6, "Не указано"));

    private final Map<Integer, Rule> rules = Map.of(
            1, new Rule(1, "Статья 1"),
            2, new Rule(2, "Статья 2"),
            3, new Rule(3, "Статья 3"),
            4, new Rule(4, "Статья 4"),
            5, new Rule(5, "Статья 5"),
            6, new Rule(6, "Статья 6"));

    private AtomicInteger accidentId = new AtomicInteger(1);

    @Override
    public List<Accident> getAllAccidents() {
        return new ArrayList<>(accidents.values());
    }

    @Override
    public Accident create(Accident accident, Set<Integer> rIds) {
        try {
            accident.setId(accidentId.getAndIncrement());
            setTypeAndRules(accident);
            accidents.put(accident.getId(), accident);
            return accident;
        } catch (Exception e) {
            throw new SaveNotSuccessException("Не удалось сохранить инцидент");
        }
    }

    @Override
    public void update(Accident accident, Set<Integer> rIds) {
        setTypeAndRules(accident);
        accidents.put(accident.getId(), accident);
    }

    @Override
    public Set<Rule> getRulesById(Set<Integer> rulesId) {
        return rulesId.stream()
                .map(rules::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<Accident> findById(int id) {
        return Optional.ofNullable(accidents.get(id));
    }

    public List<AccidentType> getAllTypes() {
        return types.values().stream().
                sorted(Comparator.comparingInt(AccidentType::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Rule> getAllRules() {
        return rules.values().stream()
                .sorted(Comparator.comparingInt(Rule::getId))
                .collect(Collectors.toList());
    }

    @Override
    public AccidentType getTypeById(int id) {
        return types.getOrDefault(id, new AccidentType(5, "Не указано"));
    }
}
