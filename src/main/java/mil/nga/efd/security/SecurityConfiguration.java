package mil.nga.efd.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Class responsible for initializing the configuration parameters associated 
 * with the Spring Security implementation for the project.
 * 
 * @author L. Craig Carpenter
 */
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	/**
	 * Injected handler for dealing with failed authentication attempts.
	 */
	@Autowired
    private LoggingAccessDeniedHandler accessDeniedHandler;
	
	/**
	 * The default password encoder.
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/** 
	 * Create the default PasswordEncoder
	 * @return The default password encoder (currently BCrypt)
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * This method configures the authentication mechanism (i.e. where to 
	 * store users, password schemes, and default users.
	 */
	@Override
	protected void configure (AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.passwordEncoder(passwordEncoder)
				.withUser("user")
					.password("$2a$10$.Sb0TAuUvKUS.xX33Lp09udne79gGSlcOBXXWXc8cDjXrm8X40QFy")
					.roles("USER")
				.and()
				.withUser("manager")
					.password("$2a$10$.Sb0TAuUvKUS.xX33Lp09udne79gGSlcOBXXWXc8cDjXrm8X40QFy")
					.roles("ADMIN");

	}
	
	/**
	 * This method configures the security
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		if (http != null) {
			http
				.authorizeRequests()
					.antMatchers(
                        "/js/**",
                        "/css/**",
                        "/images/**",
                        "/webjars/**",
                        "/**/favicon.ico").permitAll()
					.antMatchers("/admin/**")
						.hasRole("ADMIN")
					.anyRequest()
						.authenticated()
				.and()
				.formLogin()
					.loginPage("/login")
					.defaultSuccessUrl("/index")
					.permitAll()
			    .and()
	            .logout()
	                .invalidateHttpSession(true)
	                .clearAuthentication(true)
	                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	                .logoutSuccessUrl("/login?logout")
	                .permitAll()
	            .and()
	            .exceptionHandling()
	                    .accessDeniedHandler(accessDeniedHandler);
		}
	}
}
