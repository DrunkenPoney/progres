package progres.tp4.api.dominospizzaapi.dao;

import org.springframework.stereotype.Repository;
import progres.tp4.api.dominospizzaapi.bo.PizzaTypeBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IPizzaTypeDao;

import javax.persistence.criteria.CriteriaQuery;

@Repository
public class PizzaTypeDao extends BaseDao<PizzaTypeBo> implements IPizzaTypeDao {
	@Override
	protected Class<PizzaTypeBo> entity() {
		return PizzaTypeBo.class;
	}
	
	@Override
	public PizzaTypeBo getByKey(String key) {
		CriteriaQuery<PizzaTypeBo> query = query();
		query.where(getCriteriaBuilder().equal(
			query.from(entity()).get("key"), key));
		return getEntityManager().createQuery(query).getSingleResult();
	}
}
