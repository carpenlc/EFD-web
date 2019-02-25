package mil.nga.efd.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * Handler class for redirecting authentication failures to the access-denied 
 * error page.
 * 
 * @author L. Craig Carpenter
 */
@Component
public class LoggingAccessDeniedHandler implements AccessDeniedHandler {
	
	/**
	 * Set up the log4j system for use throughout the class.
	 */
	private static Logger log = LoggerFactory.getLogger(
			LoggingAccessDeniedHandler.class);

	/**
	 * Overridden method used to log the failed authorization attempt 
	 * and redirect to the access denied page.
	 */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex) 
                    		   throws IOException, ServletException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            log.info("Authentication failed for user [ " + auth.getName()
                    + " ] trying to access protected resource [ "
                    + request.getRequestURI()
                    + " ].");
        }

        response.sendRedirect(request.getContextPath() + "/access-denied");
    }
}
