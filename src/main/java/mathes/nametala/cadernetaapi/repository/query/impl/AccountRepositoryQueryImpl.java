package mathes.nametala.cadernetaapi.repository.query.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;
import mathes.nametala.cadernetaapi.repository.query.AccountRepositoryQuery;

public class AccountRepositoryQueryImpl implements AccountRepositoryQuery{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Page<AccountEntity> filter(Pageable pageable) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AccountEntity> criteriaQuery = criteriaBuilder.createQuery(AccountEntity.class);
		Root<AccountEntity> root = criteriaQuery.from(AccountEntity.class);
				
		Predicate[] predicates= createRestrictions(criteriaBuilder,root);
		criteriaQuery.where(predicates);
		
		TypedQuery<AccountEntity> query = entityManager.createQuery(criteriaQuery);
		
		createPageableRestrictions(query,pageable);
		
		return new PageImpl<>(query.getResultList(), pageable,query.getResultList().size());
	}

	private void createPageableRestrictions(TypedQuery<AccountEntity> query, Pageable pageable) {
		int pageNumber = pageable.getPageNumber();
		int pageSize = pageable.getPageSize();
		int firstResult = pageNumber * pageSize;
		query.setFirstResult(firstResult);
		query.setMaxResults(pageSize);
		
	}

	private Predicate[] createRestrictions(CriteriaBuilder criteriaBuilder, Root<AccountEntity> root) {
		List<Predicate> predicates = new ArrayList<>();
		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
