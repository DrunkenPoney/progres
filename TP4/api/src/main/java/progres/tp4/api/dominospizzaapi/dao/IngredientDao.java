package progres.tp4.api.dominospizzaapi.dao;

import org.springframework.stereotype.Repository;
import progres.tp4.api.dominospizzaapi.bo.IngredientBo;
import progres.tp4.api.dominospizzaapi.bo.VolumeUnitBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IIngredientDao;

import javax.persistence.criteria.CriteriaQuery;

@Repository
public class IngredientDao extends BaseDao<IngredientBo> implements IIngredientDao {
	@Override
	protected Class<IngredientBo> entity() {
		return IngredientBo.class;
	}
	
	@Override
	public IngredientBo getByKey(String key) {
		CriteriaQuery<IngredientBo> query = query();
		query.where(getCriteriaBuilder().equal(
			query.from(IngredientBo.class).get("key"), key));
		return getEntityManager().createQuery(query).getSingleResult();
	}
}
