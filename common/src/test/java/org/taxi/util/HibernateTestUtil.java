package org.taxi.util;

import lombok.experimental.UtilityClass;
import org.testcontainers.containers.PostgreSQLContainer;

@UtilityClass
public class HibernateTestUtil {

   private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

   static {
      postgres.start();
   }
}
