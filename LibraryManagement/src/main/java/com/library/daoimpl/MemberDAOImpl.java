package com.library.daoimpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.library.dao.MemberDAO;
import com.library.model.custom.insertDto.MemberDto;
import com.library.model.custom.insertDto.memberSearch.MemberSearchDto;
import com.library.model.entity.BookTransaction;
import com.library.model.entity.MembershipMaintenance;
import com.library.util.WebUtil;

@Repository
public class MemberDAOImpl implements MemberDAO
{	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Integer addMember(MembershipMaintenance membershipMaintenance) 
	{
		return (Integer) sessionFactory.getCurrentSession().save(membershipMaintenance);
	}
	
	@Override
	public void updateMember(MembershipMaintenance member)
	{
		sessionFactory.getCurrentSession().update(member);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getMembersBySearchAndFilter(MemberSearchDto memberSearchDto) 
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MembershipMaintenance.class);
		
		criteria.setProjection(Projections.rowCount());
		Long totalRecords = (Long) criteria.uniqueResult();
		
		if (memberSearchDto.getSearchBy() != null && memberSearchDto.getSearchValue() != null)
		{
	        if (memberSearchDto.getSearchBy().equalsIgnoreCase("name"))
	        {
	            criteria.add(Restrictions.ilike("firstName", memberSearchDto.getSearchValue().strip(), MatchMode.ANYWHERE))
	                    .add(Restrictions.ilike("lastName", memberSearchDto.getSearchValue().strip(), MatchMode.ANYWHERE));
	        }
	        else if (memberSearchDto.getSearchBy().equalsIgnoreCase("memberId"))
	        {
	            criteria.add(Restrictions.eq("memberId", Integer.parseInt(memberSearchDto.getSearchValue().strip())));
	        }
	        else if (memberSearchDto.getSearchBy().equalsIgnoreCase("phoneNumber"))
	        {
	            criteria.add(Restrictions.eq("mobileNumber", memberSearchDto.getSearchValue().trim()));
	        }
	        else if (memberSearchDto.getSearchBy().equalsIgnoreCase("email"))
	        {
	            criteria.add(Restrictions.eq("email", memberSearchDto.getSearchValue().trim()));
	        }
	    }
	    
	    if (memberSearchDto.getFilter() != null)
	    {
	        if (memberSearchDto.getFilter().getWorkStatus() != null) 
	        {
	            criteria.add(Restrictions.eq("workStatus", memberSearchDto.getFilter().getWorkStatus().strip()));
	        }
	        
	        if (memberSearchDto.getFilter().getMembershipStatus() != null)
	        {
	            criteria.add(Restrictions.eq("membershipStatus", memberSearchDto.getFilter().getMembershipStatus().strip()));
	        }
	        
	        if (memberSearchDto.getFilter().getGender() != null)
	        {
	            criteria.add(Restrictions.eq("gender", memberSearchDto.getFilter().getGender()));
	        }
	    }
	    
	    if (memberSearchDto.getMinAge() != null && memberSearchDto.getMaxAge() != null)
	    {
	        criteria.add(Restrictions.between("age", memberSearchDto.getMinAge(), memberSearchDto.getMaxAge()));
	    }
	    else if (memberSearchDto.getMinAge() != null)
	    {
	        criteria.add(Restrictions.ge("age", memberSearchDto.getMinAge()));
	    }
	    else if (memberSearchDto.getMaxAge() != null)
	    {
	        criteria.add(Restrictions.le("age", memberSearchDto.getMaxAge()));
	    }

	    if(memberSearchDto.getOrder() != null)
	    {
	    	 if (memberSearchDto.getOrder().getColumn().equalsIgnoreCase("name"))
	 	    {
	 	        if (memberSearchDto.getOrder().getType().equalsIgnoreCase("asc"))
	 	        {
	 	            criteria.addOrder(Order.asc("firstName")).addOrder(Order.asc("lastName"));
	 	        } 
	 	        else
	 	        {
	 	            criteria.addOrder(Order.desc("firstName")).addOrder(Order.desc("lastName"));
	 	        }
	 	    }
	 	    
	 	    else if (memberSearchDto.getOrder().getColumn().equalsIgnoreCase("age"))
	 	    {
	 	        if (memberSearchDto.getOrder().getType().equalsIgnoreCase("asc")) 
	 	        {
	 	            criteria.addOrder(Order.asc("age"));
	 	        }
	 	        else
	 	        {
	 	            criteria.addOrder(Order.desc("age"));
	 	        } 
	 	    } 
	 	    
	 	    else if (memberSearchDto.getOrder().getColumn().equalsIgnoreCase("memberId"))
	 	    {
	 	        if (memberSearchDto.getOrder().getType().equalsIgnoreCase("asc")) 
	 	        {
	 	            criteria.addOrder(Order.asc("memberId"));
	 	        } 
	 	        else
	 	        {
	 	            criteria.addOrder(Order.desc("memberId"));
	 	        }
	 	    }
	    }	   
	    
	    criteria.setProjection(Projections.rowCount());
	    Long filteredRecords = (Long) criteria.uniqueResult();    
	    
	    criteria.setFirstResult(memberSearchDto.getStart()).setMaxResults(memberSearchDto.getLimit());
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("memberId"), "memberId")
				.add(Projections.property("firstName"), "firstName")
				.add(Projections.property("lastName"), "lastName")
				.add(Projections.property("age"), "age")
				.add(Projections.property("gender"), "gender")
				.add(Projections.property("addressLine1"), "addressLine1")
				.add(Projections.property("addressLine2"), "addressLine2")
				.add(Projections.property("mobileNumber"), "mobileNumber")
				.add(Projections.property("email"), "email")
				.add(Projections.property("workStatus"), "workStatus")
				.add(Projections.property("membershipStartDate"), "membershipStartDate")
				.add(Projections.property("membershipEndDate"), "membershipEndDate")
				.add(Projections.property("membershipStatus"), "membershipStatus"));
		
		List<MemberDto> membersList = criteria.setResultTransformer(Transformers.aliasToBean(MemberDto.class)).list();	
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("totalRecords", totalRecords);
		result.put("filteredRecords", filteredRecords);
		result.put("data", membersList);
		return result;
	}

	@Override
	public MembershipMaintenance getMemberById(Integer memberId)
	{
		return (MembershipMaintenance) sessionFactory.getCurrentSession().get(MembershipMaintenance.class, memberId);
	}

	@Override
	public boolean getActiveTransactionsForMember(Integer memberId)
	{
	    Criteria criteria = sessionFactory.getCurrentSession().createCriteria(BookTransaction.class)
	            .add(Restrictions.eq("membershipMaintenance.memberId", memberId))
	            .add(Restrictions.eq("rentalStatus", WebUtil.BOOK_STATUS_BORROWED))
	            .setProjection(Projections.rowCount());
	    return !(criteria.uniqueResult() == null);
	}
	
	@Override
	public boolean isDuplicateMember(String email)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MembershipMaintenance.class)
									.add(Restrictions.eq("email", email));
		return !(criteria.uniqueResult() == null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MembershipMaintenance> getMembershipExpiredMembers(Date currentDate)
	{
		return sessionFactory.getCurrentSession().createCriteria(MembershipMaintenance.class)
					.add(Restrictions.lt("membershipEndDate", currentDate)).list();	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getMemberDetailsById(Integer memberId) 
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MembershipMaintenance.class);
		criteria.setProjection(Projections.rowCount());
		Long totalRecords = (Long) criteria.uniqueResult();

		if(memberId != null)
		{
			criteria.add(Restrictions.eq("memberId", memberId));
		}
		
		criteria.setProjection(Projections.rowCount());
		Long filteredRecords = (Long) criteria.uniqueResult();   
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("memberId"), "memberId")
				.add(Projections.property("firstName"), "firstName")
				.add(Projections.property("lastName"), "lastName")
				.add(Projections.property("age"), "age")
				.add(Projections.property("gender"), "gender")
				.add(Projections.property("addressLine1"), "addressLine1")
				.add(Projections.property("addressLine2"), "addressLine2")
				.add(Projections.property("mobileNumber"), "mobileNumber")
				.add(Projections.property("email"), "email")
				.add(Projections.property("workStatus"), "workStatus")
				.add(Projections.property("membershipStartDate"), "membershipStartDate")
				.add(Projections.property("membershipEndDate"), "membershipEndDate")
				.add(Projections.property("membershipStatus"), "membershipStatus"));		
		List<MemberDto> membersList = criteria.setResultTransformer(Transformers.aliasToBean(MemberDto.class)).list();	
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("totalRecords", totalRecords);
		result.put("filteredRecords", filteredRecords);
		result.put("data", membersList);
		return result;
	}
}
