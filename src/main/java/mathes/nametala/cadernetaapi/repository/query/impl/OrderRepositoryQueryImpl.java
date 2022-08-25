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
import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;
import mathes.nametala.cadernetaapi.model.entitys.OrderEntity;
import mathes.nametala.cadernetaapi.model.entitys.ProductEntity;
import mathes.nametala.cadernetaapi.repository.filter.OrderFilter;
import mathes.nametala.cadernetaapi.repository.query.OrderRepositoryQuery;

public class OrderRepositoryQueryImpl implements OrderRepositoryQuery{

	@Autowired
	private EntityManager entityManager;
	
	@Override
	public Page<OrderEntity> filter(Pageable pageable, OrderFilter orderFilter) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OrderEntity> criteriaQuery = criteriaBuilder.createQuery(OrderEntity.class);
		Root<OrderEntity> root = criteriaQuery.from(OrderEntity.class);
		
		List<Predicate> predicateList = new ArrayList<Predicate>();
		addPredicates(predicateList,orderFilter,criteriaBuilder,root);
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		criteriaQuery.where(predicates);
		
		TypedQuery<OrderEntity> query = entityManager.createQuery(criteriaQuery);
		
		addPageRestrictions(query, pageable);
		
		return new PageImpl<OrderEntity>(query.getResultList(),pageable,total(orderFilter));
	}


	private void addPageRestrictions(TypedQuery<OrderEntity> query, Pageable pageable) {
		query.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		
	}


	private long total(OrderFilter orderFilter) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<OrderEntity> root = criteriaQuery.from(OrderEntity.class);
		
		List<Predicate> predicateList = new ArrayList<Predicate>();
		addPredicates(predicateList, orderFilter, criteriaBuilder, root);
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		
		criteriaQuery.select(criteriaBuilder.count(root));
		
		return entityManager.createQuery(criteriaQuery).getSingleResult();
	}




	private void addPredicates(List<Predicate> predicateList, OrderFilter orderFilter,
			CriteriaBuilder criteriaBuilder, Root<OrderEntity> root) {
		
		if(orderFilter.getProductsId()!=null) {
			Join<OrderEntity, ProductEntity> join = root.join("products");
			Path<ProductEntity> productId = join.get("id");
			predicateList.add(
					criteriaBuilder.isTrue(productId.in(orderFilter.getProductsId())));
			
		}
		if(orderFilter.getAccountId()!=null) {
			Join<OrderEntity, AccountEntity> join = root.join("account");
			Path<AccountEntity> accountId = join.get("id");
			predicateList.add(
					criteriaBuilder.isTrue(accountId.in(orderFilter.getAccountId())));
			
		}
		
		if(orderFilter.getCustomerId()!=null) {
			Join<OrderEntity, CustomerEntity> join = root.join("customer");
			Path<CustomerEntity> customerId = join.get("id");
			predicateList.add(
					criteriaBuilder.isTrue(customerId.in(orderFilter.getCustomerId())));
		}
			
		if(orderFilter.getMinTotal()!=null) 
			predicateList.add(
				criteriaBuilder.greaterThanOrEqualTo(root.get("total"), orderFilter.getMinTotal()));
		
		if(orderFilter.getMaxTotal()!=null)
			predicateList.add(
				criteriaBuilder.lessThanOrEqualTo(root.get("total"), orderFilter.getMaxTotal()));
			
		if(orderFilter.getMinCreatedOn()!=null)
			predicateList.add(
				criteriaBuilder.greaterThanOrEqualTo(root.get("createdOn"), orderFilter.getMinCreatedOn()));
			
		if(orderFilter.getMaxCreatedOn()!=null)
			predicateList.add(
				criteriaBuilder.lessThanOrEqualTo(root.get("createdOn"), orderFilter.getMaxCreatedOn()));
		
		if(orderFilter.getPaid()!=null)
			predicateList.add(
				criteriaBuilder.equal(root.get("paid"), orderFilter.getPaid()));		
		
	}

}
