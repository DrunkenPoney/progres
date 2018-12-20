package progres.tp4.api.dominospizzaapi.dao;

import org.springframework.transaction.annotation.Transactional;
import progres.tp4.api.dominospizzaapi.bo.IBaseBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IBaseDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@SuppressWarnings("unused")
@Transactional
abstract class BaseDao<BO extends IBaseBo> implements IBaseDao<BO> {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	protected EntityManager getEntityManager() {
		return entityManager;
	}
	
	protected CriteriaBuilder getCriteriaBuilder() {
		return entityManager.getCriteriaBuilder();
	}
	
	protected abstract Class<BO> entity();
	
	protected CriteriaQuery<BO> select() {
		CriteriaQuery<BO> query = getCriteriaBuilder().createQuery(entity());
		return query.select(query.from(entity()));
	}
	
	protected CriteriaQuery<BO> query() {
		return getCriteriaBuilder().createQuery(entity());
	}
	
//	public BO get(Long id) {
//		return getEntityManager().find(entity(), id);
//	}
	
	@Override
	public List<BO> all() {
		return getEntityManager().createQuery(select()).getResultList();
	}
	
	@Override
	public void create(BO obj) {
		getEntityManager().persist(obj);
	}
	
	@Override
	public BO update(BO obj) {
		return getEntityManager().merge(obj);
	}
	
	@Override
	public void delete(BO obj) {
		getEntityManager().remove(obj);
	}
}
