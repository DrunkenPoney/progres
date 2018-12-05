package progres.tp4.api.dominospizzaapi.dao;

import org.springframework.stereotype.Repository;
import progres.tp4.api.dominospizzaapi.bo.PizzaIngredientBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IPizzaIngredientDao;

@Repository
public class PizzaIngredientDao extends BaseDao<PizzaIngredientBo> implements IPizzaIngredientDao {
	@Override
	protected Class<PizzaIngredientBo> entity() {
		return PizzaIngredientBo.class;
	}
}
