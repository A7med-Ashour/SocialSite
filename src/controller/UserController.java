package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import service.UserService;


public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public UserController() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("action");
		
		if (action != null && !action.trim().isEmpty()) {
			switch(action) {
				case "login" : 
					loginHandler(request,response);
					break;
				case "join"  :
					registerHandler(request,response);
					break;
				case "Verify" :
				case "Resend":
					verifyHandler(request,response);
					break;
				case "Reset" :
				case "newPassword" :
					resetPasswordHandler(request,response);
					break;
				case "logout":
					logoutHandler(request,response);
					break;
				default :
					/* Do Nothing */
					break;
			}
		}else {
			getServletContext().getRequestDispatcher("/login.jsp").forward(request,response);
		}
		
	}



	private void loginHandler(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		String 	email = request.getParameter("email"),
				password = request.getParameter("password"),
				rememberMe = request.getParameter("rememberMe"),
				URL = "/login.jsp";
		
		User user = null;
		
		
		// EMPTY email OR PASSWORD
		try {
			user = User.builder().email(email).password(password).build();
		}catch(IllegalArgumentException e){
			request.setAttribute("errorMSG", "PLEASE ENTER ALL REQUIRED FIELDS.");
			getServletContext().getRequestDispatcher(URL).forward(request, response);
		}
			
			
		// VALID email AND PASSWORD
		if(UserService.isAuthenticated(user)) {
			user = UserService.getUser(email);
			if(UserService.isVerified(user)) {
				URL =  "/home.jsp";
				HttpSession session = request.getSession();
				session.setMaxInactiveInterval(-1);
				/* THIS PART TO MAKE SESSION THREAD SAFE  IF USER USE MULTI TABS*/
				final String sessionID = session.getId().intern(); /* String Constants Pool Object */
				synchronized(sessionID) {
					session.setAttribute("user", user);
				}
				
				/* IF USER WANTS TO REMEMBER HIM */
				if(rememberMe != null && rememberMe.equalsIgnoreCase("on")) {
					Cookie userEmailCookie = new Cookie("userEmail",user.getEmail());
					userEmailCookie.setMaxAge(60*60*24*365); /* ADD COOKIE FOR ONE YEAR */
					userEmailCookie.setPath("/");
					response.addCookie(userEmailCookie);
				}
				
			}else {
				URL =  "/verify.jsp";
				request.setAttribute("user",user);
			}
			
		}else {
			request.setAttribute("errorMSG"," WRONG Email OR PASSWORD.");
			request.setAttribute("user",user);
			request.setAttribute("rememberMe",rememberMe);
		}
					
		getServletContext().getRequestDispatcher(URL).forward(request, response);
		
	}
	
	private void registerHandler(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		User user = null;
		Map<String,String> validationMessages = new HashMap<>();
		String URL = "/register.jsp";
		
		try {
			user = User.builder().email(request.getParameter("email"))
					  			 .password(request.getParameter("password"))
					  			 .name(request.getParameter("name"))
					  			 .phone(request.getParameter("phone"))
					  			 .male(request.getParameter("gender") != null && request.getParameter("gender").equals("male"))
					  			 .build();
		}catch(IllegalArgumentException e) {
			request.setAttribute("errorMSG", "PLEASE ENTER ALL REQUIRED FIELDS.");
			getServletContext().getRequestDispatcher(URL).forward(request, response);
			return;
		}
		
		
		
		if(UserService.hasValidInfo(user,validationMessages) && UserService.isInsertedSuccessfully(user)) {
			URL = "/verify.jsp";
			user = UserService.getUser(user.getEmail());
		}else {
			request.setAttribute("messages", validationMessages);
		}
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(-1);
		/* THIS PART TO MAKE SESSION THREAD SAFE  IF USER USE MULTI TABS*/
		final String sessionID = session.getId().intern();
		synchronized(sessionID) {
			session.setAttribute("user", user);
		}
		
		getServletContext().getRequestDispatcher(URL).forward(request, response);
	}
	
	private void verifyHandler(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("action");
		String email  = request.getParameter("email");
		String code   = request.getParameter("code");
		String URL 	  = "/verify.jsp";
		User   user   = UserService.getUser(email);
		
		
		if(action.equals("Verify")) {
			if (UserService.isCorrectVerification(email,code)) {
				UserService.makeUserVerified(email);
				/* THIS PART TO MAKE SESSION THREAD SAFE  IF USER USE MULTI TABS*/
				HttpSession session = request.getSession();
				session.setMaxInactiveInterval(-1);
				final String sessionID = session.getId().intern();
				synchronized(sessionID) {
					session.setAttribute("user", user);
				}
				URL = "/home.jsp";
			}else {
				request.setAttribute("errorMSG", "INVALID CODE");
				request.setAttribute("user", user);
			}
			
		}else if(action.equals("Resend")) {
			UserService.sendVerificationCode(email);
			request.setAttribute("user", user);
			request.setAttribute("errorMSG", "YOUR CODE HAS BEEN SENT TO YOUR EMAIL ADDRESS.");
		}
		
		getServletContext().getRequestDispatcher(URL).forward(request, response);
		
	}
	
	private void resetPasswordHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(request.getParameter("action").equals("newPassword")) {
			String token = request.getParameter("token"),
					  password = request.getParameter("password"),
					  repeat = request.getParameter("repeat"),
					  message = "Could'nt Update Your Password",
					  URL = "/createNewPassword.jsp";
			
			if(password == null || password.trim().isEmpty()) {
				message = "Please Enter Required Fields.";
			}else if( !password.equals(repeat)) {
				message = "Password Doesn't Match";
			}else if(!Pattern.matches(UserService.getValidationPatterns().get("password"), password)) {
				message = "Password Must be Alphanumeric between 8 and 20 characters and may has @ or -";
			} else if(UserService.updatePasswordByToken(token,password)) {
				message = "Your Password Is Successfully Updated . You Can Now <a href='login.jsp'>Log In</a> . ";
				URL = "/tempMessage.jsp";
				request.setAttribute("title", "Rest Password Success");
				
			}
			
			request.setAttribute("errorMSG", message);
			request.setAttribute("token", token);
			request.setAttribute("password", password);
			request.setAttribute("repeat", repeat);
			getServletContext().getRequestDispatcher(URL).forward(request, response);
			return;
		}
		
		/* THIS PART TO HANDLE SEND REST PAGE */
		
		String email     = request.getParameter("email"),
				  message  = "InValid Email Address.";
		
		/* Check If The Reset Mail Is Sent Successfully */
		if ( UserService.sendResetPasswordEmail(email)) {
			message = "Please Check Your Email For Reset Password Instruction.";
		}
		
		request.setAttribute("errorMSG", message);
		getServletContext().getRequestDispatcher("/resetPassword.jsp").forward(request, response);
		
		
	}


	private void logoutHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/* MAKE SESSION INVALID */
		request.getSession().invalidate();
		
		/* DELETE USEREMAIL COOKIE IF EXISTS */
		Cookie cookies [] = request.getCookies();
		if(cookies != null) {
			for(Cookie c : cookies) {
				if(c.getName().equals("userEmail")) {
					c.setMaxAge(0);
					c.setPath("/");
					response.addCookie(c);
					break;
				}
			}
		}
		
		/* RETURN TO LOGIN PAGE */
		getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
		
	}



}
