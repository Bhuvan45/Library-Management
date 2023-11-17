package com.library.service;

import java.util.Date;
import java.util.List;

import com.library.model.custom.displayDto.FilteredResponse;
import com.library.model.custom.displayDto.Response;
import com.library.model.custom.insertDto.MemberDto;
import com.library.model.custom.insertDto.memberSearch.MemberSearchDto;

public interface MemberService 
{	
	Response addOrUpdateMember(MemberDto memberDto);

	Response activateMembers(List<Integer> memberIds, Date endDate);

	Response deActivateMembers(List<Integer> memberIds);
	
	FilteredResponse getMemberDetailsById(Integer memberId);

	FilteredResponse getMembersBySearchAndFilter(MemberSearchDto memberSearchDto);
}
