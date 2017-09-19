package actors;
 
import json.UserJson;
 
public class UserProtocol {
 
    public enum Action {
        CREATE,GET_ONE,GET_ALL,EDIT,DELETE
    }
 
    private Action action;
    private Long id;
    private UserJson userJson;
 
 
    public UserProtocol(Action action, Long id) {
        this.action = action;
        this.id = id;
    }
 
    public UserProtocol(Action action, Long id, UserJson userJson) {
        this.action = action;
        this.id = id;
        this.userJson = userJson;
    }
 
    public UserProtocol(Action action, UserJson userJson) {
        this.action = action;
        this.userJson = userJson;
    }
 
    public UserProtocol(Action action) {
        this.action = action;
    }
 
    public Action getAction() {
        return action;
    }
 
    public UserJson getUserJson() {
        return userJson;
    }
 
    public Long getId() {
        return id;
    }
}