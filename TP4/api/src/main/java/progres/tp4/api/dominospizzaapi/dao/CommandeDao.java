package progres.tp4.api.dominospizzaapi.dao;

import org.hibernate.criterion.CriteriaQuery;
import org.springframework.stereotype.Repository;
import progres.tp4.api.dominospizzaapi.bo.CommandeBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.ICommandeDao;

import java.util.List;

@Repository
public class CommandeDao extends BaseDao<CommandeBo> implements ICommandeDao {
	@Override
	protected Class<CommandeBo> entity() {
		return CommandeBo.class;
	}
}
