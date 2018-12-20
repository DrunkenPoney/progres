package progres.tp4.api.dominospizzaapi.dao;

import org.springframework.stereotype.Repository;
import progres.tp4.api.dominospizzaapi.bo.IngredientBo;
import progres.tp4.api.dominospizzaapi.bo.PizzaSizeBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IPizzaSizeDao;

import javax.persistence.criteria.CriteriaQuery;

@Repository
public class PizzaSizeDao extends BaseDao<PizzaSizeBo> implements IPizzaSizeDao {
	@Override
	protected Class<PizzaSizeBo> entity() {
		return PizzaSizeBo.class;
	}
	
	@Override
	public PizzaSizeBo getByKey(String key) {
		CriteriaQuery<PizzaSizeBo> query = query();
		query.where(getCriteriaBuilder().equal(
			query.from(entity()).get("key"), key));
		return getEntityManager().createQuery(query).getResultStream().findFirst().orElse(null);
	}
}
