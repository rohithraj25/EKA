package com.example.resource;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dto.ApplyLeaveRequestDto;
import com.example.dto.CommonApiResponse;
import com.example.dto.UserResponseDto;
import com.example.entity.LeaveBalance;
import com.example.entity.LeaveRequest;
import com.example.entity.User;
import com.example.exceptions.MissingInputException;
import com.example.exceptions.UserNotFoundException;
import com.example.service.EmailService;
import com.example.service.LeaveBalanceService;
import com.example.service.LeaveRequestService;
import com.example.service.UserService;

@Service
public class LeaveRequestResource {

	@Autowired
	private LeaveRequestService leaveRequestService;

	@Autowired
	private LeaveBalanceService leaveBalanceService;
	
	@Autowired
	UserService userService;

	@Autowired
	private EmailService emailService;
	
	 public ResponseEntity<CommonApiResponse> applyLeave( List<ApplyLeaveRequestDto> leaveRequests) {
	        CommonApiResponse response = new CommonApiResponse();

	        if(leaveRequests==null||leaveRequests.isEmpty()) {
				throw new MissingInputException("input missing");
			}
	        
	        
	        for (ApplyLeaveRequestDto leaveRequestDto : leaveRequests) {
	            User user = userService.findByEmpNumber(leaveRequestDto.getEmpnumber());

	            if (user == null) {
	            	throw new UserNotFoundException("Employee not found for empnumber: " + leaveRequestDto.getEmpnumber());
	               
	            }

	            LeaveRequest leaveRequest = ApplyLeaveRequestDto.toLeaveRequestEntity(leaveRequestDto, user);
	            leaveRequest.setStatus("Pending");

	            int requestedLeaveDays = calculateLeaveDays(leaveRequest);

	            LeaveBalance byEmpnumberAndLeaveType = leaveBalanceService
	                    .findByEmpnumberAndLeaveType(leaveRequest.getEmpnumber(), leaveRequest.getLeaveType());

	            if (byEmpnumberAndLeaveType == null) {
	                response.setMessage("No leave balance found for employee: " + leaveRequest.getEmpnumber());
	                response.setStatus(false);
	                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	            }

	            if (byEmpnumberAndLeaveType.getAvailableDays() < requestedLeaveDays) {
	                response.setMessage("Insufficient leave balance for employee: " + leaveRequest.getEmpnumber());
	                response.setStatus(false);
	                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	            }

	            boolean isLeaveApplied = leaveRequestService.applyLeave(leaveRequest);
	            if (isLeaveApplied) {
	                leaveBalanceService.updateLeaveBalance(leaveRequest);
	                emailService.sendEmailForLeave(leaveRequest.getEmployeeEmail(), "Leave Request Submitted",
	                        "Your leave request has been submitted for approval.");
	                emailService.notifyHrsAboutLeave(leaveRequest);
	            } else {
	                response.setMessage("Failed to apply leave for employee: " + leaveRequest.getEmployeeName());
	            }
	        }

	        response.setMessage("Leave request processed.");
	        response.setStatus(true);
	        return ResponseEntity.ok(response);
	    }

	    private int calculateLeaveDays(LeaveRequest leaveRequest) {
	        return (int) ChronoUnit.DAYS.between(leaveRequest.getStartDate(), leaveRequest.getEndDate()) + 1;
	    }
///leave approve api
	public ResponseEntity<CommonApiResponse> approveLeave(Long empnumber) {
	  CommonApiResponse response = new CommonApiResponse();
	  
	  String status="Pending";
	  
	  LeaveRequest leaveRequest =leaveRequestService.findByEmpnumberAndStatus(empnumber, status); 
	  if (leaveRequest !=null) {
		  leaveRequestService.approveLeave(leaveRequest);
	  emailService.sendEmailApproved(leaveRequest.getEmployeeEmail(),
	  "Leave Approved", "Your leave request has been approved.");
	  response.setMessage("Leave approved.");
	  response.setStatus(false);
	  return new ResponseEntity<CommonApiResponse>(response,HttpStatus.OK);
	  }
	  response.setMessage("Leave request not found.");
	  response.setStatus(false);
	  return  new ResponseEntity<CommonApiResponse>(response,HttpStatus.NOT_FOUND);
	  }

	//leave reject api
	
	public ResponseEntity<CommonApiResponse> rejectLeave(Long empnumber) {
        CommonApiResponse response = new CommonApiResponse();

        String status = "Pending";

        LeaveRequest leaveRequest = leaveRequestService.findByEmpnumberAndStatus(empnumber, status);
        if (leaveRequest != null) {
            // Calculate the number of requested leave days
            int requestedLeaveDays = calculateLeaveDays(leaveRequest);

            // Update leave request status to "Rejected"
            leaveRequest.setStatus("Rejected");
            leaveRequestService.rejectLeave(leaveRequest);

            // Rollback the leave balance
            leaveBalanceService.updateLeaveBalance(leaveRequest.getEmpnumber(), leaveRequest.getLeaveType(), requestedLeaveDays);

            // Send rejection email
            emailService.sendEmailRejected(leaveRequest.getEmployeeEmail(), "Leave Rejected", "Your leave request has been rejected.");

            response.setMessage("Leave rejected.");
            response.setStatus(true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.setMessage("Leave request not found.");
        response.setStatus(false);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

  //api for findall pending leaves
	
	public ResponseEntity<UserResponseDto>findallpendingleaves(){
		UserResponseDto response = new UserResponseDto();
		List<LeaveRequest> findbyleaverequests = leaveRequestService.findbyleaverequests();
		
		if(findbyleaverequests.isEmpty()) {
			response.setMessage("no pending requests found");
			response.setStatus(false);
			return new ResponseEntity<UserResponseDto>(response,HttpStatus.NOT_FOUND);
		}
		response.setLeaverequest(findbyleaverequests);
		response.setMessage(" pending requests found");
		response.setStatus(true);
		
		return new ResponseEntity<UserResponseDto>(response,HttpStatus.OK);
	}

	public ResponseEntity<UserResponseDto>findempleavesrequests(Long empnumber){
		UserResponseDto response = new UserResponseDto();
		List<LeaveRequest> byEmpnumber = leaveRequestService.findByEmpnumber(empnumber);
		
		if(byEmpnumber.isEmpty()) {
			response.setMessage("no  requests found");
			response.setStatus(false);
			return new ResponseEntity<UserResponseDto>(response,HttpStatus.NOT_FOUND);
		}
		
		response.setLeaverequest(byEmpnumber);
		response.setMessage("data found");
		response.setStatus(true);
		return new ResponseEntity<UserResponseDto>(response,HttpStatus.OK);
	}
}
