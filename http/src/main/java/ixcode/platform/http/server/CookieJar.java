package ixcode.platform.http.server;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class CookieJar {
    private final Map<String, String> cookieMap;


    public static CookieJar cookieJarFrom(HttpServletRequest httpRequest) {
        Map<String, String> cookieMap = new HashMap<String, String>();

        Cookie[] cookies = httpRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }
        }

        return new CookieJar(cookieMap);
    }

    private CookieJar(Map<String, String> cookieMap) {
        this.cookieMap = cookieMap;
    }


    private static String cookieKeyFrom(Cookie cookie) {
        return cookie.getDomain() + cookie.getPath() + cookie.getValue();
    }

    public boolean contains(String key) {
        return cookieMap.containsKey(key);
    }

    public String get(String key) {
        return contains(key) ? cookieMap.get(key) : null;
    }
}