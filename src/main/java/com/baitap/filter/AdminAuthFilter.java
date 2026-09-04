package com.baitap.filter;

import com.baitap.model.User;
import com.baitap.model.UserRole;
import com.baitap.util.LoginRedirect;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = "/admin/*")
public class AdminAuthFilter implements Filter {
    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request; HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false); Object value = session == null ? null : session.getAttribute("account");
        User account = value instanceof User ? (User) value : null;
        if (account == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
        boolean userManagement = req.getRequestURI().startsWith(req.getContextPath() + "/admin/user/");
        if (!account.isActive() || !UserRole.canAccessAdmin(account.getRoleid()) || (userManagement && account.getRoleid() != UserRole.ADMIN)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            String path = LoginRedirect.pathForRole(req.getContextPath(), account.getRoleid());
            req.setAttribute("returnPath", path.substring(req.getContextPath().length()));
            req.getRequestDispatcher("/WEB-INF/views/access-denied.jsp").forward(req, resp);
            return;
        }
        chain.doFilter(request, response);
    }
}
