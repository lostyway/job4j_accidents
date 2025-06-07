package ru.job4j.accidents.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccidentMem implements IAccidentRepository {
    private Map<Integer, Accident> accidents = new ConcurrentHashMap<>();

    @Override
    public List<Accident> getAllAccidents() {
        accidents.put(1, new Accident(1, "name", "text", "address"));
        accidents.put(2, new Accident(2, "name2", "text2", "address2"));
        accidents.put(3, new Accident(3, "name3", "text3", "address3"));
        accidents.put(4, new Accident(4, "name4", "text4", "address4"));
        return new ArrayList<>(accidents.values());
    }
}
