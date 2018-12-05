package progres.tp4.api.dominospizzaapi.dao;

import org.springframework.stereotype.Repository;
import progres.tp4.api.dominospizzaapi.bo.ClientBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IClientDao;

@Repository
public class ClientDao extends BaseDao<ClientBo> implements IClientDao {
	@Override
	protected Class<ClientBo> entity() {
		return ClientBo.class;
	}
}
