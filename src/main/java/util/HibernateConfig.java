package util;
import org.springframework.context.annotation.*;import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;import javax.sql.DataSource;
import org.springframework.orm.hibernate5.*;import org.springframework.transaction.annotation.EnableTransactionManagement;import java.util.Properties;
/** Spring crée et ferme une seule SessionFactory et gère les transactions Hibernate. */
@Configuration @ComponentScan({"dao","metier"}) @EnableTransactionManagement(proxyTargetClass=true) @PropertySource("classpath:application.properties")
public class HibernateConfig {
    @Bean public DataSource dataSource(Environment env){
        DriverManagerDataSource ds=new DriverManagerDataSource();ds.setDriverClassName(env.getRequiredProperty("spring.datasource.driver-class-name"));
        ds.setUrl(env.getProperty("DB_URL",env.getRequiredProperty("spring.datasource.url")));ds.setUsername(env.getProperty("DB_USER",env.getRequiredProperty("spring.datasource.username")));ds.setPassword(env.getProperty("DB_PASSWORD",env.getRequiredProperty("spring.datasource.password")));return ds;
    }
    @Bean public LocalSessionFactoryBean sessionFactory(DataSource ds,Environment env){
        var factory=new LocalSessionFactoryBean();factory.setDataSource(ds);factory.setPackagesToScan("entities");Properties p=new Properties();
        p.setProperty("hibernate.dialect",env.getRequiredProperty("spring.jpa.properties.hibernate.dialect"));p.setProperty("hibernate.hbm2ddl.auto",env.getRequiredProperty("spring.jpa.hibernate.ddl-auto"));p.setProperty("hibernate.show_sql",env.getRequiredProperty("spring.jpa.show-sql"));p.setProperty("hibernate.format_sql",env.getRequiredProperty("spring.jpa.properties.hibernate.format_sql"));factory.setHibernateProperties(p);return factory;
    }
    @Bean public HibernateTransactionManager transactionManager(org.hibernate.SessionFactory factory){return new HibernateTransactionManager(factory);}
}
