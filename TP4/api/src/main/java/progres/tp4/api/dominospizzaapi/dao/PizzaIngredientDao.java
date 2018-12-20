package progres.tp4.api.dominospizzaapi.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import progres.tp4.api.dominospizzaapi.bo.IngredientBo;
import progres.tp4.api.dominospizzaapi.bo.PizzaIngredientBo;
import progres.tp4.api.dominospizzaapi.bo.PizzaIngredientBo.PKPizzaIngredient;
import progres.tp4.api.dominospizzaapi.bo.PizzaTypeBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IIngredientDao;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IPizzaIngredientDao;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IPizzaTypeDao;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IVolumeUnitDao;

@Repository
public class PizzaIngredientDao extends BaseDao<PizzaIngredientBo> implements IPizzaIngredientDao {
	
	@Autowired
	private IIngredientDao ingredientDao;
	
	@Autowired
	private IPizzaTypeDao pizzaTypeDao;
	
	@Override
	protected Class<PizzaIngredientBo> entity() {
		return PizzaIngredientBo.class;
	}
	
	@Override
	public PizzaIngredientBo get(String keyType, String keyIngredient) {
		IngredientBo ingredient = new IngredientBo();
		ingredient.setKey(keyIngredient);
		PizzaTypeBo type = new PizzaTypeBo();
		type.setKey(keyIngredient);
		return getEntityManager().find(entity(), new PKPizzaIngredient(ingredient, type));
	}
	
	@Override
	public void create(PizzaIngredientBo obj) {
		obj.setIngredient(ingredientDao.getByKey(obj.getIngredient().getKey()));
		obj.setType(pizzaTypeDao.getByKey(obj.getType().getKey()));
		super.create(obj);
	}
}
