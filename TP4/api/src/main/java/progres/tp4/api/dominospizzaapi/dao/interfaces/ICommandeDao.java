package progres.tp4.api.dominospizzaapi.dao.interfaces;

import progres.tp4.api.dominospizzaapi.bo.ClientBo;
import progres.tp4.api.dominospizzaapi.bo.CommandeBo;

import java.util.List;

public interface ICommandeDao extends IBaseDao<CommandeBo> {
	CommandeBo get(Long id);
	
	List<CommandeBo> byClient(ClientBo client);
}
