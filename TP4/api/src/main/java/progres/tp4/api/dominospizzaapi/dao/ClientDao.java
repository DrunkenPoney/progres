package progres.tp4.api.dominospizzaapi.dao;

import org.springframework.stereotype.Repository;
import progres.tp4.api.dominospizzaapi.bo.ClientBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IClientDao;

import javax.persistence.criteria.CriteriaQuery;

@Repository
public class ClientDao extends BaseDao<ClientBo> implements IClientDao {
	@Override
	protected Class<ClientBo> entity() {
		return ClientBo.class;
	}
	
	@Override
	public ClientBo get(Long id) {
		return getEntityManager().find(entity(), id);
	}
	
	@Override
	public ClientBo getByUsername(String username) {
		CriteriaQuery<ClientBo> criteria = query();
		criteria.where(getCriteriaBuilder().equal(
			criteria.from(entity()).get("username"), username));
		
		return getEntityManager().createQuery(criteria).getResultStream().findFirst().orElse(null);
	}
	
}
