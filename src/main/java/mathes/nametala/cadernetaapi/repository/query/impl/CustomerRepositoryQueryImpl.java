package mathes.nametala.cadernetaapi.repository.query.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;
import mathes.nametala.cadernetaapi.repository.filter.CustomerFilter;
import mathes.nametala.cadernetaapi.repository.query.CustomerRepositoryQuery;

public class CustomerRepositoryQueryImpl implements CustomerRepositoryQuery{

	@Autowired
	EntityManager entityManager;
	
	@Override
	public Page<CustomerEntity> filter(Pageable pageable, CustomerFilter customerFilter) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CustomerEntity> criteriaQuery = criteriaBuilder.createQuery(CustomerEntity.class);
		Root<CustomerEntity> root = criteriaQuery.from(CustomerEntity.class);
		
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		addPredicates(predicatesList,customerFilter,criteriaBuilder,root);
		
		Predicate[] predicates = new Predicate[predicatesList.size()];
		predicatesList.toArray(predicates);
		criteriaQuery.where(predicates);
		
		TypedQuery<CustomerEntity> query = entityManager.createQuery(criteriaQuery);
		
		addPageRestrictions(query, pageable);
		
		return new PageImpl<CustomerEntity>(query.getResultList(),pageable,total(customerFilter));
	}

	private long total(CustomerFilter customerFilter) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<CustomerEntity> root = criteriaQuery.from(CustomerEntity.class);
		
		List<Predicate> predicateList = new ArrayList<>();
		addPredicates(predicateList, customerFilter, criteriaBuilder, root);
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		
		criteriaQuery.where(predicates);
		criteriaQuery.select(criteriaBuilder.count(root));
		
		return entityManager.createQuery(criteriaQuery).getSingleResult();
	}

	private void addPageRestrictions(TypedQuery<CustomerEntity> query, Pageable pageable) {
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
	}

	private void addPredicates(List<Predicate> predicatesList, CustomerFilter customerFilter,
			CriteriaBuilder criteriaBuilder, Root<CustomerEntity> root) {
		
		if(customerFilter.getName() != null)
			predicatesList.add(
					criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" +customerFilter.getName() + "%"));

		if(customerFilter.getEmail() != null)
			predicatesList.add(
					criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" +customerFilter.getEmail() + "%"));

		if(customerFilter.getAdress() != null)
			predicatesList.add(
					criteriaBuilder.like(criteriaBuilder.lower(root.get("adress")), "%" +customerFilter.getAdress() + "%"));
		
	}

}
