package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.UserDB;
import model.User;
import service.UserService;

public class FriendshipController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("action");
		int userID;
		final String sessionID = request.getSession().getId().intern();
		synchronized(sessionID) {
			userID = ((User)request.getSession().getAttribute("user")).getID();
		}
		
	
		if (action != null && !action.trim().isEmpty()) {
			switch(action) {
				case "show":
					showHandler(userID,request,response);
					break;
				case "search" : 
					searchHandler(userID,request,response);
					break;
				case "add" : 
					addHandler(userID,request,response);
					break;
				case "accept" : 
					acceptHandler(userID,request,response);
					break;
				case "remove" : 
					removeHandler(userID,request,response);
					break;
				default :
					/* Do Nothing */
					break;
			}
		}else {
			getServletContext().getRequestDispatcher(request.getHeader("referer").replaceAll(".*social", "")).forward(request,response);
		}
	}

	private void removeHandler(int userID, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int friendID = Integer.parseInt( request.getParameter("id") );
		UserService.removeFriend(userID,friendID);
		response.sendRedirect("/social/friendship?action=show");
	}

	private void addHandler(int userID, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int friendID = Integer.parseInt( request.getParameter("id") );
		UserService.addFriend(userID,friendID);
		
		response.sendRedirect("/social/friendship?action=show");
		
	}

	private void acceptHandler(int userID, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
			int friendID = Integer.parseInt( request.getParameter("id") );
			UserService.acceptFriend(userID,friendID);
			
			response.sendRedirect("/social/friendship?action=show");
			
		}
	private void showHandler(int userID, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = null;
		HttpSession session = request.getSession();
		final String sessionID = session.getId().intern();
		List<User> receivedRequests = new ArrayList<>();
		List<User> sentRequests = new ArrayList<>();
		
		synchronized(sessionID){
			 user = (User)session.getAttribute("user");
		}
		user.setFriends(UserDB.getFriendsByID(userID));
		UserService.getRequestsThatRecieved(user.getID(), receivedRequests);
		UserService.getRequestsThatSent(user.getID(),sentRequests);
		
		synchronized(sessionID){
			session.setAttribute("receivedRequests", receivedRequests);
			session.setAttribute("sentRequests", sentRequests);
		}
		getServletContext().getRequestDispatcher("/friends.jsp").forward(request, response);
		
	}

	private void searchHandler(int ID,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String searchAbout = request.getParameter("search");
		List<User> results = new ArrayList<>();
		Map<Integer,String> states = new HashMap<>();
		
		UserService.getSearchFriendsResult(ID,searchAbout,results,states);
		
		request.setAttribute("searchResult", results);
		request.setAttribute("searchResultState", states);
		
		getServletContext().getRequestDispatcher("/searchFriend.jsp").forward(request, response);
	}

}
