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
}
