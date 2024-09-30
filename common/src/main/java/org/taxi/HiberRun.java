package org.taxi;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.taxi.entity.Car;
import org.taxi.entity.User;
import org.taxi.entity.UserRole;
import org.taxi.util.HibernateUtil;

@Slf4j
public class HiberRun {
    public static void main(String[] args) {
        Car car = Car.builder()
                .model("Skoda")
                .make("Karoq")
                .isAvailable(true)
                .licensePlate("125dss138")
                .build();
        User driver = User.builder()
                .email("max@gmail.com")
                .firstName("Max")
                .lastName("Ivanov")
                .userRole(UserRole.DRIVER)
                .build();

        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            try(session) {
                Transaction transaction = session.beginTransaction();

                session.save(driver);

                session.getTransaction().commit();
            }

        }
    }
}
