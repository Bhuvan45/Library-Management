package com.library.restController;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.model.custom.displayDto.FilteredResponse;
import com.library.model.custom.displayDto.Response;
import com.library.model.custom.insertDto.MemberDto;
import com.library.model.custom.insertDto.memberSearch.MemberSearchDto;
import com.library.service.MemberService;

@CrossOrigin
@RestController
@RequestMapping(value = "member")
public class MemberRestController 
{
	
	@Autowired
	private MemberService memberService;
	
	@CrossOrigin
	@RequestMapping(value = "addOrUpdateMember", method = RequestMethod.POST)
	public ResponseEntity<Response> addOrUpdateMembers(@RequestBody MemberDto memberDto) 
	{
		return new ResponseEntity<>(memberService.addOrUpdateMember(memberDto), HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "getMembersBySearchAndFilter", method = RequestMethod.POST)
	public ResponseEntity<FilteredResponse> getMembersBySearchAndFilter(@RequestBody MemberSearchDto memberSearchDto)
	{
		return new ResponseEntity<>(memberService.getMembersBySearchAndFilter(memberSearchDto), HttpStatus.OK);
	}
	
	@CrossOrigin
	@RequestMapping(value = "getMemberDetailsById", method = RequestMethod.GET)
	public ResponseEntity<FilteredResponse> getMemberDetailsById(@RequestParam Integer memberId)
	{
		return new ResponseEntity<>(memberService.getMemberDetailsById(memberId), HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "activateMember", method = RequestMethod.POST)
	public ResponseEntity<Response> activatingMembers(@RequestParam List<Integer> memberIds,
			@RequestParam(required = false) @DateTimeFormat(pattern = "MM-dd-yyyy hh:mm a") Date endDate) 
	{
		return new ResponseEntity<>(memberService.activateMembers(memberIds, endDate), HttpStatus.OK);
	}
	
	@CrossOrigin
	@RequestMapping(value = "deActivateMember", method = RequestMethod.POST)
	public ResponseEntity<Response> deActivatingMembers(@RequestParam List<Integer> memberIds) 
	{
		return new ResponseEntity<>(memberService.deActivateMembers(memberIds), HttpStatus.OK);
	}
}
