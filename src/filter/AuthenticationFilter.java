package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import service.UserService;

public class AuthenticationFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		/* CHECK IF USER IS ALREADY LOGIN OR NOT BEFORE ACCESS ANY USER PAGES */
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();
		boolean isAllowed = false;
		
		if(session.getAttribute("user") != null) {
			isAllowed = true;
		}else {
			Cookie [] cookies = httpRequest.getCookies();
			if(cookies != null) {
				for (Cookie c : cookies) {
					if(c.getName().equals("userEmail")) {
						isAllowed = true;
						session.setAttribute("user", UserService.getUser(c.getValue()) );
						break;
					}
				}
			}
		}
		
		if(isAllowed) {
			chain.doFilter(request, response);
		}else {
			httpResponse.sendRedirect("login.jsp");
			//httpResponse.sendError(404);
		}
		
	}

}
