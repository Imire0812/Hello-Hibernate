package hibernate;

import hibernate.domain.Event;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class EntityManagerDemo {

    static EntityManager em;
    public static void main(String[] args) {
        // Получаем фабрику менеджеров сущностей
        EntityManagerFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
        // Из фабрики создаем EntityManager
        em = factory.createEntityManager();

        // Вызываем метод findAll и выводим все события на консоль
        List<Event> events = findAll();
        for (Event event : events) {
            System.out.println(event);
        }

        // Реализация метода findAll()
        static List<Event> findAll() {
            // Создаем запрос с использованием JPQL (Java Persistence Query Language)
            String jpql = "SELECT e FROM Event e";
            TypedQuery<Event> query = em.createQuery(jpql, Event.class);

            // Выполняем запрос и возвращаем результат
            List<Event> event = query.getResultList();
            return event;
        }

        Event event = findById(1);
        System.out.println(event);

        //Event newEvent = add(new Event("Rock Concert", "Berlin"));
        event = findById(3);
        System.out.println(event);

        //event.setCity("Prague");
        //event = update(event);
        //System.out.println(event);

        event.setName(null);
        event.setCity(null);
        delete(event);
    }

    static Event findById(int id) {
        return em.find(Event.class, id);
    }

    static Event add(Event event) {
        // Открываем транзакцию
        em.getTransaction().begin();
        // Create (сохраняем в базе данных, и благодаря этому сущность
        // становится управляемой Hibernate и заносится в контекст постоянства)
        em.persist(event);
        // Подтверждаем транзакцию
        em.getTransaction().commit();
        return event;
    }

    static Event update(Event event) {
        em.getTransaction().begin();
        em.merge(event);
        em.getTransaction().commit();
        return event;
    }

    static void delete(Event event) {
        em.getTransaction().begin();
        em.remove(event);
        em.getTransaction().commit();
    }
}