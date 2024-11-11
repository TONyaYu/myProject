package org.taxi.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.taxi.util.HibernateUtil;

@Import(AppConfig.class)
public class IntegrationTestConfiguration {

    @Bean
    public SessionFactory sessionFactory() {
        return HibernateUtil.buildSessionFactory();
    }
}
