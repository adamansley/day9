package edu.acc.j2ee.hubbubjpa.domain;

import edu.acc.j2ee.hubbubjpa.HashTool;
import edu.acc.j2ee.hubbubjpa.UserBean;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Transactional
public class HubbubJpaDao {
    private final EntityManagerFactory emf;

    public HubbubJpaDao(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public User addUser(UserBean userBean) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            User user = new User();
            user.setUsername(userBean.getUsername());
            user.setPassword(HashTool.hash(userBean.getPassword()));
            Profile profile = new Profile();
            em.persist(profile);
            user.setProfile(profile);
            em.persist(user);
            em.getTransaction().commit();
            return user;
        }
        catch (Exception e) {
            em.getTransaction().rollback();
            return null;
        }
        finally{
            em.close();
        }
        
    }
    
    public void addPost(Post p) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            em.getTransaction().rollback();
        }
        finally {
            em.close();
        }
    }
    
    public void addProfile(Profile profile) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(profile);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            em.getTransaction().rollback();
        }
        finally {
            em.close();
        }
    }
    
    public User findUserByUsername(String username) {
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, username);
        em.close();
        return user;
    }      
    
    public User findUserByUserBean(UserBean bean) {
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, bean.getUsername());
        em.close();
        if (user != null && HashTool.compare(bean.getPassword(), user.getPassword()))
            return user;
        else return null;
    }
    
    public boolean userExists(String username) {
        return ! (this.findUserByUsername(username) == null);
    }
    
    public List<Post> findPostsByPage(int offset, int limit) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNamedQuery("Post.findAll")
                .setFirstResult(offset)
                .setMaxResults(limit);
     
        List<Post> posts = q.getResultList();
        em.close();
        return posts;
    }
    
    public List<Post> findPostsByAuthorAndPage(User author, int offset, int limit) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNamedQuery("Post.findByAuthor")
                .setParameter("author", author)
                .setFirstResult(offset)
                .setMaxResults(limit);
        List<Post> posts = q.getResultList();
        em.close();
        return posts;
    }
    
    public long countAllPosts() {
        EntityManager em = emf.createEntityManager();
        long count = (long)em.createNamedQuery("Post.countAll").getSingleResult();
        em.close();
        return count;
    }
    
    public long countAuthorPosts(User author) {
        EntityManager em = emf.createEntityManager();
        long count = (long)em.createNamedQuery("Post.countByAuthor")
                .setParameter("author", author)
                .getSingleResult();
        em.close();
        return count;
    }
    
    public void updateProfileFor(User user, Profile temp) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Profile current = user.getProfile();
            current.setFirstName(temp.getFirstName());
            current.setLastName(temp.getLastName());
            current.setEmail(temp.getEmail());
            current.setBiography(temp.getBiography());
            em.merge(current);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            em.getTransaction().rollback();
        }
        finally {
            em.close();
        }
    }
    
    public void updateAvatarFor(User user, String mime, InputStream is) throws IOException {
        EntityManager em = emf.createEntityManager();
        byte[] imgdata = imageFromStream(is);
        try {
            em.getTransaction().begin();
            Profile profile = user.getProfile();
            profile.setAvatar(imgdata);
            profile.setMime(mime);
            em.merge(profile);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            em.getTransaction().rollback();
        }
        finally {
            em.close();
        }
    }    
    
    private byte[] imageFromStream(InputStream is) throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[0xFFFF];
            for (int len; (len = is.read(buffer)) != -1;)
                os.write(buffer, 0, len);
            os.flush();
            return os.toByteArray();
        }          
    }
  
    public void revertAvatarFor(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Profile profile = user.getProfile();
            profile.setAvatar(null);
            profile.setMime(null);
            em.merge(profile);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            em.getTransaction().rollback();
        }
        finally {
            em.close();
        }
    }
    
    public void follow(User user, User target) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            user.getFollowees().add(target);
            target.getFollowers().add(user);
            em.merge(user);
            em.merge(target);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            em.getTransaction().rollback();
        }
        finally {
            em.close();
        }
    }
    
    public void unfollow(User user, User target) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            user.getFollowees().remove(target);
            target.getFollowers().remove(user);
            em.merge(user);
            em.merge(target);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            em.getTransaction().rollback();
        }
        finally {
            em.close();
        }
    }
}
