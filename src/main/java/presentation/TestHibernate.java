package presentation;

import util.HibernateConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.HibernateTransactionManager;

/** Vérifie la présence des deux composants d'infrastructure du TP. */
public class TestHibernate {
  public static void main(String[] args) {
    try (var c = new AnnotationConfigApplicationContext(HibernateConfig.class)) {
      System.out.println(
          "SessionFactory is configured correctly: " + c.getBean(SessionFactory.class));
      System.out.println(
          "Transaction Manager is configured correctly: "
              + c.getBean(HibernateTransactionManager.class));
    } finally {
      com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
    }
  }
}
