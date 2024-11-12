package org.taxi.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.taxi.config.AppConfig;

import java.lang.reflect.Proxy;

public abstract class AbstractTestBase {

    protected static AnnotationConfigApplicationContext applicationContext;
    protected Session session;

    @BeforeAll
    static void createApplicationContext() {
        applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        }

    @BeforeEach
    void openSession(){
        session = applicationContext.getBean(Session.class);
        session.getTransaction().begin();
    }

    @AfterEach
    void closeSession(){
        session.getTransaction().rollback();
    }

    @AfterAll
    static void closeApplicationContext() {
        applicationContext.close();
    }
}