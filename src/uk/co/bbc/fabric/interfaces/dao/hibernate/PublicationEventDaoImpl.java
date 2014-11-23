package uk.co.bbc.fabric.interfaces.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Service;

import uk.co.bbc.fabric.interfaces.entity.OnAirEntity;

@Service
public class PublicationEventDaoImpl extends HibernateEntitylessDao implements PublicationEventDao {

	public Object find(String sql){
		System.out.println("sql: "+sql);
		
		List l =getHibernateTemplate().find(sql);
		System.out.println("List"+l);
		
		if(l!=null && l.size()>0){
			for (int i = 0; i < l.size(); i++) {
				OnAirEntity onAirEntity =(OnAirEntity) l.get(i);
				System.out.println(onAirEntity.getErrorStacktrace());
				
			}
		}
		
		return null;
	}
	

}
