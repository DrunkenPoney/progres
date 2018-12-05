package progres.tp4.api.dominospizzaapi.dao;

import org.springframework.transaction.annotation.Transactional;
import progres.tp4.api.dominospizzaapi.bo.BaseBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IBaseDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@SuppressWarnings("unused")
abstract class BaseDao<BO extends BaseBo> implements IBaseDao<BO> {
	
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
	
	@Override
	public BO get(Long id) {
		return getEntityManager().find(entity(), id);
	}
	
	@Override
	public List<BO> all() {
		return getEntityManager().createQuery(select()).getResultList();
	}
	
	@Override
	@Transactional
	public Long create(BO obj) {
		getEntityManager().persist(obj);
		return obj.getId();
	}
	
	@Override
	@Transactional
	public BO update(BO obj) {
		return getEntityManager().merge(obj);
	}
	
	@Override
	@Transactional
	public void delete(Long id) {
		getEntityManager().remove(get(id));
	}
}
