import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.aop.support.AopUtils;
import util.HibernateConfig;
import metier.ProductDaoImpl;
import entities.Product;
import org.hibernate.SessionFactory;

class SpringHibernateTest {
  static AnnotationConfigApplicationContext context;
  static ProductDaoImpl dao;

  @BeforeAll
  static void init() {
    context = new AnnotationConfigApplicationContext(HibernateConfig.class);
    dao = context.getBean(ProductDaoImpl.class);
  }

  @AfterAll
  static void close() {
    context.close();
    com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
  }

  @Test
  void infrastructureSingletonEtProxy() {
    assertTrue(AopUtils.isAopProxy(dao));
    assertSame(context.getBean(SessionFactory.class), context.getBean(SessionFactory.class));
    try (var s = context.getBean(SessionFactory.class).openSession()) {
      s.doWork(c -> assertEquals("MySQL", c.getMetaData().getDatabaseProductName()));
    }
  }

  @Test
  void crudComplet() {
    Product p = new Product("CRUD-" + System.nanoTime(), 100);
    assertTrue(dao.create(p));
    assertEquals(100, dao.findById(p.getId()).getPrice());
    p.setPrice(200);
    assertTrue(dao.update(p));
    assertEquals(200, dao.findById(p.getId()).getPrice());
    assertTrue(dao.findAll().stream().anyMatch(x -> x.getId() == p.getId()));
    assertTrue(dao.delete(p));
    assertNull(dao.findById(p.getId()));
  }

  @Test
  void rollbackLotEnCasDeDoublon() {
    String name = "Unique-" + System.nanoTime();
    Product existing = new Product(name, 10);
    dao.create(existing);
    int before = dao.findAll().size();
    assertThrows(
        RuntimeException.class,
        () ->
            dao.createBatch(new Product("Annulé-" + System.nanoTime(), 20), new Product(name, 30)));
    assertEquals(before, dao.findAll().size());
    Product after = new Product("Après rollback-" + System.nanoTime(), 40);
    assertTrue(dao.create(after));
    dao.delete(after);
    dao.delete(existing);
  }
}
