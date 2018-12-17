package progres.tp4.api.dominospizzaapi.dao.interfaces;

import progres.tp4.api.dominospizzaapi.bo.IngredientBo;

public interface IIngredientDao extends IBaseDao<IngredientBo> {
	IngredientBo getByKey(String key);
}
