package ru.job4j.accidents.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccidentMem implements IAccidentRepository {
    private final Map<Integer, Accident> accidents = new ConcurrentHashMap<>();

    public AccidentMem() {
        accidents.put(1, new Accident(1, "name", "text", "address"));
        accidents.put(2, new Accident(2, "name2", "text2", "address2"));
        accidents.put(3, new Accident(3, "name3", "text3", "address3"));
        accidents.put(4, new Accident(4, "name4", "text4", "address4"));
    }

    @Override
    public List<Accident> getAllAccidents() {
        return new ArrayList<>(accidents.values());
    }

    @Override
    public Optional<Accident> create(Accident accident) {
        try {
            accidents.put(accident.getId(), accident);
            return Optional.of(accident);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean update(Accident accident) {
        return accidents.put(accident.getId(), accident) != null;
    }

    @Override
    public Optional<Accident> findById(int id) {
        return Optional.ofNullable(accidents.get(id));
    }
}
