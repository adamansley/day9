package edu.acc.j2ee.hubbubjpa.domain;

import edu.acc.j2ee.hubbubjpa.domain.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-02-25T17:34:08")
@StaticMetamodel(Post.class)
public class Post_ { 

    public static volatile SingularAttribute<Post, User> author;
    public static volatile SingularAttribute<Post, Integer> id;
    public static volatile SingularAttribute<Post, String> content;
    public static volatile SingularAttribute<Post, Date> posted;

}