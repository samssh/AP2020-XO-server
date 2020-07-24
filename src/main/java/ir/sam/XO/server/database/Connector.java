package ir.sam.XO.server.database;

import ir.sam.XO.server.util.Loop;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Connector {
    private final static Object staticLock = new Object();
    private final static PrintStream logFilePrintStream;

    static {
        PrintStream temp;
        String logPath = String.format(".%ssrc%smain%sresources%slog%s", File.separator, File.separator
                , File.separator, File.separator, File.separator) + "hibernate log.txt";
        try {
            temp = new PrintStream(new File(logPath));
        } catch (FileNotFoundException e) {
            temp = System.err;
        }

        logFilePrintStream = temp;
    }

    private final SessionFactory sessionFactory;
    private final Set<SaveAble> save, delete, tempSave, tempDelete;
    private final Object lock;
    private final Loop worker;

    @SneakyThrows
    public Connector() {
        sessionFactory = buildSessionFactory();
        save = new HashSet<>();
        delete = new HashSet<>();
        tempDelete = new HashSet<>();
        tempSave = new HashSet<>();
        lock = new Object();
        worker = new Loop(30, this::persist);
        worker.start();
    }

    private SessionFactory buildSessionFactory() {
        PrintStream err = System.err;
        System.setErr(logFilePrintStream);
        ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        System.setErr(err);
        return sessionFactory;
    }

    private void persist() {
        synchronized (lock) {
            tempSave.addAll(save);
            this.save.clear();
            tempDelete.addAll(delete);
            this.delete.clear();
        }
        if (tempSave.size() > 0 || tempDelete.size() > 0)
            synchronized (staticLock) {
                Session session = sessionFactory.openSession();
                session.beginTransaction();
                for (SaveAble saveAble : tempDelete) {
                    session.delete(saveAble);

                }
                tempDelete.clear();
                for (SaveAble saveAble : tempSave) {
                    session.saveOrUpdate(saveAble);
                }
                tempSave.clear();
                session.getTransaction().commit();
                session.close();
            }
    }

    public void close() {
        worker.stop();
        sessionFactory.close();
    }

    public void save(SaveAble saveAble) {
        if (saveAble != null)
            synchronized (lock) {
                save.add(saveAble);
            }
    }

    public <E extends SaveAble> E fetch(Class<E> entity, Serializable id) {
        synchronized (staticLock) {
            Session session = sessionFactory.openSession();
            E result = session.get(entity, id);
            session.close();
            return result;
        }
    }


    public <E extends SaveAble> List<E> fetchAll(Class<E> entity) {
        String hql = "from " + entity.getName();
        return executeHQL(hql, entity);
    }

    public <E extends SaveAble> List<E> executeHQL(String hql, Class<E> entity) {
        synchronized (staticLock) {
            Session session = sessionFactory.openSession();
            List<E> result = session.createQuery(hql, entity).getResultList();
            session.close();
            return result;
        }
    }
}