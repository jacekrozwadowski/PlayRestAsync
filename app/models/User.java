package models;
 
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
 
@Entity
public class User {
 
 
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO) 
    public Long id;
 
    public String email;
 
    public String name;
 
    public String lastName;
    
	public User() {
		super();
	}

	public User(String email, String name, String lastName) {
		super();
		this.email = email;
		this.name = name;
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", name=" + name + ", lastName=" + lastName + "]";
	}
    
}