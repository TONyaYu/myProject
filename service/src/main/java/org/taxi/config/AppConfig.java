package org.taxi.config;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.taxi.util.HibernateUtil;

import java.lang.reflect.Proxy;

@Configuration
@ComponentScan("org.taxi")
public class AppConfig {

    public SessionFactory sessionFactory() {
        return HibernateUtil.buildSessionFactory();
    }

    public Session session(SessionFactory sessionFactory) {
        return (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args));
    }
}
