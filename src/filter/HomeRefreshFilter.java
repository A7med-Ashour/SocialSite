package filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.Post;
import model.User;
import service.PostService;

public class HomeRefreshFilter implements Filter {


	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession();
		User user = null;
		final String sessionID = httpRequest.getSession().getId().intern();
		synchronized(sessionID) {
			user = (User)session.getAttribute("user");
			List<Post> wallPosts = PostService.getWallPostsByID(user.getID());
			session.setAttribute("wallPosts", wallPosts);
		}
		
		chain.doFilter(request, response);
	}

}
