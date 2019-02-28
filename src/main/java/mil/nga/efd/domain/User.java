package mil.nga.efd.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * "user" is a reserved word in Derby.  Table renamed to "user_accounts".
 * 
 * @author L. Craig Carpenter
 */
@Entity
@Table(name = "user_accounts")
public class User implements Serializable {
  
	/**
	 * Eclipse-generated serialVersionUID
	 */
	private static final long serialVersionUID = -749926502715988475L;
	
	private Long id;
    private String username;
    private String password;
  
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
}
