package com.library.serviceImpl;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.dao.MemberDAO;
import com.library.model.custom.displayDto.FilteredResponse;
import com.library.model.custom.displayDto.Response;
import com.library.model.custom.errorDto.ErrorDto;
import com.library.model.custom.insertDto.MemberDto;
import com.library.model.custom.insertDto.memberSearch.MemberSearchDto;
import com.library.model.entity.MembershipMaintenance;
import com.library.service.MemberService;

import com.library.util.WebUtil;

@Service
public class MemberServiceImpl implements MemberService
{	
	@Autowired
	private MemberDAO memberDao;

	@Override
	@Transactional
	public Response addOrUpdateMember(MemberDto memberDto) 
	{
		MembershipMaintenance membershipMaintenance = new MembershipMaintenance();
		List<ErrorDto> errorList = new ArrayList<ErrorDto>();
		Response response = new Response();
		if (memberDto.getMemberId() == null)
		{
			 if (memberDao.isDuplicateMember(memberDto.getEmail()))
			 {
				response.setStatus(WebUtil.STATUS_F);
				response.setData("Member with this Email already exist");
		     }
			 else
			 {
				FieldValidation.validateMemberFields(errorList, memberDto, membershipMaintenance);	
				
				if (errorList.isEmpty())
				{
					membershipMaintenance.setMembershipStartDate(new Date());			
					membershipMaintenance.setMembershipStatus(WebUtil.MEMBER_STATUS_ACTIVE);
					membershipMaintenance.setCreatedDate(new Date());
					membershipMaintenance.setUpdatedDate(new Date());
					
					Integer id = memberDao.addMember(membershipMaintenance);
					response.setStatus(WebUtil.STATUS_S);
					response.setData(membershipMaintenance.getFullName() + " Added Successfully with ID: " + id);
				} 
				else 
				{
					response.setStatus(WebUtil.STATUS_F);
					response.setData(errorList);
				}
			 }			
			return response;
		}
		else
		{
			MembershipMaintenance existingMember = memberDao.getMemberById(memberDto.getMemberId());

			if (existingMember != null)
			{
				if (! memberDto.getEmail().equals(existingMember.getEmail()) && memberDao.isDuplicateMember(memberDto.getEmail()))
				{
		            response.setStatus(WebUtil.STATUS_F);
		            response.setData("Member with this Email already exists");
			    }
				else
				{
					FieldValidation.validateMemberFields(errorList, memberDto, existingMember);
					if (errorList.isEmpty())
					{
						existingMember.setUpdatedDate(new Date());
						response.setStatus(WebUtil.STATUS_S);
						response.setData(existingMember.getFullName() +" Details Updated Successfully");
					} 
					else
					{
						response.setStatus(WebUtil.STATUS_F);
						response.setData(errorList);
					}
				}
			}
			else
			{
				response.setStatus(WebUtil.STATUS_F);
				response.setData("Member Details Not Found");
			}
		}
		return response;
	}

	@Override
	@Transactional
	public FilteredResponse getMembersBySearchAndFilter(MemberSearchDto memberSearchDto)
	{
		FilteredResponse filteredResponse = new FilteredResponse();
		try 
		{
			List<ErrorDto> errorList = new ArrayList<ErrorDto>();
			if(! memberSearchDto.getSearchBy().equalsIgnoreCase("name") 
					&& ! memberSearchDto.getSearchBy().equalsIgnoreCase("memberId")
					&& ! memberSearchDto.getSearchBy().equalsIgnoreCase("phoneNumber") 
					&& ! memberSearchDto.getSearchBy().equalsIgnoreCase("email"))
			{
				 ErrorDto error = new ErrorDto();
				 error.setFieldName("searchBy");
				 error.setErrorMessage("Search By should be Name or MemberId or PhoneNumber or Email");
				 errorList.add(error);
				 filteredResponse.setStatus(WebUtil.STATUS_F);
				 filteredResponse.setData(errorList);
			}
			
			FieldValidation.validateStartAndLimitField(memberSearchDto.getStart(),
					memberSearchDto.getLimit(), errorList);
			
			if((filteredResponse.getStatus() != null && filteredResponse.getStatus().equals(WebUtil.STATUS_F))
					|| !errorList.isEmpty())
			{
				filteredResponse.setStatus(WebUtil.STATUS_F);
				filteredResponse.setData(errorList);
				return filteredResponse;
			}		
			
			Map<String, Object> members = memberDao.getMembersBySearchAndFilter(memberSearchDto);
			if ((Long) members.get("filteredRecords") > 0) 
			{
				filteredResponse.setData(members.get("data"));
				filteredResponse.setTotalRecords((Long) members.get("totalRecords"));
				filteredResponse.setFilteredRecords((Long) members.get("filteredRecords"));
			}
			else
			{
				filteredResponse.setData("No Members Found");
			}
			filteredResponse.setStatus(WebUtil.STATUS_S);
		}
		catch (Exception e)
		{
			filteredResponse.setStatus(WebUtil.STATUS_F);
			filteredResponse.setData("Error occured while fetching");
		}
		return filteredResponse;
	}

	@Override
	@Transactional
	public Response activateMembers(List<Integer> memberIds, Date endDate)
	{
	    Response response = new Response();

	    List<String> membersActivated = new ArrayList<>();
	    List<String> membersAlreadyActive = new ArrayList<>();
	    List<String> membersCannotActivate = new ArrayList<>();
	    List<String> membersNotFound = new ArrayList<>();

	    try
	    {
	        for (Integer memberId : memberIds)
	        {
	            MembershipMaintenance membershipMaintenance = memberDao.getMemberById(memberId);
	            if (membershipMaintenance != null) 
	            {
	                if (membershipMaintenance.getMembershipStatus().equalsIgnoreCase(WebUtil.MEMBER_STATUS_ACTIVE)) 
	                {
	                    membersAlreadyActive.add("Member " + membershipMaintenance.getFullName() + " is already in Active Status");
	                }
	                else
	                {
	                    if (endDate != null && endDate.before(new Date())) 
	                    {
	                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	                        membersCannotActivate.add(membershipMaintenance.getFullName() + "Membership EndDate should be greater than or equal to " + dateFormat.format(new Date()));

	                    }
	                    else 
	                    {
	                        membershipMaintenance.setMembershipEndDate(endDate);
	                        membershipMaintenance.setMembershipStatus(WebUtil.MEMBER_STATUS_ACTIVE);
	                        membershipMaintenance.setUpdatedDate(new Date());
	                        membersActivated.add("Member " + membershipMaintenance.getFullName() + " Membership Activated");
	                    }
	                }
	            }
	            else
	            {
	                membersNotFound.add("Member ID " + memberId + " - No Member Found");
	            }
	        }

	        List<String> combinedResults = new ArrayList<>();
	        combinedResults.addAll(membersActivated);
	        combinedResults.addAll(membersAlreadyActive);
	        combinedResults.addAll(membersCannotActivate);
	        combinedResults.addAll(membersNotFound);

	        response.setStatus(WebUtil.STATUS_S);
	        response.setData(combinedResults);
	    } 
	    catch (Exception e)
	    {
	        response.setStatus(WebUtil.STATUS_F);
	        response.setData("Error occurred during activation");
	    }

	    return response;
	}

	@Override
	@Transactional
	public Response deActivateMembers(List<Integer> memberIds)
	{
	    Response response = new Response();

	    Map<Integer, String> membersDeactivated = new HashMap<>();
	    Map<Integer, String> alreadyDeactivated = new HashMap<>();
	    Map<Integer, String> cannotDeactivated = new HashMap<>();

	    try
	    {
	        for (Integer memberId : memberIds)
	        {
	            MembershipMaintenance membershipMaintenance = memberDao.getMemberById(memberId);
	            
	            if (membershipMaintenance == null)
	            {
	            	cannotDeactivated.put(memberId, "No Member Found");
	            }
	            else if (membershipMaintenance.getMembershipStatus().equalsIgnoreCase(WebUtil.MEMBER_STATUS_INACTIVE)) 
	            {
	                alreadyDeactivated.put(memberId, "Already Deactivated");
	            }
	            else if (memberDao.getActiveTransactionsForMember(memberId))
	            {
	            	cannotDeactivated.put(memberId, "Member cannot be deactivated. They have active book transactions");
	            }
	            else 
	            {
	                membershipMaintenance.setMembershipStatus(WebUtil.MEMBER_STATUS_INACTIVE);
	                membershipMaintenance.setMembershipEndDate(new Date());
	                membershipMaintenance.setUpdatedDate(new Date());
	                membersDeactivated.put(memberId, "Membership Deactivated");
	            }
	        }

	        Map<Integer, String> combinedResults = new HashMap<>();
	        combinedResults.putAll(membersDeactivated);
	        combinedResults.putAll(alreadyDeactivated);
	        combinedResults.putAll(cannotDeactivated);

	        response.setData(combinedResults);
	        
	        if (! cannotDeactivated.isEmpty())
	        {
	            response.setStatus(WebUtil.STATUS_F);
	        }
	        else
	        {
	            response.setStatus(WebUtil.STATUS_S);
	        }
	    } 
	    catch (Exception e) 
	    {
	        response.setStatus(WebUtil.STATUS_F);
	        response.setData("Error occurred during deactivation");
	    }

	    return response;
	}

	@Override
	@Transactional
	public FilteredResponse getMemberDetailsById(Integer memberId) 
	{
		FilteredResponse filteredResponse = new FilteredResponse();
		try
		{			
			Map<String, Object> members = memberDao.getMemberDetailsById(memberId);
			if ((Long) members.get("filteredRecords") > 0) 
			{
				filteredResponse.setData(members.get("data"));
				filteredResponse.setTotalRecords((Long) members.get("totalRecords"));
				filteredResponse.setFilteredRecords((Long) members.get("filteredRecords"));
			}
			else
			{
				filteredResponse.setData("No Members Found");
			}
			filteredResponse.setStatus(WebUtil.STATUS_S);
		}
		catch(Exception e)
		{
			filteredResponse.setData("Error occured while fetching");
			filteredResponse.setStatus(WebUtil.STATUS_F);
		}
		return filteredResponse;
	}
	
//	@Override
//	@Transactional
//	@Scheduled(cron = "0 37 16 * * ?")
//    public void checkAndDeactivateExpiredMembers()
//    {
//        List<MembershipMaintenance> expiredMembers = memberDao.getMembershipExpiredMembers(new Date());
//
//        for (MembershipMaintenance member : expiredMembers) 
//        {
//    		  member.setMembershipStatus(WebUtil.MEMBER_STATUS_INACTIVE);
//              memberDao.updateMember(member);
//              System.out.println("Cron executed successfully.");
//        }
//    }
}
