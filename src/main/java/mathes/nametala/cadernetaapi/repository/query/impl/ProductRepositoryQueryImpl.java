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

import mathes.nametala.cadernetaapi.model.entitys.ProductEntity;
import mathes.nametala.cadernetaapi.repository.filter.ProductFilter;
import mathes.nametala.cadernetaapi.repository.query.ProductRepositoryQuery;

public class ProductRepositoryQueryImpl implements ProductRepositoryQuery{

	@Autowired
	EntityManager entityManager;
	
	@Override
	public Page<ProductEntity> filter(Pageable pageable, ProductFilter productFilter) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductEntity> criteriaQuery = criteriaBuilder.createQuery(ProductEntity.class);
		Root<ProductEntity> root = criteriaQuery.from(ProductEntity.class);
		
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		addPredicates(predicatesList,productFilter,criteriaBuilder,root);
		
		Predicate[] predicates = new Predicate[predicatesList.size()];
		predicatesList.toArray(predicates);
		criteriaQuery.where(predicates);
		
		TypedQuery<ProductEntity> query = entityManager.createQuery(criteriaQuery);
		
		addPageRestrictions(query, pageable);
		
		return new PageImpl<ProductEntity>(query.getResultList(),pageable,total(productFilter));
	}

	private long total(ProductFilter productFilter) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<ProductEntity> root = criteriaQuery.from(ProductEntity.class);
		
		List<Predicate> predicateList = new ArrayList<>();
		addPredicates(predicateList, productFilter, criteriaBuilder, root);
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		
		criteriaQuery.where(predicates);
		criteriaQuery.select(criteriaBuilder.count(root));
		
		return entityManager.createQuery(criteriaQuery).getSingleResult();
	}

	private void addPageRestrictions(TypedQuery<ProductEntity> query, Pageable pageable) {
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		
	}

	private void addPredicates(List<Predicate> predicatesList, ProductFilter productFilter,
			CriteriaBuilder criteriaBuilder, Root<ProductEntity> root) {
		
		if(productFilter.getName()!=null)
			predicatesList.add(
					criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),"%" + productFilter.getName() + "%"));
		
		if(productFilter.getMinValue()!=null) 
			predicatesList.add(
					criteriaBuilder.greaterThanOrEqualTo(root.get("value"), productFilter.getMinValue()));
		if(productFilter.getMaxValue()!=null)
			predicatesList.add(
					criteriaBuilder.lessThanOrEqualTo(root.get("value"), productFilter.getMaxValue()));
		if(productFilter.getMinCreatedOn()!=null)
			predicatesList.add(
					criteriaBuilder.greaterThanOrEqualTo(root.get("createdOn"), productFilter.getMinCreatedOn()));
		if(productFilter.getMaxCreatedOn()!=null)
			predicatesList.add(
					criteriaBuilder.lessThanOrEqualTo(root.get("createdOn"), productFilter.getMaxCreatedOn()));
		
	}

}
