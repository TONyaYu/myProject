package org.taxi.util;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;
import org.taxi.entity.Car;
import org.taxi.entity.Payment;
import org.taxi.entity.Review;
import org.taxi.entity.Ride;
import org.taxi.entity.User;
import org.taxi.entity.UserCar;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Car.class);
        configuration.addAnnotatedClass(Ride.class);
        configuration.addAnnotatedClass(Review.class);
        configuration.addAnnotatedClass(Payment.class);
        configuration.addAnnotatedClass(UserCar.class);

        configuration.configure();

        return configuration.buildSessionFactory();
    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(User.class);

        return configuration;
    }
}
