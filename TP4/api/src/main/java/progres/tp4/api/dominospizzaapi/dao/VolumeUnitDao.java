package progres.tp4.api.dominospizzaapi.dao;

import org.springframework.stereotype.Repository;
import progres.tp4.api.dominospizzaapi.bo.VolumeUnitBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IVolumeUnitDao;

import javax.persistence.criteria.CriteriaQuery;

@Repository
public class VolumeUnitDao extends BaseDao<VolumeUnitBo> implements IVolumeUnitDao {
	@Override
	protected Class<VolumeUnitBo> entity() {
		return VolumeUnitBo.class;
	}
	
	@Override
	public VolumeUnitBo getByKey(String key) {
		CriteriaQuery<VolumeUnitBo> query = query();
		query.where(getCriteriaBuilder().equal(
			query.from(entity()).get("key"), key));
		return getEntityManager().createQuery(query).getResultStream().findFirst().orElse(null);
	}
}
