package com.library.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.library.model.custom.insertDto.MemberDto;
import com.library.model.custom.insertDto.memberSearch.MemberSearchDto;
import com.library.model.entity.MembershipMaintenance;

public interface MemberDAO
{	
	Integer addMember(MembershipMaintenance membershipMaintenance);

	MembershipMaintenance getMemberById(Integer memberId);

	boolean getActiveTransactionsForMember(Integer memberId);

	boolean isDuplicateMember(String firstName);

	List<MembershipMaintenance> getMembershipExpiredMembers(Date currentDate);

	void updateMember(MembershipMaintenance member);

	Map<String, Object> getMemberDetailsById(Integer memberId);

	Map<String, Object> getMembersBySearchAndFilter(MemberSearchDto memberSearchDto);
}
