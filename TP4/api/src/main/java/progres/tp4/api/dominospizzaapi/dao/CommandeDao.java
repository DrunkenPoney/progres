package progres.tp4.api.dominospizzaapi.dao;

import org.springframework.stereotype.Repository;
import progres.tp4.api.dominospizzaapi.bo.CommandeBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.ICommandeDao;

@Repository
public class CommandeDao extends BaseDao<CommandeBo> implements ICommandeDao {
	@Override
	protected Class<CommandeBo> entity() {
		return CommandeBo.class;
	}
	
	@Override
	public CommandeBo get(Long id) {
		return getEntityManager().find(entity(), id);
	}
}
