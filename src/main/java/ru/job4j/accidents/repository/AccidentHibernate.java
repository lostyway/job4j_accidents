package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.utils.TransactionExecutor;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@AllArgsConstructor
public class AccidentHibernate implements IAccidentRepository {
    private static final int DEFAULT_TYPE_ID = 0;
    private static final int DEFAULT_RULE_ID = 0;
    private final TransactionExecutor executor;

    @Override
    public Accident create(Accident accident, Set<Integer> rIds) {
        return executor.execute(session -> {
            checkAndSetTypeAndRoles(accident, rIds, session);

            session.persist(accident);
            return accident;
        });
    }

    @Override
    public List<Accident> getAllAccidents() {
        return executor.execute(session ->
                session.createQuery(
                        "select distinct a from Accident a left join fetch a.type left join fetch a.rules order by a.id",
                        Accident.class).list());
    }

    @Override
    public void update(Accident accident, Set<Integer> rIds) {
        executor.executeVoid(session -> {
            checkAndSetTypeAndRoles(accident, rIds, session);
            session.merge(accident);
        });
    }

    @Override
    public Optional<Accident> findById(int id) {
        return executor.execute(session -> Optional.ofNullable(session.get(Accident.class, id)));
    }

    @Override
    public List<AccidentType> getAllTypes() {
        return executor.execute(session ->
                session.createQuery(
                        "from AccidentType order by id", AccidentType.class).list());
    }

    @Override
    public List<Rule> getAllRules() {
        return executor.execute(session -> session.createQuery("from Rule order by id", Rule.class).list());
    }

    @Override
    public AccidentType getTypeById(int id) {
        return executor.execute(session -> session.get(AccidentType.class, id));
    }

    @Override
    public Set<Rule> getRulesById(Set<Integer> rulesId) {
        return new LinkedHashSet<>(executor.execute(session ->
                session.createQuery("from Rule where id in :ids order by id", Rule.class)
                        .setParameter("ids", rulesId)
                        .list()
        ));
    }

    private void checkAndSetTypeAndRoles(Accident accident, Set<Integer> rIds, Session session) {
        accident.setType(getTypeOrDefault(accident.getType(), session));
        accident.setRules(getRulesOrDefault(rIds, session));
    }

    private Set<Rule> getRulesOrDefault(Set<Integer> rIds, Session session) {
        return rIds == null
                ? Set.of(session.get(Rule.class, DEFAULT_RULE_ID))
                : new LinkedHashSet<>(
                session.createQuery("from Rule where id in :ids order by id", Rule.class)
                        .setParameter("ids", rIds)
                        .list());

    }

    private AccidentType getTypeOrDefault(AccidentType type, Session session) {
        int id = type != null ? type.getId() : DEFAULT_TYPE_ID;
        AccidentType result = session.get(AccidentType.class, id);
        if (result == null) {
            throw new IllegalArgumentException("Тип аварии не найден: id = " + id);
        }
        return result;
    }
}


