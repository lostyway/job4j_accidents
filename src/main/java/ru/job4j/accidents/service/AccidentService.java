package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.repository.AccidentHibernate;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class AccidentService implements IAccidentService {
    private final AccidentHibernate accidentRepository;

    @Override
    public List<Accident> getAllAccidents() {
        return accidentRepository.getAllAccidents();
    }

    @Override
    public Accident create(Accident accident, Set<Integer> rIds) {
        validateFields(accident, rIds);
        return accidentRepository.create(accident, rIds);
    }

    @Override
    public void update(Accident accident, Set<Integer> rIds) {
        try {
            accidentRepository.update(accident, rIds);
        } catch (Exception e) {
            throw new IllegalArgumentException("Не удалось обновить инцидент");
        }
    }

    @Override
    public Accident findById(int id) {
        return accidentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Авария с таким id не найдена"));
    }

    @Override
    public List<AccidentType> getAllTypes() {
        return accidentRepository.getAllTypes();
    }

    @Override
    public List<Rule> getAllRules() {
        return accidentRepository.getAllRules();
    }

    @Override
    public void validateFields(Accident accident, Set<Integer> rIds) {
        if (accident == null || accident.getName() == null || accident.getText() == null || accident.getAddress() == null) {
            throw new IllegalArgumentException("Вы не заполнили необходимые поля инцидента");
        }
    }
}
