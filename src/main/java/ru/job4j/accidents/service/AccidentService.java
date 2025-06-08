package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.repository.AccidentJdbcTemplate;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class AccidentService implements IAccidentService {
    private final AccidentJdbcTemplate accidentJdbcTemplate;

    @Override
    public List<Accident> getAllAccidents() {
        return accidentJdbcTemplate.getAllAccidents();
    }

    @Override
    public Accident create(Accident accident) {
        return accidentJdbcTemplate.create(accident);
    }

    @Override
    public boolean update(Accident accident) {
        boolean isUpdated = accidentJdbcTemplate.update(accident);
        if (!isUpdated) {
            throw new IllegalArgumentException("Не удалось обновить инцидент");
        }
        return true;
    }

    @Override
    public Accident findById(int id) {
        return accidentJdbcTemplate.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Авария с таким id не найдена"));
    }

    @Override
    public List<AccidentType> getAllTypes() {
        return accidentJdbcTemplate.getAllTypes();
    }

    @Override
    public List<Rule> getAllRules() {
        return accidentJdbcTemplate.getAllRules();
    }

    @Override
    public Set<Rule> getRulesById(Set<Integer> rulesId) {
        return accidentJdbcTemplate.getRulesById(rulesId);
    }

    @Override
    public Set<Rule> processSelectedRules(Set<Integer> ruleIds) {
        if (ruleIds == null || ruleIds.isEmpty()
                || (ruleIds.size() == 1 && ruleIds.contains(0))) {

            return Set.of(new Rule(0, "Не указано"));
        }

        return getRulesById(ruleIds);
    }

    @Override
    public void validateAndSetFields(Accident accident, Set<Integer> rIds) {
        if (accident == null || accident.getName() == null || accident.getText() == null || accident.getAddress() == null) {
            throw new IllegalArgumentException("Вы не заполнили необходимые поля инцидента");
        }
        Set<Rule> rules = processSelectedRules(rIds);
        accident.setRules(rules);
    }
}
