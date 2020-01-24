package service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import data.PostDB;
import data.UserDB;
import help.Container;
import help.Helper;
import help.MailUtil;
import help.SecurityUtil;
import model.User;

public class UserService {
	
	private static Map<String,String> patterns;
	
	public static Map<String,String> getValidationPatterns(){
		
		if(patterns == null) {
			 patterns = new HashMap<>();
			 /* Initialize Patterns Map */
				patterns.put("email","^([\\w-\\.]+)@([a-z\\d-]+)\\.([a-z]{2,8})(\\.[a-z]{2,8})?$");
				patterns.put("password","^[\\w@-]{8,20}$");
				patterns.put("name","^[a-zA-Z]{3,10}$");
				patterns.put("phone","^(\\+2)?01[0125][\\d]{8}$");
		}
		
		return patterns;
	}
	 
	public static boolean isAuthenticated(User user) {
		
		return UserDB.isExist(user.getEmail(),user.getPassword());
		
	}

	public static User getUser(String Email) {
		
		User user =  UserDB.findByEmail(Email);
		user.setFriends(UserDB.getFriendsByID(user.getID()));
		user.setPosts(PostDB.getPrivateByID(user.getID()));
	
		return user;
	}

	public static boolean hasValidInfo(User user,Map<String,String> validationMessages) {
		
		Map<String,String> successMessages 	    = new HashMap<>();
		Map<String,String> failMessages 	    = new HashMap<>();
		
		Container<Integer> successCount 	    = new Container<>();
		Container<Integer> failCount    	    = new Container<>();
		
		boolean isNewEmail = true;
		
		/* Initialize successMessages  Map */
		successMessages.put("email","valid");
		successMessages.put("password","valid");
		successMessages.put("name","valid");
		successMessages.put("phone","valid");
		
		/* Initialize failMessages  Map */
		failMessages.put("email","inValid");
		failMessages.put("password","inValid");
		failMessages.put("name","inValid");
		failMessages.put("phone","inValid");
		
		
		Helper.validateFieldsWithPatterns(user, UserService.getValidationPatterns(),successMessages,failMessages,
										  validationMessages,successCount,failCount);
		
		if(UserDB.isExist(user.getEmail())) {
				validationMessages.put("email", "inValid");
				isNewEmail = false;
		}
	
		
		return (failCount.getValue() == 0 && isNewEmail);
		
	}

	public static boolean isInsertedSuccessfully(User user) {
		
		boolean isInserted = (UserDB.insert(user) > 0);
		
		if(isInserted) {
			sendVerificationCode(user.getEmail());
		}
		
		return isInserted;
	}

	public static boolean isCorrectVerification(String email,String code) {
		
		String realCode = UserDB.getVerificationCode(email); 
		return (realCode != null && realCode.equalsIgnoreCase(code));
	}

	public static void sendVerificationCode(String email) {
		
		String body = "Thanks to Join SocilaSite.Please Verify Your Account During 24 Hours Or it will be Deleted. Your Verfication Code is : " 
							+ UserDB.getVerificationCode(email);
		
		try {
			MailUtil.sendMail("custserv2021@gmail.com",email,"SocilSite Verification Code",MailUtil.BODYTYPE.TEXT,body);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void makeUserVerified(String email) {
		
		UserDB.makeUserVerified(email);
	}

	public static boolean isVerified(User user) {
		
		/* IF USER EMAIL IS NOT EXIST IN VERFICATION TABLE SO IT IS ALREADY VERFIED */
		return (UserDB.getVerificationCode(user.getEmail()) == null);
	}

	public static boolean sendResetPasswordEmail(String email) {
		/* 
		 * This Function Will Return False if Email Is Syntactly InValid.
		 * 						Will Return True if Email is Exist With Us And Send Email to it.
		 * 						Will Return True if Email Is Not Exist With Us WithOut Sending Any Email.
		 */
		
		/* Validate Email With REGEX Pattern */
		if(!Pattern.matches(UserService.getValidationPatterns().get("email"), email)) {
			return false;
       }
		
		/* Check if This Email Already Register With Us Before */
	  if(UserDB.isExist(email)) {
		  
		  	String token = UserDB.getResetPasswordToken(email);
		
			/* Check if This Email already has Reset Token */
			if(token == null) {
				/* Create New Token And Store It In DataBase */
				token = createResetPasswordToken();
				UserDB.insertResetPasswordToken(email,token);
				
			}
			/* Send Email With Instruction Of Password Reset */
			String body = "This is The Password Reset Instruction ."
								+ "Please Use It During One Hour Or it Will Be Invalid."
								+ "Please Enter OR Copy Reset URL to Your Browser To Reset Your Password."
								+ "Reset URL : http://localhost:8080/social/createNewPassword.jsp?token=" + token; 
			MailUtil.sendMail("custserv2021@gmail.com", email, "Password Reset Instruction ", MailUtil.BODYTYPE.TEXT, body);
	  }
			
	  return true;
	}

	private static String createResetPasswordToken() {
			return SecurityUtil.hashPassword(SecurityUtil.getSalt());
	}

	public static boolean updatePasswordByToken(String token, String password) {
		
		String email = UserDB.getEmailByToken(token);
		
		if(email == null ) return false;
		UserDB.deleteResetPasswordToken(token);
		
		return (UserDB.updatePassword(email,password) > 0);
	}

	public static void getSearchFriendsResult(int ID,String value,List<User> results, Map<Integer, String> states) {
		
		if(value == null || value.trim().isEmpty()) return;
		
		UserDB.getSearchResult(ID,value,results,states);
	}

	
	public static void getRequestsThatRecieved(int ID , List<User> receivedRequests) {
		UserDB.getRecievedRequests(ID,receivedRequests);
	}

	public static void getRequestsThatSent(int ID, List<User> sentRequests) {
		
		UserDB.getSentRequests(ID,sentRequests);
	}

	public static void addFriend(int userID, int friendID) {
		
		UserDB.sendRequest(userID,friendID);
	}

	public static void acceptFriend(int userID, int friendID) {
		
		UserDB.acceptRequest(userID, friendID);
		
	}
	public static void removeFriend(int userID, int friendID) {
		
		UserDB.removeFriendship(userID,friendID);
		
	}
	

}
