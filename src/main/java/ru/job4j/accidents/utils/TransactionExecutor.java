package ru.job4j.accidents.utils;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class TransactionExecutor {
    private final SessionFactory sf;

    public void executeVoid (Consumer<Session> command) {
        Transaction tx = null;
        Session session = null;
        try {
            session = sf.openSession();
            tx = session.beginTransaction();
            command.accept(session);
            tx.commit();
        } catch (Exception e) {
            rollback(tx);
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public <T> T execute (Function<Session, T> command) {
        Transaction tx = null;
        Session session = null;
        try {
            session = sf.openSession();
            tx = session.beginTransaction();
            T result = command.apply(session);
            tx.commit();
            return result;
        } catch (Exception e) {
            rollback(tx);
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    private void rollback (Transaction tx) {
        if (tx != null) {
            tx.rollback();
        }
    }
}
