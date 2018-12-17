package progres.tp4.api.dominospizzaapi.dao.interfaces;

import progres.tp4.api.dominospizzaapi.bo.ClientBo;

public interface IClientDao extends IBaseDao<ClientBo> {
	ClientBo get(Long id);
	
	ClientBo getByUsername(String username);
}
