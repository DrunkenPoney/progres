package progres.tp4.api.dominospizzaapi.dao.interfaces;

import progres.tp4.api.dominospizzaapi.bo.PizzaStateBo;

public interface IPizzaStateDao extends IBaseDao<PizzaStateBo> {
	PizzaStateBo getByKey(String key);
}
