package progres.tp4.api.dominospizzaapi.dao;

import org.springframework.stereotype.Repository;
import progres.tp4.api.dominospizzaapi.bo.ClientBo;
import progres.tp4.api.dominospizzaapi.bo.CommandeBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.ICommandeDao;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

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
	
	@Override
	public List<CommandeBo> byClient(ClientBo client) {
		CriteriaQuery<CommandeBo> query = query();
		query.where(getCriteriaBuilder().equal(query.from(entity()).get("client"), client));
		return getEntityManager().createQuery(query).getResultList();
	}
}
