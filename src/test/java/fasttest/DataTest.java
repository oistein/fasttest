package fasttest;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;

public class DataTest {

	protected SessionFactory sessionFactory;

	protected static SessionFactory createSessionFactory() {
		return new AnnotationConfiguration()
		.setProperty(Environment.DRIVER, org.h2.Driver.class.getName())
		.setProperty(Environment.URL, "jdbc:h2:mem:test")
		.setProperty(Environment.USER, "sa")
		.setProperty(Environment.PASS, "")
		.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
		.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread")
		.setProperty(Environment.HBM2DDL_AUTO, "create")
		.addAnnotatedClass(Person.class)
		.addAnnotatedClass(Address.class) 
		.buildSessionFactory();
	}

	protected static SessionFactory createInMemorySessionFactory() {
		return new InMemorySessionFactory();
		
	}

}
