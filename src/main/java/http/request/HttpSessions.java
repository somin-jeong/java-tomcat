package http.request;

import java.util.HashMap;
import java.util.Map;

public class HttpSessions {
    private static final Map<String, HttpSession> sessions = new HashMap<>();

    public static HttpSession getSession(String id){
        HttpSession httpSession = sessions.get(id);
        if(httpSession == null){
            httpSession = new HttpSession(id);
        }
        return httpSession;
    }
    public static void remove(String id){
        sessions.remove(id);
    }
}
