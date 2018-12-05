package progres.tp4.api.dominospizzaapi.dao;

import org.springframework.stereotype.Repository;
import progres.tp4.api.dominospizzaapi.bo.PizzaStateBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IPizzaStateDao;

import javax.persistence.criteria.CriteriaQuery;

@Repository
public class PizzaStateDao extends BaseDao<PizzaStateBo> implements IPizzaStateDao {
	@Override
	protected Class<PizzaStateBo> entity() {
		return PizzaStateBo.class;
	}
	
	@Override
	public PizzaStateBo getByKey(String key) {
		CriteriaQuery<PizzaStateBo> query = query();
		query.where(getCriteriaBuilder().equal(
			query.from(PizzaStateBo.class).get("key"), key));
		return getEntityManager().createQuery(query).getSingleResult();
	}
}
