package uk.co.bbc.fabric.interfaces.dao.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

/**
 * Useful for DAOs that are not intended for use with Entities e.g. stored proc only DAOs.
 * 
 */
@Transactional
public abstract class HibernateEntitylessDao extends HibernateDaoSupport {

 /**
  * Override {@link HibernateTemplate} creation to set {@link HibernateTemplate#setAllowCreate(boolean)} to false. This will cause Hibernate to refuse to
  * create a new transaction if one is not already configured. This prevents connections being created by accident or not being closed promptly, and thus
  * emptying the connection pool.
  * 
  * @see https://jira.dev.bbc.co.uk/browse/TAF-1804
  */
 @Override
 public HibernateTemplate createHibernateTemplate(SessionFactory sessionFactory) {
  HibernateTemplate template = super.createHibernateTemplate(sessionFactory);
  template.setAllowCreate(false);
  return template;
 }

 @Autowired
 // Method is called storeSessionFactory(SessionFactory) because super.setSessionFactory(SessionFactory) is final, and so cannot be overridden
 public void storeSessionFactory(SessionFactory sessionFactory) {
  super.setSessionFactory(sessionFactory);
 }

}
