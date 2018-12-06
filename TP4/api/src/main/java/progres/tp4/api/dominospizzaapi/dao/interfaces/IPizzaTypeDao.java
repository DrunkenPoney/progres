package progres.tp4.api.dominospizzaapi.dao.interfaces;

import progres.tp4.api.dominospizzaapi.bo.PizzaTypeBo;

public interface IPizzaTypeDao extends IBaseDao<PizzaTypeBo> {
	PizzaTypeBo getByKey(String key);
}
