package metier;
import dao.IDao;import entities.Product;import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;import org.springframework.transaction.annotation.Transactional;import java.util.List;
/** Toutes les opérations passent par le proxy transactionnel Spring. */
@Repository @Transactional
public class ProductDaoImpl implements IDao<Product> {
    private final SessionFactory sessionFactory;
    public ProductDaoImpl(SessionFactory sessionFactory){this.sessionFactory=sessionFactory;}
    public boolean create(Product product){sessionFactory.getCurrentSession().save(product);return true;}
    public boolean delete(Product product){var s=sessionFactory.getCurrentSession();s.delete(s.merge(product));return true;}
    public boolean update(Product product){sessionFactory.getCurrentSession().merge(product);return true;}
    @Transactional(readOnly=true) public Product findById(int id){return sessionFactory.getCurrentSession().get(Product.class,id);}
    @Transactional(readOnly=true) public List<Product> findAll(){return sessionFactory.getCurrentSession().createQuery("from Product p order by p.id",Product.class).list();}
    /** Un doublon fait échouer le lot entier : le proxy annule la transaction. */
    public void createBatch(Product first,Product second){var s=sessionFactory.getCurrentSession();s.save(first);s.save(second);s.flush();}
}
