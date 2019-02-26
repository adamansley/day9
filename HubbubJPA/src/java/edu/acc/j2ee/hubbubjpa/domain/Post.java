package edu.acc.j2ee.hubbubjpa.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "POSTS")
@NamedQueries({
    @NamedQuery(name = "Post.findAll", query = "SELECT p FROM Post p ORDER BY p.posted DESC")
    ,@NamedQuery(name = "Post.findByAuthor", query = "SELECT p FROM Post p WHERE p.author = :author ORDER BY p.posted DESC")
    ,@NamedQuery(name = "Post.findById", query = "SELECT p FROM Post p WHERE p.id = :id")
    ,@NamedQuery(name = "Post.countAll", query = "SELECT COUNT(p.id) FROM Post p")
    ,@NamedQuery(name = "Post.countByAuthor", query = "SELECT COUNT(p.id) FROM Post p WHERE p.author = :author")
})
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Column(name = "CONTENT")
    private String content;
    
    @Column(name = "POSTED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date posted;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    
    @JoinColumn(name = "AUTHOR", referencedColumnName = "USERNAME")
    @ManyToOne(optional = false)
    private User author;

    public Post() {
        posted = new Date();
    }
    
    public Post(String content, User author) {
        this();
        this.content = content;
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPosted() {
        return posted;
    }

    public void setPosted(Date posted) {
        this.posted = posted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Post)) {
            return false;
        }
        Post other = (Post) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Post[id=" + id + "]";
    }
    
}
