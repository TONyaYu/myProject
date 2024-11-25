package org.taxi.util;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.taxi.config.AppConfig;

public abstract class AbstractTestBase {

    protected static AnnotationConfigApplicationContext applicationContext;
    protected Session session;

    @BeforeAll
    static void createApplicationContext() {
        applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        }

    @BeforeEach
    void beginTransaction(){
        session = applicationContext.getBean(Session.class);
        session.getTransaction().begin();
    }

    @AfterEach
    void closeTransaction(){
        session.getTransaction().rollback();
    }

    @AfterAll
    static void closeApplicationContext() {
        applicationContext.close();
    }
}
