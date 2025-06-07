package ru.job4j.accidents.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.AccidentMem;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccidentService implements IAccidentService {
    private final AccidentMem accidentMem;

    @Override
    public List<Accident> getAllAccidents() {
        return accidentMem.getAllAccidents();
    }

    @Override
    public Accident create(Accident accident) {
        return accidentMem.create(accident)
                .orElseThrow(() -> new IllegalArgumentException("Не удалось создать инцидент"));
    }

    @Override
    public boolean update(Accident accident) {
       return accidentMem.update(accident);
    }

    @Override
    public Accident findById(int id) {
        return accidentMem.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Авария с таким id не найдена"));
    }
}
