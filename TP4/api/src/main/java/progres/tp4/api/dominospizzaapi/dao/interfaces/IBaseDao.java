package progres.tp4.api.dominospizzaapi.dao.interfaces;

import progres.tp4.api.dominospizzaapi.bo.IBaseBo;

import java.util.List;

public interface IBaseDao<BO extends IBaseBo> {
	void create(BO obj);
	
	BO update(BO obj);
	
	void delete(BO id);
	
	List<BO> all();
}
