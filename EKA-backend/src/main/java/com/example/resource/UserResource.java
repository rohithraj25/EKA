package com.example.resource;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.AdminPasswordAndEmailUpdate;
import com.example.dto.AdminUpdateDetailsDto;
import com.example.dto.CommonApiResponse;
import com.example.dto.OTPValidation;
import com.example.dto.OtpRequest;
import com.example.dto.RequestUserDto;
import com.example.dto.ResponseDto;
import com.example.dto.UserLoginRequest;
import com.example.dto.UserLoginResponse;
import com.example.dto.UserResponseDto;
import com.example.entity.User;
import com.example.exceptions.DataNotFound;
import com.example.exceptions.MissingInputException;
import com.example.exceptions.UserNotFoundException;
import com.example.exceptions.UserSaveFailedException;
import com.example.security.JwtUtils;
import com.example.service.OtpService;
import com.example.service.UserService;

@Service
public class UserResource {

    private static final Logger logger = Logger.getLogger(UserResource.class.getName());

    @Autowired
    private UserService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
  

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private OtpService otpService;
//registe HR
    public ResponseEntity<CommonApiResponse> registerUser(RequestUserDto request) {
        CommonApiResponse response = new CommonApiResponse();

        if (request == null) {
          throw new MissingInputException("input missing");
        }

        
        
        User getbyemail = service.getbyemail(request.getEmail());
        
        if(getbyemail!=null) {
        	response.setMessage("user alredy register this mail");
        	response.setStatus(false);
        	 return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        
        User userEntity = RequestUserDto.toUserEntity(request);
        userEntity.setRole("HR");
        userEntity.setStatus("Active");
        
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedata = service.savedata(userEntity);
        if (savedata == null) {
            throw new UserSaveFailedException("Failed to register");
        }

        response.setMessage("Successfully registered");
        response.setStatus(true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
//login api
    public ResponseEntity<UserLoginResponse> login(UserLoginRequest loginRequest) {
        UserLoginResponse response = new UserLoginResponse();

        if (loginRequest == null) {
           throw new MissingInputException("Missing input");
        }

        String jwtToken;
        User user;

       // List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(loginRequest.getRole()));

        try {
            if (loginRequest.getOtp() != null && !loginRequest.getOtp().isEmpty()) {
                if (!otpService.validateOtp(loginRequest.getEmailId(), loginRequest.getOtp())) {
                    response.setMessage("Invalid OTP.");
                    response.setStatus(false);
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
            } else {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmailId(), loginRequest.getPassword()));
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Authentication failed", ex);
            response.setMessage("Invalid email or password.");
            response.setStatus(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        jwtToken = jwtUtils.generateToken(loginRequest.getEmailId());
        user = service.getbyemail(loginRequest.getEmailId());

        if (jwtToken != null) {
            response.setUser(user);
            response.setMessage("Logged in successfully");
            response.setStatus(true);
            response.setJwtToken(jwtToken);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setMessage("Login failed");
            response.setStatus(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
//otp send
    public ResponseEntity<CommonApiResponse> sendOtp(OtpRequest otpRequest) {
        CommonApiResponse response = new CommonApiResponse();

        if (otpRequest == null || otpRequest.getEmail() == null) {
            throw new MissingInputException("missing input");
        }
User getbyemail = service.getbyemail(otpRequest.getEmail());
if(getbyemail!=null) {
        try {
            otpService.generateAndSendOtp(otpRequest.getEmail());
            response.setMessage("OTP sent successfully");
            response.setStatus(true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to send OTP", e);
            response.setMessage("Failed to send OTP");
            response.setStatus(false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }}else
        	 response.setMessage("Give proper email");
response.setStatus(false);
return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    public ResponseEntity<UserResponseDto> fetchAllUsers() {
        UserResponseDto api = new UserResponseDto();

        List<User> findAll = service.findall();
        if (findAll.isEmpty()) {
          throw new DataNotFound("No data found");
        }

        api.setUser(findAll);
        api.setMessage("Data found");
        api.setStatus(true);
        return new ResponseEntity<>(api, HttpStatus.OK);
    }
    
	
    //give special permission
    public ResponseEntity<CommonApiResponse> grantRole(String email) {
        CommonApiResponse response = new CommonApiResponse();
        if(email==null) {
			throw new MissingInputException("input missing");
		}
        
        String role="HR";
        
        User byEmailAndRole = service.findByEmailAndRole(email, role);
        
        if(byEmailAndRole==null) {
      	  response.setMessage("permissin given failed its only for Hr s");
            response.setStatus(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

      }
        
        boolean success = service.grantRole(email);
        if (success) {
            response.setMessage("Role granted successfully");
            response.setStatus(true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setMessage("User not found or role assignment failed");
            response.setStatus(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
//remove special permission
    public ResponseEntity<CommonApiResponse> revokeRole(String email) {
        CommonApiResponse response = new CommonApiResponse();
        
        if(email==null) {
			throw new MissingInputException("input missing");
		}
        
        
         String role="Special";
        
        User byEmailAndRole = service.findByEmailAndRole(email, role);
        
        if(byEmailAndRole==null) {
        	  response.setMessage("User not found or role revocation failed");
              response.setStatus(false);
              return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }
        
        boolean success = service.revokeRole(email);
        if (success) {
            response.setMessage("Role revoked successfully");
            response.setStatus(true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setMessage("revocation failed");
            response.setStatus(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    /////
    //register employee
    public ResponseEntity<CommonApiResponse> registeremployeeUser(RequestUserDto request) {
        CommonApiResponse response = new CommonApiResponse();
        
        

        if (request == null) {
        	throw new MissingInputException("input missing");
        }

        
        
        User getbyemail = service.getbyemail(request.getEmail());
        
        if(getbyemail!=null) {
        	response.setMessage("user alredy register this mail");
        	response.setStatus(false);
        	 return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        
        
        User userEntity = RequestUserDto.toUserEntity(request);
        

    	
        userEntity.setRole("Employee");
        userEntity.setStatus("Active");
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        
   

        User savedata = service.savedata(userEntity);
        if (savedata == null) {
           throw new UserSaveFailedException("Failed to save data");
        }

        response.setMessage("Successfully registered");
        response.setStatus(true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
   
    
    
    public ResponseEntity<UserResponseDto>findbyemail(String email){
    	
    	
    	
    	
    	UserResponseDto dto = new UserResponseDto();
    	
    	
    	if(email==null) {
    		throw new MissingInputException("input missing");
		}
    	
    	User getbyemail = service.getbyemail(email);
    	if(getbyemail==null) {
    		throw new UserNotFoundException("user not found");
    	}
    	
    	dto.setSingleuser(getbyemail);
    	dto.setMessage("user  found");
		dto.setStatus(true);
		return new ResponseEntity<UserResponseDto>(dto,HttpStatus.OK);
    }
    
    
  
    
    
    //update employee
    
    public ResponseEntity<CommonApiResponse>updateemployee(String firstname,Long mobileno,String email){
    	
    	  CommonApiResponse response = new CommonApiResponse();
    	  User getbyemail = service.getbyemail(email);
    	  
    	  if(getbyemail==null) {
    		  throw new UserNotFoundException("user not found");
    	  }
    	  
    	  getbyemail.setFirstname(firstname);
    	  getbyemail.setMobileno(mobileno);
    	  User savedata = service.savedata(getbyemail);
    	  
    	  if (savedata == null) {
              response.setMessage("Failed to update data");
              response.setStatus(false);
              return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
          }

          response.setMessage("Successfully updated");
          response.setStatus(true);
          return new ResponseEntity<>(response, HttpStatus.OK);
    	
    	
    }
    
    //findby role
  public ResponseEntity<UserResponseDto>findbyrole(String role){
	  
	  
	  UserResponseDto response = new UserResponseDto();
	  
	  
	  if(role==null) {
			throw new MissingInputException("input missing");
		}
	  
	  List<User> byRole = service.findByRole(role);
	  
	  if(byRole.isEmpty()) {
		  throw new DataNotFound("data not found");
	  }
	  response.setUser(byRole);
	  response.setMessage("data found");
      response.setStatus(true);
      return new ResponseEntity<>(response, HttpStatus.OK);
  }
   
  //delete Employee
  public ResponseEntity<CommonApiResponse>deleteemployee(String email){
	   CommonApiResponse response = new CommonApiResponse();
	   
	   
	   if(email==null) {
			throw new MissingInputException("input missing");
		}
	   User getbyemail = service.getbyemail(email);
 	  
 	  if(getbyemail==null) {
 		throw new DataNotFound("no data found");
 	  }
 	 getbyemail.setStatus("Deactivate");
 	 
 	  User savedata = service.savedata(getbyemail);
 	  
 	  if (savedata == null) {
           response.setMessage("Failed to delete data");
           response.setStatus(false);
           return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
       }

       response.setMessage("Successfully deleted");
       response.setStatus(true);
       return new ResponseEntity<>(response, HttpStatus.OK);
	   
	   
  }
  
	/*
	 * //teams based on position
	 * 
	 * public ResponseEntity<UserResponseDto>findbyposition(String Designation){
	 * UserResponseDto response = new UserResponseDto();
	 * 
	 * List<User> byPosition = service.findByPosition(Designation);
	 * 
	 * if(byPosition.isEmpty()) { response.setMessage("no data found");
	 * response.setStatus(false); return new ResponseEntity<>(response,
	 * HttpStatus.NOT_FOUND); }
	 * 
	 * response.setUser(byPosition); response.setMessage("data found");
	 * response.setStatus(true); return new
	 * ResponseEntity<>(response,HttpStatus.OK); }
	 */
  
	//admin Update Details
  public ResponseEntity<CommonApiResponse>updateAdminDetails(AdminUpdateDetailsDto dto){
	  
		  CommonApiResponse response = new CommonApiResponse();
		  
		  
		  if(dto==null) {
				throw new MissingInputException("input missing");
			}
		 
		  User byEmailAndRole = service.findByEmailAndRole(dto.getEmail(), dto.getRole());
	  
	  if(byEmailAndRole==null) {
	  response.setMessage("your are not authorized to update details");
	  response.setStatus(false); return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	  } 
	  byEmailAndRole.setMobileno(dto.getMobileno());
	  byEmailAndRole.setFirstname(dto.getFirstname());
	  User update = service.savedata(byEmailAndRole);
	  if(update==null) {
		  response.setMessage("update failed");
          response.setStatus(false);
          return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	  }
	  response.setMessage("data updated success");
      response.setStatus(true);
      return new ResponseEntity<>(response, HttpStatus.OK);
  }
	 
  //password update
  
  public ResponseEntity<CommonApiResponse>OTPValidation(OTPValidation dto){
	  CommonApiResponse response = new CommonApiResponse();
	  
	  if(dto.getEmail()!=null||dto.getOtp()!=null) {
		  if (!otpService.validateOtp(dto.getEmail(), dto.getOtp())) {
              response.setMessage("Invalid OTP.");
              response.setStatus(false);
              return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
          }else
        	  response.setMessage("otp successfully validated");
	      response.setStatus(true);
	      return new ResponseEntity<>(response, HttpStatus.OK);
        	  
	  }
		  response.setMessage("missing input");
      response.setStatus(false);
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
  
  public ResponseEntity<CommonApiResponse>updatAdminPasswordAndEmail(AdminPasswordAndEmailUpdate dto){
	  CommonApiResponse response = new CommonApiResponse();
	  if(dto==null) {
		  throw new MissingInputException("input missing");
	  }
	  User getbyemail = service.getbyemail(dto.getOldEmail());
	  if(getbyemail==null) {
		  throw new DataNotFound("no data found");

	  }
	  getbyemail.setEmail(dto.getNewemail());
	  getbyemail.setPassword(passwordEncoder.encode(dto.getNewpassword()));
	  User update = service.savedata(getbyemail);
	  if(update==null) {
		  response.setMessage("failed to update data");
		  response.setStatus(false);
		  return new ResponseEntity<CommonApiResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
	  }
	  response.setMessage("succesfully updated");
	  response.setStatus(true);
	  return new ResponseEntity<CommonApiResponse>(response,HttpStatus.OK);
  }
  public ResponseEntity<ResponseDto> findBirthdays() {
      ResponseDto responseDto = new ResponseDto();

      LocalDate today = LocalDate.now();
      LocalDate futureDate = today.plusDays(20);

      List<User> usersWithUpcomingBirthdays = service.findUsersWithBirthdaysBetween(today, futureDate);

      if (usersWithUpcomingBirthdays.isEmpty()) {
          responseDto.setMessage("No upcoming birthdays found");
          responseDto.setStatus(false);
          return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
      }

      responseDto.setMessage("Upcoming birthdays found");
      responseDto.setStatus(true);
      responseDto.setListusers(usersWithUpcomingBirthdays);
      return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }
  public ResponseEntity<ResponseDto> findNewJoiners() {
	    ResponseDto responseDto = new ResponseDto();

	    LocalDate presentDate = LocalDate.now();
	    LocalDate pastDate = presentDate.minusDays(10); // 10 days before present date

	    List<User> newJoiners = service.findUsersWithJoiningDatesBetween(pastDate, presentDate);

	    if (newJoiners.isEmpty()) {
	        responseDto.setMessage("No new joiners found within the last 10 days.");
	        responseDto.setStatus(false);
	        return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
	    }

	    responseDto.setMessage("New joiners found within the last 10 days.");
	    responseDto.setStatus(true);
	    responseDto.setListusers(newJoiners);
	    return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

 
  public ResponseEntity<UserResponseDto> getCurrentUser() {
	    UserResponseDto response = new UserResponseDto();

	    // Get the authentication object from the security context
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication == null || !authentication.isAuthenticated()) {
	        response.setMessage("User is not authenticated");
	        response.setStatus(false);
	        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	    }

	    // Get the email of the authenticated user
	    String userEmail = authentication.getName();

	    // Fetch the user details using the email
	    User user = service.getbyemail(userEmail);

	    if (user == null) {
	        response.setMessage("User not found");
	        response.setStatus(false);
	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    }

	    response.setSingleuser(user);
	    response.setMessage("User details found");
	    response.setStatus(true);
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
  
  
}