package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.exception.SaveNotSuccessException;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.sql.SQLException;
import java.util.*;

@Repository
@AllArgsConstructor
public class AccidentJdbcTemplate implements IAccidentRepository {
    private final JdbcTemplate jdbc;

    @Override
    public List<Accident> getAllAccidents() {
        Map<Integer, Accident> accidents = new LinkedHashMap<>();

        jdbc.query(
                """
                                    SELECT
                            a.id as accident_id,
                            a.name as accident_name,
                            a.description,
                            a.address,
                            t.id as type_id,
                            t.name as type_name,
                            r.id as rule_id,
                            r.name as rule_name
                        FROM accidents a
                        JOIN types t ON a.type_id = t.id
                        LEFT JOIN accident_rules ar ON a.id = ar.accident_id
                        LEFT JOIN rules r ON ar.rule_id = r.id
                        ORDER BY a.id, r.id
                        """,
                rs -> {
                    int id = rs.getInt("accident_id");

                    accidents.computeIfAbsent(id, accidentId -> {
                        try {
                            Accident acc = new Accident();
                            acc.setId(accidentId);
                            acc.setName(rs.getString("accident_name"));
                            acc.setText(rs.getString("description"));
                            acc.setAddress(rs.getString("address"));
                            acc.setType(new AccidentType(
                                    rs.getInt("type_id"),
                                    rs.getString("type_name")));
                            acc.setRules(new LinkedHashSet<>());
                            return acc;
                        } catch (SQLException e) {
                            throw new SaveNotSuccessException("Не удалось сохранить заявку");
                        }
                    });

                    int ruleId = rs.getInt("rule_id");
                    if (!rs.wasNull()) {
                        Rule rule = new Rule(ruleId, rs.getString("rule_name"));
                        accidents.get(id).getRules().add(rule);
                    }
                }
        );

        return new ArrayList<>(accidents.values());
    }

    @Override
    public Accident create(Accident accident) {
        setTypeAndRules(accident);
        Integer generatedId = jdbc.queryForObject(
                "insert into accidents (name, description, address, type_id) values (?,?,?,?) returning id",
                Integer.class, accident.getName(), accident.getText(), accident.getAddress(), accident.getType().getId());

        if (generatedId == null) {
            throw new SaveNotSuccessException("Не удалось получить ID новой записи. Сохранение неудачно.");
        }
        accident.setId(generatedId);

        refreshAllRulesInAccident(accident);
        return accident;
    }

    @Override
    public boolean update(Accident accident) {
        try {
            setTypeAndRules(accident);
            jdbc.update("update accidents set name =?, description = ?, address = ?, type_id = ? where id = ?",
                    accident.getName(), accident.getText(), accident.getAddress(), accident.getType().getId(), accident.getId());

            jdbc.update("delete from accident_rules where accident_id=?", accident.getId());

            refreshAllRulesInAccident(accident);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<AccidentType> getAllTypes() {
        return jdbc.query("select id, name from types",
                (rs, row) -> new AccidentType(rs.getInt("id"), rs.getString("name")));
    }

    @Override
    public Set<Rule> getRulesById(Set<Integer> rulesId) {
        Set<Rule> rules = new HashSet<>();

        if (rulesId == null || rulesId.isEmpty()) {
            rules.add(new Rule(0, "Не указано"));
            return rules;
        }

        String inSql = String.join(",", Collections.nCopies(rulesId.size(), "?"));
        rules.addAll(jdbc.query(
                "select id, name from rules where id in (" + inSql + ")",
                rulesId.toArray(),
                (rs, row) -> new Rule(rs.getInt("id"), rs.getString("name"))
        ));
        return rules;
    }

    @Override
    public Optional<Accident> findById(int id) {
        Accident accident = jdbc.queryForObject(
                """
                        select a.id, a.name, a.description, a.address, t.id as type_id, t.name as type_name
                        from accidents a
                        join types t on t.id = a.type_id
                        where a.id = ?
                        """,
                (rs, row) -> {
                    Accident acc = new Accident();
                    acc.setId(rs.getInt("id"));
                    acc.setName(rs.getString("name"));
                    acc.setText(rs.getString("description"));
                    acc.setAddress(rs.getString("address"));
                    acc.setType(new AccidentType(rs.getInt("type_id"), rs.getString("type_name")));
                    return acc;
                },
                id);

        List<Rule> rules = jdbc.query(
                "select r.id, r.name from rules r join accident_rules ar on r.id = ar.rule_id where ar.accident_id = ?"
                , (rs, row) -> new Rule(rs.getInt("id"), rs.getString("name")), id
        );

        accident.setRules(new LinkedHashSet<>(rules));
        return Optional.ofNullable(accident);
    }

    @Override
    public List<Rule> getAllRules() {
        return jdbc.query("select id, name from rules",
                (rs, row) -> new Rule(rs.getInt("id"), rs.getString("name")));
    }

    @Override
    public AccidentType getTypeById(int id) {
        return jdbc.queryForObject("select id, name from types where id = ?",
                (rs, row)
                        -> new AccidentType(rs.getInt("id"), rs.getString("name")), id);
    }

    private void refreshAllRulesInAccident(Accident accident) {
        for (Rule rule : accident.getRules()) {
            jdbc.update("insert into accident_rules (accident_id, rule_id) values (?,?)", accident.getId(), rule.getId());
        }
    }
}
