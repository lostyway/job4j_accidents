package ru.job4j.accidents.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.accidents.model.Rule;

public interface RuleRepository extends CrudRepository<Rule, Integer> {
}
