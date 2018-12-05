package progres.tp4.api.dominospizzaapi.dao.interfaces;

import progres.tp4.api.dominospizzaapi.bo.BaseBo;

import java.util.List;

public interface IBaseDao<BO extends BaseBo> {
	BO get(Long id);
	
	Long create(BO obj);
	
	BO update(BO obj);
	
	void delete(Long id);
	
	List<BO> all();
}
