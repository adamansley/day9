package edu.acc.j2ee.hubbubjpa;

import edu.acc.j2ee.hubbubjpa.domain.HubbubJpaDao;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class StartupListener implements ServletContextListener, HttpSessionListener {
    private static int defaultPageSize;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        defaultPageSize = Integer.parseInt(sce.getServletContext().getInitParameter("page.size"));
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HubbubJPAPU");
        HubbubJpaDao dao = new HubbubJpaDao(emf);
        sce.getServletContext().setAttribute("dao", dao);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setAttribute("pageSize", defaultPageSize);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
    }

}
