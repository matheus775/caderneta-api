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

import mathes.nametala.cadernetaapi.model.entitys.ProductEntity;
import mathes.nametala.cadernetaapi.model.entitys.RecordEntity;
import mathes.nametala.cadernetaapi.repository.filter.RecordFilter;
import mathes.nametala.cadernetaapi.repository.query.RecordRepositoryQuery;

public class RecordRepositoryQueryImpl implements RecordRepositoryQuery{

	@Autowired
	private EntityManager entityManager;
	
	@Override
	public Page<RecordEntity> filter(Pageable pageable, RecordFilter recordFilter) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RecordEntity> criteriaQuery = criteriaBuilder.createQuery(RecordEntity.class);
		Root<RecordEntity> root = criteriaQuery.from(RecordEntity.class);
		
		List<Predicate> predicateList = new ArrayList<Predicate>();
		addPredicates(predicateList,recordFilter,criteriaBuilder,root);
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		criteriaQuery.where(predicates);
		
		TypedQuery<RecordEntity> query = entityManager.createQuery(criteriaQuery);
		
		addPageRestrictions(query, pageable);
		
		return new PageImpl<RecordEntity>(query.getResultList(),pageable,total(recordFilter));
	}


	private void addPageRestrictions(TypedQuery<RecordEntity> query, Pageable pageable) {
		query.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		
	}


	private long total(RecordFilter recordFilter) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<RecordEntity> root = criteriaQuery.from(RecordEntity.class);
		
		List<Predicate> predicateList = new ArrayList<Predicate>();
		addPredicates(predicateList, recordFilter, criteriaBuilder, root);
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		
		criteriaQuery.select(criteriaBuilder.count(root));
		
		return entityManager.createQuery(criteriaQuery).getSingleResult();
	}




	private void addPredicates(List<Predicate> predicateList, RecordFilter recordFilter,
			CriteriaBuilder criteriaBuilder, Root<RecordEntity> root) {
		
		if(recordFilter.getProductsId()!=null) {
			Join<RecordEntity, ProductEntity> join = root.join("products");
			Path<ProductEntity> productId = join.get("id");
			predicateList.add(
					criteriaBuilder.isTrue(productId.in(recordFilter.getProductsId())));
			
		}
		
	}

}
