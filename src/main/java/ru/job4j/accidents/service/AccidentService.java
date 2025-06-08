package ru.job4j.accidents.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.repository.AccidentRepository;
import ru.job4j.accidents.repository.AccidentTypeRepository;
import ru.job4j.accidents.repository.RuleRepository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccidentService implements IAccidentService {
    private final AccidentRepository accidentRepository;
    private final RuleRepository ruleRepository;
    private final AccidentTypeRepository typeRepository;
    private static final int DEFAULT_TYPE_ID = 0;
    private static final int DEFAULT_RULE_ID = 0;

    @Override
    @Transactional(readOnly = true)
    public List<Accident> getAllAccidents() {
        return accidentRepository.findAllWithTypeAndRules();
    }

    @Override
    @Transactional
    public Accident create(Accident accident, Set<Integer> rIds) {
        validateFieldsAndSetTypeAndRules(accident, rIds);
        return accidentRepository.save(accident);
    }

    @Override
    @Transactional
    public void update(Accident accident, Set<Integer> rIds) {
        validateFieldsAndSetTypeAndRules(accident, rIds);
        accidentRepository.save(accident);
    }

    @Override
    @Transactional(readOnly = true)
    public Accident findById(int id) {
        return accidentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Авария с таким id не найдена"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccidentType> getAllTypes() {
        return (List<AccidentType>) typeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rule> getAllRules() {
        return (List<Rule>) ruleRepository.findAll();
    }

    @Override
    public void validateFields(Accident accident) {
        if (accident == null
                || !StringUtils.hasText(accident.getName())
                || !StringUtils.hasText(accident.getText())
                || !StringUtils.hasText(accident.getAddress())) {
            throw new IllegalArgumentException("Вы не заполнили необходимые поля инцидента");
        }
    }

    private void validateFieldsAndSetTypeAndRules(Accident accident, Set<Integer> rIds) {
        validateFields(accident);
        accident.setType(getTypeOrDefault(accident.getType()));
        accident.setRules(getRulesOrDefault(rIds));
    }

    private Set<Rule> getRulesOrDefault(Set<Integer> rIds) {
        if (rIds == null || rIds.isEmpty()) {
            return Set.of(ruleRepository.findById(DEFAULT_RULE_ID)
                    .orElseThrow(() -> new IllegalArgumentException("Правило не найдено")));
        } else {
            Set<Rule> rules = new LinkedHashSet<>();
            ruleRepository.findAllById(rIds).forEach(rules::add);
            return rules;
        }
    }

    private AccidentType getTypeOrDefault(AccidentType type) {
        int id = type != null ? type.getId() : DEFAULT_TYPE_ID;
        return typeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Тип аварии не найден: " + type));
    }
}
