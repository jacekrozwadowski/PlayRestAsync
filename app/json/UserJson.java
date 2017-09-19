package json;
 
public class UserJson {
 
    public Long id;
    public String email;
    public String name;
    public String lastName;
 
 
    public UserJson(Long id, String email, String name, String lastName) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
    }
 
    public UserJson() {
    }

	@Override
	public String toString() {
		return "UserJson [id=" + id + ", email=" + email + ", name=" + name + ", lastName=" + lastName + "]";
	}
 
    
}