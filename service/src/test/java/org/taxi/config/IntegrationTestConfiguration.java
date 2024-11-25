package org.taxi.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.taxi.util.HibernateTestUtil;

@Import(AppConfig.class)
public class IntegrationTestConfiguration {

    @Bean
    public SessionFactory sessionFactory() {
        return HibernateTestUtil.buildSessionFactory();
    }
}
