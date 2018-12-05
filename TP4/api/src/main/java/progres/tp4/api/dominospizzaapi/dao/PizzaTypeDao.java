package progres.tp4.api.dominospizzaapi.dao;

import org.springframework.stereotype.Repository;
import progres.tp4.api.dominospizzaapi.bo.PizzaTypeBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IPizzaTypeDao;

@Repository
public class PizzaTypeDao extends BaseDao<PizzaTypeBo> implements IPizzaTypeDao {
	@Override
	protected Class<PizzaTypeBo> entity() {
		return PizzaTypeBo.class;
	}
}
