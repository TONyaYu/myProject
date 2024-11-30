package org.taxi.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.taxi.util.HibernateTestUtil;

@Import(AppConfig.class)
@TestPropertySource("/aplication-test.yml")
public class IntegrationTestConfiguration {

    @Bean
    public SessionFactory sessionFactory() {
        return HibernateTestUtil.buildSessionFactory();
    }
}
