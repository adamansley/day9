package edu.acc.j2ee.hubbubjpa;

import edu.acc.j2ee.hubbubjpa.domain.HubbubJpaDao;
import edu.acc.j2ee.hubbubjpa.domain.Post;
import edu.acc.j2ee.hubbubjpa.domain.Profile;
import edu.acc.j2ee.hubbubjpa.domain.User;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

public class FrontController extends HttpServlet {
    private HubbubJpaDao dao;
    
    @Override
    public void init() {
        dao = (HubbubJpaDao)this.getServletContext().getAttribute("dao");
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String action = request.getParameter("action"), destination;
        if (action == null || action.length() < 1)
            action = this.getServletConfig().getInitParameter("action.default"); 
        switch (action) {
            default:
            case "guest": destination = guest(request); break;
            case "timeline": destination = timeline(request); break;
            case "logout": destination = logout(request); break;
            case "login": destination = login(request); break;
            case "wall": destination = wall(request); break;
            case "join": destination = join(request); break;
            case "post": destination = post(request); break;
            case "profile": destination = profile(request); break;
            case "avatar": destination = avatar(request); break;
            case "revert": destination = revert(request); break;
            case "follow": destination = follow(request); break;
            case "unfollow": destination = unfollow(request); break;
        }

        String redirect = this.getServletConfig().getInitParameter("redirect.tag");
        if (destination.startsWith(redirect)) {
            response.sendRedirect("main?action=" + destination.substring(
                destination.indexOf(redirect) + redirect.length()));
            return;
        }
        
        String viewDir = this.getServletConfig().getInitParameter("view.dir");
        String viewType = this.getServletConfig().getInitParameter("view.type");
        request.getRequestDispatcher(viewDir + destination + viewType)
                .forward(request, response);
    }
    
    private String guest(HttpServletRequest request) {
        if (loggedIn(request)) return "redirect:timeline";
        List<Post> posts = dao.findPostsByPage(0, pageSize(request));
        request.setAttribute("posts", posts);
        return "guest";
    }
    
    private String timeline(HttpServletRequest request) {
        if (notLoggedIn(request)) return "redirect:guest";
        long postCount = dao.countAllPosts();
        Pager pager = Pager.of(request.getParameter("page"), pageSize(request), postCount);
        List<Post> posts = dao.findPostsByPage(pager.getOffset(), pager.getLimit());
        request.setAttribute("posts", posts);
        request.setAttribute("pager", pager);
        return "timeline";
    }
    
    private String wall(HttpServletRequest request) {
        if (notLoggedIn(request)) return "redirect:guest";
        String targetName = request.getParameter("for");
        if (targetName == null || targetName.length() == 0)
            return "redirect:timeline";
        User target = dao.findUserByUsername(targetName);
        // TODO: Figure out followees and include their posts in the count and the select
        long postCount = dao.countAuthorPosts(target);
        Pager pager = Pager.of(request.getParameter("page"), pageSize(request), postCount);
        List<Post> posts = dao.findPostsByAuthorAndPage(target, pager.getOffset(), pager.getLimit());
        request.setAttribute("posts", posts);
        request.setAttribute("pager", pager);
        request.setAttribute("target", target);
        return "wall";
    }
    
    private String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:guest";
    }
    
    private String login(HttpServletRequest request) {
        if (loggedIn(request)) return "redirect:timeline";
        if (isGet(request)) return "login";

        UserBean bean = new UserBean(request.getParameter("username"), request.getParameter("password"));
        User user = dao.findUserByUserBean(bean);
        if (user != null) {
            request.getSession().setAttribute("user", user);
            return "redirect:timeline";
        }
        request.setAttribute("flash", "Access Denied");
        return "login";
    }
    
    private String join(HttpServletRequest request) {
        if (loggedIn(request)) return "redirect:timeline";
        if (isGet(request)) return "join";
        
        String destination = "join"; // assume the worst
        String username = request.getParameter("username");
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");
        if (!password1.equals(password2)) 
            request.setAttribute("flash", "Passwords don't match");
        else if (dao.userExists(username))
                request.setAttribute("flash", "That username is unavailable");
        else {
            UserBean bean = new UserBean(username, password1);
            User newUser = dao.addUser(bean);
            request.getSession().setAttribute("user", newUser);
            destination = "redirect:timeline";
        }
        return destination;
    }
    
    private String post(HttpServletRequest request) {
        if (notLoggedIn(request)) return "redirect:timeline";
        if (isGet(request)) return "post";
        
        // POST Mapping
        String content = request.getParameter("content");
        Post post = new Post(content, sessionUser(request));
        dao.addPost(post);
        request.setAttribute("lastPost", post);
        return "post";    
    }
    
    private String profile(HttpServletRequest request) {
        if (notLoggedIn(request)) return "redirect:timeline";
        if (isGet(request)) {
            String target = request.getParameter("for");
            if (target == null || target.length() == 0)
                return "redirect:timeline";
            else {
                User targetUser = dao.findUserByUsername(target);
                request.setAttribute("target", targetUser);
            }
            return "profile";
        }
        // POST Mapping
        Profile temp = new Profile();
        temp.setFirstName(request.getParameter("firstName"));
        temp.setLastName(request.getParameter("lastName"));
        temp.setEmail(request.getParameter("email"));
        temp.setBiography(request.getParameter("biography"));
        dao.updateProfileFor(sessionUser(request), temp);
        request.setAttribute("success", "Profile updated");
        request.setAttribute("target", this.sessionUser(request));
        return "profile";
    }

    private String avatar(HttpServletRequest request) throws ServletException, IOException {
        if (this.isGet(request)) return "upload";
        // POST Mapping
        final Part filePart = request.getPart("avatar");
        String filename = filePart.getSubmittedFileName();
        String filetype = filePart.getContentType();
        if (!filetype.contains("image")) {
            request.setAttribute("flash", "The uploaded file is not an image");
            return "upload";
        }
        InputStream data = filePart.getInputStream();
        dao.updateAvatarFor(sessionUser(request), filetype, data);
        return "redirect:profile";
    }
    
    private String revert(HttpServletRequest request) {
        dao.revertAvatarFor(sessionUser(request));
        return "redirect:profile";
    }
    
    private String follow(HttpServletRequest request) {
        if (notLoggedIn(request)) return "redirect:timeline";
        
        User user = sessionUser(request);
        String targetName = request.getParameter("target");
        if (targetName == null
                || targetName.length() == 0
                || targetName.equalsIgnoreCase(user.getUsername()))
            return "redirect:wall";
        User target = dao.findUserByUsername(targetName);
        dao.follow(user, target);
        return "redirect:wall&for=" + targetName;       
    }
    
    private String unfollow(HttpServletRequest request) {
        if (notLoggedIn(request)) return "redirect:timeline";
        
        User user = sessionUser(request);
        String targetName = request.getParameter("target");
        if (targetName == null
                || targetName.length() == 0
                || targetName.equalsIgnoreCase(user.getUsername()))
            return "redirect:wall";
        User target = dao.findUserByUsername(targetName);
        dao.unfollow(user, target);
        return "redirect:wall&for=" + targetName;
    }
    
    private User sessionUser(HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        User user = (User)request.getSession().getAttribute("user");
        return user;
    }
    
    private int pageSize(HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        int pageSize = (int)request.getSession().getAttribute("pageSize");
        return pageSize;
    }
    
    private boolean notLoggedIn(HttpServletRequest request) {
        return request.getSession().getAttribute("user") == null;
    }
    
    private boolean loggedIn(HttpServletRequest request) {
        return ! notLoggedIn(request);
    }
    
    private boolean isGet(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase("GET");
    }

    // <editor-fold defaultstate="collapsed" desc="Expression servletEditorFold is undefined on line 54, column 54 in Templates/JSP_Servlet/Front_Controller.java.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
