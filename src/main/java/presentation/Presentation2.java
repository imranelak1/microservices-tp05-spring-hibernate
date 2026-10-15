package presentation;

import util.HibernateConfig;
import entities.Product;
import metier.ProductDaoImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** Démonstration du CRUD complet, réalisée via les beans transactionnels. */
public class Presentation2 {
  public static void main(String[] args) {
    try (var c = new AnnotationConfigApplicationContext(HibernateConfig.class)) {
      var dao = c.getBean(ProductDaoImpl.class);
      Product p = new Product("Produit 1-" + System.currentTimeMillis(), 100);
      dao.create(p);
      System.out.println("Produit sauvegardé : " + dao.findById(p.getId()));
      p.setPrice(120);
      dao.update(p);
      System.out.println("Produits après modification : " + dao.findAll());
      dao.delete(p);
      System.out.println("Produit supprimé : " + (dao.findById(p.getId()) == null));
    } finally {
      com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
    }
  }
}
