package progres.tp4.api.dominospizzaapi.dao.interfaces;

import progres.tp4.api.dominospizzaapi.bo.CommandeBo;

public interface ICommandeDao extends IBaseDao<CommandeBo> {
	CommandeBo get(Long id);
}
