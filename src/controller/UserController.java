package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
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
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("action");
		
		if(action!= null && action.equals("login")) {
			loginHandler(request,response);
		}else if(action != null && action.equals("join")){
			registerHandler(request,response);
		}else if(action!= null && (action.equals("Verify") || action.equals("Resend"))) { // Contains More Than ONE Action
			verifyHandler(request,response);
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
			}else {
				URL =  "/verify.jsp";
			}
			request.setAttribute("user",user);
			HttpSession session = request.getSession();
			session.getId();
			
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
			
			request.setAttribute("user", user); // remove it
			/* Make session Thread Save */
			//HttpSession session = request.getSession();
			//session.setAttribute("user", user);
		}else {
			request.setAttribute("user", user);
			request.setAttribute("messages", validationMessages);
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
				//ADD TO SESSION
				request.setAttribute("user", user);
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

}
