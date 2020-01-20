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

import model.User;
import service.UserService;

public class FriendshipController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("action");
		int userID = ((User)request.getSession().getAttribute("user")).getID();
		
		if (action != null && !action.trim().isEmpty()) {
			switch(action) {
				case "search" : 
					searchHandler(userID,request,response);
					break;
				default :
					/* Do Nothing */
					break;
			}
		}else {
			getServletContext().getRequestDispatcher(request.getHeader("referer").replaceAll(".*social", "")).forward(request,response);
		}
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
