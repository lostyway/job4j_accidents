package ru.job4j.accidents.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@RequiredArgsConstructor
@Setter
@Getter
public class AccidentMem implements IAccidentRepository {
    private final Map<Integer, Accident> accidents = new ConcurrentHashMap<>();

    private AtomicInteger accidentId = new AtomicInteger(1);

    @Override
    public List<Accident> getAllAccidents() {
        return new ArrayList<>(accidents.values());
    }

    @Override
    public Optional<Accident> create(Accident accident) {
        try {
            accident.setId(accidentId.getAndIncrement());
            accidents.put(accident.getId(), accident);
            return Optional.of(accident);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean update(Accident accident) {
        accidents.put(accident.getId(), accident);
        return true;
    }

    @Override
    public Optional<Accident> findById(int id) {
        return Optional.ofNullable(accidents.get(id));
    }
}
