package com.library.daoimpl;

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
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.library.dao.BookDAO;
import com.library.model.custom.displayDto.DeletedBookDto;
import com.library.model.custom.displayDto.MostBorrowed;
import com.library.model.custom.insertDto.BookDto;
import com.library.model.custom.insertDto.bookSearch.BookSearchDto;
import com.library.model.entity.BookMaintenance;
import com.library.model.entity.BookTransaction;
import com.library.util.WebUtil;

@Repository
public class BookDAOImpl implements BookDAO 
{
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Integer addBook(BookMaintenance bookMaintenance)
	{
		return (Integer) sessionFactory.getCurrentSession().save(bookMaintenance);
	}
	
	@Override
	public BookMaintenance getBookById(Integer bookId) 
	{
		return (BookMaintenance) sessionFactory.getCurrentSession().get(BookMaintenance.class, bookId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> searchBooksBySearchValue(BookSearchDto bookSearchDto) 
	{
	    Criteria criteria = sessionFactory.getCurrentSession().createCriteria(BookMaintenance.class);
	    
	    criteria.setProjection(Projections.rowCount());
	    Long totalRecords = (Long) criteria.uniqueResult();
	    
	    if (bookSearchDto.getSearchBy() != null && bookSearchDto.getSearchValue() != null && ! bookSearchDto.getSearchValue().strip().isEmpty())
		{
			if (bookSearchDto.getSearchBy().equals("title") && bookSearchDto.getSearchValue().strip() != null)
			{
				criteria.add(Restrictions.ilike("title", bookSearchDto.getSearchValue().strip(), MatchMode.ANYWHERE));
			}
			else if (bookSearchDto.getSearchBy().equals("bookId") && bookSearchDto.getSearchValue().strip() != null)
			{
				criteria.add(Restrictions.eq("bookId", Integer.parseInt(bookSearchDto.getSearchValue().strip())));
			}
			
		}
	    
	    if (bookSearchDto.getFilter() != null)
	    {
	        if (bookSearchDto.getFilter().getAuthor() != null && !bookSearchDto.getFilter().getAuthor().isEmpty())
	        {
	            criteria.add(Restrictions.eq("author", bookSearchDto.getFilter().getAuthor()));
	        }

	        if (bookSearchDto.getFilter().getLanguage() != null && !bookSearchDto.getFilter().getLanguage().isEmpty()) 
	        {
	            criteria.add(Restrictions.eq("language", bookSearchDto.getFilter().getLanguage()));
	        }

	        if (bookSearchDto.getFilter().getStatus() != null && ! bookSearchDto.getFilter().getStatus().isEmpty()) 
	        {
	            criteria.add(Restrictions.eq("bookStatus", bookSearchDto.getFilter().getStatus()));
	        }
	        
	        if (bookSearchDto.getFilter().getFromDate() != null && bookSearchDto.getFilter().getToDate() != null) 
	        {
				if (bookSearchDto.getFilter().getFromDate().equals(bookSearchDto.getFilter().getToDate()))
				{
					criteria.add(Restrictions.sqlRestriction("DATE(BOMA_Book_Registered_Date) = ?",
							bookSearchDto.getFilter().getFromDate(), StandardBasicTypes.DATE));
				}
				else 
				{
					 criteria.add(Restrictions.between("registeredDate", bookSearchDto.getFilter().getFromDate(), 
							 bookSearchDto.getFilter().getToDate()));
				}
	        }
	    }
	    
	    if (bookSearchDto.getOrder() != null) 
	    {
	    	if(bookSearchDto.getOrder().getColumn() != null && bookSearchDto.getOrder().getType() != null)
	    	{
	    		if (bookSearchDto.getOrder().getColumn().equalsIgnoreCase("title")) 
	    		{
	 	            if (bookSearchDto.getOrder().getType().equalsIgnoreCase("asc"))
	 	            {
	 	                criteria.addOrder(Order.asc("title"));
	 	            }
	 	            else
	 	            {
	 	                criteria.addOrder(Order.desc("title"));
	 	            }
	 	        }
	    		else if (bookSearchDto.getOrder().getColumn().equalsIgnoreCase("language")) 
	    		{
	 	            if (bookSearchDto.getOrder().getType().equalsIgnoreCase("asc"))
	 	            {
	 	                criteria.addOrder(Order.asc("language"));
	 	            } 
	 	            else 
	 	            {
	 	                criteria.addOrder(Order.desc("language"));
	 	            }
	 	        } 
	    		else if (bookSearchDto.getOrder().getColumn().equalsIgnoreCase("author"))
	    		{
	 	            if (bookSearchDto.getOrder().getType().equalsIgnoreCase("asc")) 
	 	            {
	 	                criteria.addOrder(Order.asc("author"));
	 	            }
	 	            else
	 	            {
	 	                criteria.addOrder(Order.desc("author"));
	 	            }
	 	        }
	    	}
	    	else
	    	{
	    		criteria.addOrder(Order.asc("title"));
	    	}
	    }	    
	    else
	    {
	    	criteria.addOrder(Order.asc("title"));
	    }

	    criteria.setProjection(Projections.rowCount());
	    Long filteredRecords = (Long) criteria.uniqueResult();

	    criteria.setFirstResult(bookSearchDto.getStart())
	            .setMaxResults(bookSearchDto.getLimit());

	    criteria.setProjection(Projections.projectionList()
	            .add(Projections.property("bookId"), "bookId")
	            .add(Projections.property("title"), "title")
	            .add(Projections.property("author"), "author")
	            .add(Projections.property("language"), "language")
	            .add(Projections.property("stockCount"), "stockCount")
	            .add(Projections.property("bookStatus"), "bookStatus")
	            .add(Projections.property("registeredDate"), "registeredDate")
	            .add(Projections.property("invalidFlag"), "invalidFlag")
	            .add(Projections.property("deletedDate"), "deletedDate"));

	    List<BookDto> resultList = criteria.setResultTransformer(Transformers.aliasToBean(DeletedBookDto.class)).list();
	    Map<String, Object> result = new HashMap<>();
	    result.put("totalRecords", totalRecords);
	    result.put("filteredRecords", filteredRecords);
	    result.put("data", resultList);
	    return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MostBorrowed> getMostBorrowedBooks(Integer memberId, Integer start, Integer limit)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(BookTransaction.class)
	            .createAlias("bookMaintenance", "book")
				.createAlias("membershipMaintenance", "member")
				.add(Restrictions.ne("book.bookStatus", "Deleted"));		 
		
	    if(memberId != null)
	    {
	    	criteria.add(Restrictions.eq("member.memberId", memberId));
	    }	    
	    
		criteria.setProjection(Projections.projectionList()
			    		.add(Projections.groupProperty("book.bookId"), "bookId")
			    		.add(Projections.property("book.title"), "title")
			            .add(Projections.property("book.author"), "author")
			            .add(Projections.property("book.language"), "language")
			            .add(Projections.property("book.bookStatus"), "bookStatus")
			            .add(Projections.property("book.stockCount"), "stockCount")
			            .add(Projections.property("book.borrowedCount"), "borrowedCount")
			            .add(Projections.property("book.registeredDate"), "registeredDate")
			            .add(Projections.count("book.bookId"), "totalBorrowed"))
						.addOrder(Order.desc("totalBorrowed"))
						.setFirstResult(start)
	    				.setMaxResults(limit);		
		    
	    return criteria.setResultTransformer(Transformers.aliasToBean(MostBorrowed.class)).list();
	}

	@Override
	public Integer isDuplicateBook(String author, String title, String language)
	{
		return (Integer) sessionFactory.getCurrentSession().createCriteria(BookMaintenance.class)
				.add(Restrictions.isNull("deletedDate"))
				.add(Restrictions.eq("invalidFlag", WebUtil.INVALID_NO))
				.add(Restrictions.eq("author", author))
				.add(Restrictions.eq("title", title))
				.add(Restrictions.eq("language", language))
				.setProjection(Projections.property("bookId"))
				.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getBookDetailsById(Integer bookId)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(BookMaintenance.class)
				.add(Restrictions.eq("invalidFlag", WebUtil.INVALID_NO))
				.add(Restrictions.isNull("deletedDate"));
		criteria.setProjection(Projections.rowCount());
		Long totalRecords = (Long) criteria.uniqueResult();  
		if(bookId != null)
		{
			criteria.add(Restrictions.eq("bookId", bookId));
		}		
		criteria.setProjection(Projections.rowCount());
		Long filteredRecords = (Long) criteria.uniqueResult();  
		
		criteria.setProjection(Projections.projectionList()
					.add(Projections.property("bookId"), "bookId")
					.add(Projections.property("author"), "author")
					.add(Projections.property("title"), "title")
					.add(Projections.property("language"), "language")
					.add(Projections.property("bookStatus"), "bookStatus")
					.add(Projections.property("stockCount"), "stockCount")
					.add(Projections.property("borrowedCount"), "borrowedCount")
					.add(Projections.property("registeredDate"), "registeredDate"));
		
		List<BookDto> booksList = criteria.setResultTransformer(Transformers.aliasToBean(BookDto.class)).list();	
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("totalRecords", totalRecords);
		result.put("filteredRecords", filteredRecords);
		result.put("data", booksList);
		return result;
	}
}
