package mathes.nametala.cadernetaapi.repository.query.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;
import mathes.nametala.cadernetaapi.model.entitys.RoleEntity;
import mathes.nametala.cadernetaapi.repository.filter.AccountFilter;
import mathes.nametala.cadernetaapi.repository.query.AccountRepositoryQuery;

public class AccountRepositoryQueryImpl implements AccountRepositoryQuery {

	@Autowired
	EntityManager entityManager;

	@Override
	public Page<AccountEntity> filter(AccountFilter accountFilter, Pageable pageable) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AccountEntity> criteriaQuery = criteriaBuilder.createQuery(AccountEntity.class);
		Root<AccountEntity> root = criteriaQuery.from(AccountEntity.class);

		List<Predicate> predicateList = new ArrayList<Predicate>();

		addPredicates(predicateList, accountFilter, criteriaBuilder, root);

		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		criteriaQuery.where(predicates);
		criteriaQuery.distinct(true);

		TypedQuery<AccountEntity> query = entityManager.createQuery(criteriaQuery);

		addPageRestrictions(query, pageable);

		return new PageImpl<AccountEntity>(query.getResultList(), pageable, total(accountFilter));

	}

	private void addPageRestrictions(TypedQuery<AccountEntity> query, Pageable pageable) {

		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());

	}

	private long total(AccountFilter accountFilter) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<AccountEntity> root = criteriaQuery.from(AccountEntity.class);

		List<Predicate> predicateList = new ArrayList<Predicate>();

		addPredicates(predicateList, accountFilter, criteriaBuilder, root);

		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		
		criteriaQuery.where(predicates);
		criteriaQuery.distinct(true);
		criteriaQuery.select(criteriaBuilder.countDistinct(root));
		
		return entityManager.createQuery(criteriaQuery).getSingleResult();

	}

	private void addPredicates(List<Predicate> predicateList, AccountFilter accountFilter, CriteriaBuilder criteriaBuilder,
			Root<AccountEntity> root) {

		if (accountFilter.getUsername() != null)
			predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("username")),
					"%" + accountFilter.getUsername() + "%"));

		if (accountFilter.getRoles() != null) {

			Join<AccountEntity, RoleEntity> join = root.join("roles");
			Path<Long> roleId = join.get("id");
			Predicate predRoles = criteriaBuilder.isTrue(roleId.in(accountFilter.getRoles()));

			predicateList.add(predRoles);
		}
	}

}
