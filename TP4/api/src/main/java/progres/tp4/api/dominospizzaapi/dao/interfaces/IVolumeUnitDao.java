package progres.tp4.api.dominospizzaapi.dao.interfaces;

import progres.tp4.api.dominospizzaapi.bo.VolumeUnitBo;

public interface IVolumeUnitDao extends IBaseDao<VolumeUnitBo> {
	VolumeUnitBo getByKey(String key);
}
