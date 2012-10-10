package ixcode.platform.dropwizard;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

/**
 * To force the browser to use utf8 you need to add
 * ;charset=UTF-8
 * To the media type header.
 * This does that for you
 */
public class Utf8ResponseFilter implements Filter {

    @Override public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        filterChain.doFilter(servletRequest, new Utf8ServletResponse((HttpServletResponse)servletResponse));
    }

    @Override public void destroy() {

    }

    private static class Utf8ServletResponse implements HttpServletResponse {

        private final HttpServletResponse delegate;

        private Utf8ServletResponse(HttpServletResponse delegate) {
            this.delegate = delegate;
        }

        @Override public void addCookie(Cookie cookie) {
            delegate.addCookie(cookie);
        }

        @Override public boolean containsHeader(String s) {
            return delegate.containsHeader(s);
        }

        @Override public String encodeURL(String s) {
            return delegate.encodeURL(s);
        }

        @Override public String encodeRedirectURL(String s) {
            return delegate.encodeRedirectURL(s);
        }

        @Override public String encodeUrl(String s) {
            return delegate.encodeUrl(s);
        }

        @Override public String encodeRedirectUrl(String s) {
            return delegate.encodeRedirectUrl(s);
        }

        @Override public void sendError(int i, String s) throws IOException {
            delegate.sendError(i, s);
        }

        @Override public void sendError(int i) throws IOException {
            delegate.sendError(i);
        }

        @Override public void sendRedirect(String s) throws IOException {
            delegate.sendRedirect(s);
        }

        @Override public void setDateHeader(String s, long l) {
            delegate.setDateHeader(s, l);
        }

        @Override public void addDateHeader(String s, long l) {
            delegate.addDateHeader(s, l);
        }

        @Override public void setHeader(String s, String s1) {
            delegate.setHeader(s, s1);
        }

        @Override public void addHeader(String name, String value) {
            if ("Content-Type".equals(name)) {
                delegate.addHeader(name, value + ";charset=UTF-8");
            }
            delegate.addHeader(name, value);
        }

        @Override public void setIntHeader(String s, int i) {
            delegate.setIntHeader(s, i);
        }

        @Override public void addIntHeader(String s, int i) {
            delegate.addIntHeader(s, i);
        }

        @Override public void setStatus(int i) {
            delegate.setStatus(i);
        }

        @Override public void setStatus(int i, String s) {
            delegate.setStatus(i, s);
        }

        @Override public int getStatus() {
            return delegate.getStatus();
        }

        @Override public String getHeader(String s) {
            return delegate.getHeader(s);
        }

        @Override public Collection<String> getHeaders(String s) {
            return delegate.getHeaders(s);
        }

        @Override public Collection<String> getHeaderNames() {
            return delegate.getHeaderNames();
        }

        @Override public String getCharacterEncoding() {
            return delegate.getCharacterEncoding();
        }

        @Override public String getContentType() {
            return delegate.getContentType();
        }

        @Override public ServletOutputStream getOutputStream() throws IOException {
            return delegate.getOutputStream();
        }

        @Override public PrintWriter getWriter() throws IOException {
            return delegate.getWriter();
        }

        @Override public void setCharacterEncoding(String s) {
            delegate.setCharacterEncoding(s);
        }

        @Override public void setContentLength(int i) {
            delegate.setContentLength(i);
        }

        @Override public void setContentType(String s) {
            delegate.setContentType(s);
        }

        @Override public void setBufferSize(int i) {
            delegate.setBufferSize(i);
        }

        @Override public int getBufferSize() {
            return delegate.getBufferSize();
        }

        @Override public void flushBuffer() throws IOException {
            delegate.flushBuffer();
        }

        @Override public void resetBuffer() {
            delegate.resetBuffer();
        }

        @Override public boolean isCommitted() {
            return delegate.isCommitted();
        }

        @Override public void reset() {
            delegate.reset();
        }

        @Override public void setLocale(Locale locale) {
            delegate.setLocale(locale);
        }

        @Override public Locale getLocale() {
            return delegate.getLocale();
        }
    }
}