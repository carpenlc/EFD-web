package mil.nga.efd.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SiteController {

	  @GetMapping("/")
	  public String getRoot () {
	    return "index";
	  }
	  
	  @GetMapping("/index")
	  public String getIndex () {
	    return "index";
	  }
	  
	  @GetMapping("/admin")
	  public String getAdmin () {
	    return "/admin/index";
	  }
	  
	  @GetMapping("/access-denied")
	  public String getAccessDenied () {
	    return "/error/access-denied";
	  }
}
