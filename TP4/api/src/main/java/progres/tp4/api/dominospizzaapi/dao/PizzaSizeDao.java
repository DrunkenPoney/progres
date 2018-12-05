package progres.tp4.api.dominospizzaapi.dao;

import org.springframework.stereotype.Repository;
import progres.tp4.api.dominospizzaapi.bo.PizzaSizeBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IPizzaSizeDao;

@Repository
public class PizzaSizeDao extends BaseDao<PizzaSizeBo> implements IPizzaSizeDao {
	@Override
	protected Class<PizzaSizeBo> entity() {
		return PizzaSizeBo.class;
	}
}
