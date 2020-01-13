package service;


import java.util.HashMap;
import java.util.Map;
import data.UserDB;
import help.Container;
import help.Helper;
import help.MailUtil;
import model.User;

public class UserService {

	 
	public static boolean isAuthenticated(User user) {
		
		return UserDB.isExist(user.getEmail(),user.getPassword());
		
	}

	public static User getUser(String Email) {
		
		return UserDB.findByEmail(Email);
	}

	public static boolean hasValidInfo(User user,Map<String,String> validationMessages) {
		
		
		Map<String,String> patterns 			= new HashMap<>();
		Map<String,String> successMessages 	    = new HashMap<>();
		Map<String,String> failMessages 	    = new HashMap<>();
		
		Container<Integer> successCount 	    = new Container<>();
		Container<Integer> failCount    	    = new Container<>();
		
		boolean isNewEmail = true;
		/* Initialize Patterns Map */
		patterns.put("email","^([\\w-\\.]+)@([a-z\\d-]+)\\.([a-z]{2,8})(\\.[a-z]{2,8})?$");
		patterns.put("password","^[\\w@-]{8,20}$");
		patterns.put("name","^[a-zA-Z]{3,10}$");
		patterns.put("phone","^(\\+2)?01[0125][\\d]{8}$");
		
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
		
		
		Helper.validateFieldsWithPatterns(user, patterns,successMessages,failMessages,
										  validationMessages,successCount,failCount);
		
		if(UserDB.isExist(user.getEmail())) {
				validationMessages.put("email", "inValid");
				isNewEmail = false;
		}
	
		
		return (failCount.getValue() == 0 && isNewEmail);
		
	}

	public static boolean isInsertedSuccessfully(User user) {
		
		return (UserDB.insert(user) > 0);
	}

	public static boolean isCorrectVerification(String email,String code) {
		
		String realCode = UserDB.getVerificationCode(email); 
		return (realCode != null && realCode.equals(code));
	}

	public static void sendVerificationCode(String email) {
		
		String content = "YOUR VERFIFICATION CODE IS " + UserDB.getVerificationCode(email);
		MailUtil.sendMail("customerServices@gmail.com",email,"SocilSite Verification",MailUtil.BODYTYPE.TEXT_PLAIN,content);
		
	}
	
	public static void makeUserVerified(String email) {
		
		UserDB.makeUserVerified(email);
	}

	public static boolean isVerified(User user) {
		
		/* IF USER EMAIL IS NOT EXIST IN VERFICATION TABLE SO IT IS ALREADY VERFIED */
		return (UserDB.getVerificationCode(user.getEmail()) == null);
	}

	

}
