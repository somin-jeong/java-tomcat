package http.request;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpSession {
    private final Map<String, Object> session = new HashMap<>();
    String id;

    public HttpSession(String id) {
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setAttribute(String key, Object value){
        session.put(key, value);
    }

    public Object getAttribute(String key){
        return session.get(key);
    }

    public void removeAttribute(String key){
        session.remove(key);
    }

    public void invalidate(){
        HttpSessions.remove(this.id);
    }

}
