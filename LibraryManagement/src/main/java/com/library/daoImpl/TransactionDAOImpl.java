package com.library.daoImpl;

import java.util.ArrayList;
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
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.library.dao.TransactionDAO;
import com.library.model.custom.displayDto.BookInfo;
import com.library.model.custom.displayDto.MemberTopAuthors;
import com.library.model.custom.displayDto.RetrivingDto;
import com.library.model.custom.insertDto.transactionSearch.TransactionSearchDto;
import com.library.model.entity.BookMaintenance;
import com.library.model.entity.BookTransaction;
import com.library.util.WebUtil;

@Repository
public class TransactionDAOImpl implements TransactionDAO
{
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Integer checkoutBook(BookTransaction bookTransaction)
	{
		return (Integer) sessionFactory.getCurrentSession().save(bookTransaction);
	}
	
	@Override
	public BookTransaction getTransactionsById(Integer transactionId)
	{
		return (BookTransaction) sessionFactory.getCurrentSession().get(BookTransaction.class, transactionId);            
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MemberTopAuthors> getTopAuthorsForAllMembers(Integer id)
	{
	    Criteria criteria = sessionFactory.getCurrentSession().createCriteria(BookTransaction.class)
	            .createAlias("bookMaintenance", "book").createAlias("membershipMaintenance", "member")
	        	.add(Restrictions.eq("member.memberId", id))
	            .setProjection(Projections.projectionList()
	                    .add(Projections.groupProperty("member.memberId"), "memberId")
	                    .add(Projections.groupProperty("book.author"), "author")
	                    .add(Projections.count("book.author").as("borrowedCount")))
	            .addOrder(Order.asc("memberId"))
	            .addOrder(Order.desc("borrowedCount"))
	            .setResultTransformer(Transformers.aliasToBean(MemberTopAuthors.class))
	            .setMaxResults(3);

	    List<MemberTopAuthors> topAuthors = criteria.list();

	    for (MemberTopAuthors topAuthor : topAuthors)
	    {
	        List<BookMaintenance> booksInfo = fetchBooksInfoByAuthor(topAuthor.getAuthor());

	        List<BookInfo> bookInfoList = new ArrayList<>();

	        for (BookMaintenance book : booksInfo)
	        {
	            BookInfo bookInfo = new BookInfo();
	            bookInfo.setTitle(book.getTitle());
	            bookInfo.setLanguage(book.getLanguage());
	            bookInfo.setBookStatus(book.getBookStatus());
	            bookInfoList.add(bookInfo);
	        }
	        topAuthor.setBookInfo(bookInfoList);
	    }
	    return topAuthors;
	}

	@SuppressWarnings("unchecked")
	public List<BookMaintenance> fetchBooksInfoByAuthor(String author)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(BookMaintenance.class);
		criteria.add(Restrictions.eq("author", author));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RetrivingDto> getNotReturnedTransactionsAndSendMail(Integer memberId)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(BookTransaction.class)
				.createAlias("membershipMaintenance", "member").createAlias("bookMaintenance", "book");		
		
		criteria.add(Restrictions.eq("rentalStatus", WebUtil.BOOK_STATUS_BORROWED))
				.add(Restrictions.gt("borrowedCount", WebUtil.DEFAULT_INT_VALUE))
				.add(Restrictions.lt("returnDueDate", new Date()));
		
		if(memberId != null)
		{
			criteria.add(Restrictions.eq("member.memberId", memberId));
		}
		criteria.setProjection(Projections.projectionList()
					.add(Projections.property("member.firstName"), "firstName")
					.add(Projections.property("member.lastName"), "lastName")
					.add(Projections.property("member.email"), "email")
					.add(Projections.property("book.bookId"), "bookId")
					.add(Projections.property("book.title"), "title")
					.add(Projections.property("book.author"), "author")
					.add(Projections.property("book.language"), "language")
					.add(Projections.property("borrowedCount"), "borrowedCount")
					.add(Projections.property("returnedCount"), "returnedCount"));
		return criteria.setResultTransformer(Transformers.aliasToBean(RetrivingDto.class)).list();		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getTransactionsBySearchAndFilter(TransactionSearchDto transactionSearchDto) 
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(BookTransaction.class)
										.createAlias("membershipMaintenance", "member")
										.createAlias("bookMaintenance", "book");
		
		criteria.setProjection(Projections.rowCount());
		Long totalRecords = (Long) criteria.uniqueResult();
		
		if (transactionSearchDto.getSearchBy() != null && transactionSearchDto.getSearchValue() != null && 
				! transactionSearchDto.getSearchValue().strip().isEmpty())
		{
			if (transactionSearchDto.getSearchBy().equals("memberName"))
			{
				criteria.add(Restrictions.disjunction(Restrictions.ilike("member.firstName", transactionSearchDto.getSearchValue().strip(), MatchMode.ANYWHERE), 
						Restrictions.ilike("member.lastName", transactionSearchDto.getSearchValue(), MatchMode.ANYWHERE)));
			}
			else if (transactionSearchDto.getSearchBy().equals("memberId"))
			{
				criteria.add(Restrictions.eq("member.memberId", Integer.parseInt(transactionSearchDto.getSearchValue().strip())));
			}
			else if (transactionSearchDto.getSearchBy().equals("bookTitle"))
			{
				criteria.add(Restrictions.ilike("book.title", transactionSearchDto.getSearchValue().strip(), MatchMode.ANYWHERE));
			}
		}
		if(transactionSearchDto.getFilter() != null)
		{
			if(transactionSearchDto.getFilter().getRentalStatus() != null 
					&& ! transactionSearchDto.getFilter().getRentalStatus().strip().isEmpty())
			{
				if((transactionSearchDto.getFilter().getRentalStatus().equalsIgnoreCase("Borrowed")))
				{
					if(transactionSearchDto.getFilter().getFromDate() != null 
							&& transactionSearchDto.getFilter().getToDate() != null)
					{
						criteria.add(Restrictions.eq("rentalStatus",  WebUtil.BOOK_STATUS_BORROWED))
								.add(Restrictions.between("borrowedDate", transactionSearchDto.getFilter().getFromDate(),
										transactionSearchDto.getFilter().getToDate()));
					}
					else
					{
						criteria.add(Restrictions.eq("rentalStatus",  WebUtil.BOOK_STATUS_BORROWED))
						.add(Restrictions.le("borrowedDate", new Date()));	
					}								
				}
				
				if((transactionSearchDto.getFilter().getRentalStatus().equalsIgnoreCase("Not Returned")))
				{
					if(transactionSearchDto.getFilter().getFromDate() != null 
							&& transactionSearchDto.getFilter().getToDate() != null)
					{
						criteria.add(Restrictions.eq("rentalStatus",  WebUtil.BOOK_STATUS_BORROWED))
								.add(Restrictions.between("returnDueDate", transactionSearchDto.getFilter().getFromDate(),
										transactionSearchDto.getFilter().getToDate()));
					}
					else
					{
						criteria.add(Restrictions.eq("rentalStatus", WebUtil.BOOK_STATUS_BORROWED))
								.add(Restrictions.lt("returnDueDate", new Date()));
					}								
				}
				
				if((transactionSearchDto.getFilter().getRentalStatus().equalsIgnoreCase("Returned")))
				{
					if(transactionSearchDto.getFilter().getFromDate() != null 
							&& transactionSearchDto.getFilter().getToDate() != null)
					{
						criteria.add(Restrictions.eq("rentalStatus",  WebUtil.BOOK_STATUS_RETURNED))
								.add(Restrictions.between("actualReturnedDate", transactionSearchDto.getFilter().getFromDate(),
										transactionSearchDto.getFilter().getToDate()));
					}
					else
					{
						criteria.add(Restrictions.eq("rentalStatus", WebUtil.BOOK_STATUS_RETURNED))
								.add(Restrictions.lt("actualReturnedDate", new Date()));
					}								
				}
			}
			else if (transactionSearchDto.getFilter().getFromDate() != null
					&& transactionSearchDto.getFilter().getToDate() != null) 
			{		    
			    if (transactionSearchDto.getFilter().getFromDate().equals(transactionSearchDto.getFilter().getToDate()))
			    {
			        criteria.add(Restrictions.sqlRestriction("DATE(BOTR_Borrowed_Date) = ?", transactionSearchDto.getFilter().getFromDate(), StandardBasicTypes.DATE));
			    }
			    else
			    {
			    	criteria.add(Restrictions.between("borrowedDate", transactionSearchDto.getFilter().getFromDate(),
			    			transactionSearchDto.getFilter().getToDate()));
			    }
			}
		}
		

		if (transactionSearchDto.getOrder().getColumn().equalsIgnoreCase("name"))
		{
			if (transactionSearchDto.getOrder().getType().equalsIgnoreCase("asc")) 
			{
				criteria.addOrder(Order.asc("member.firstName")).addOrder(Order.asc("member.lastName"));
			}
			else
			{
				criteria.addOrder(Order.desc("member.firstName")).addOrder(Order.desc("member.lastName"));
			}
		}

		else if (transactionSearchDto.getOrder().getColumn().equalsIgnoreCase("bookId")) 
		{
			if (transactionSearchDto.getOrder().getType().equalsIgnoreCase("asc")) 
			{
				criteria.addOrder(Order.asc("book.bookId"));
			}
			else
			{
				criteria.addOrder(Order.desc("book.bookId"));
			}
		}
		
		else if (transactionSearchDto.getOrder().getColumn().equalsIgnoreCase("memberId"))
		{
			if (transactionSearchDto.getOrder().getType().equalsIgnoreCase("asc"))
			{
				criteria.addOrder(Order.asc("member.memberId"));
			}
			else
			{
				criteria.addOrder(Order.desc("member.memberId"));
			}
		}
		else
		{
			criteria.addOrder(Order.asc("member.firstName")).addOrder(Order.asc("member.lastName"));
		}
		
		criteria.setProjection(Projections.rowCount());
		Long filteredRecords = (Long) criteria.uniqueResult();
		
		criteria.setFirstResult(transactionSearchDto.getStart())
				.setMaxResults(transactionSearchDto.getLimit())
				.setProjection(Projections.projectionList()
					.add(Projections.property("member.memberId"), "memberId")
					.add(Projections.property("member.firstName"), "firstName")
					.add(Projections.property("member.lastName"), "lastName")
					.add(Projections.property("member.email"), "email")
					.add(Projections.property("book.bookId"), "bookId")
					.add(Projections.property("book.title"), "title")
					.add(Projections.property("book.author"), "author")
					.add(Projections.property("book.language"), "language")
					.add(Projections.property("borrowedCount"), "borrowedCount")
					.add(Projections.property("returnedCount"), "returnedCount")
					.add(Projections.property("rentalStatus"), "rentalStatus")
					.add(Projections.property("borrowedDate"), "borrowedDate")
					.add(Projections.property("actualReturnedDate"), "actualReturnedDate")
					.add(Projections.property("returnDueDate"), "returnDueDate"));

		List<RetrivingDto> resultList = criteria.setResultTransformer(Transformers.aliasToBean(RetrivingDto.class)).list();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("totalRecords", totalRecords);
		result.put("filteredRecords", filteredRecords);
		result.put("data", resultList);
		return result;
	}
}
