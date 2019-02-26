package edu.acc.j2ee.hubbubjpa.domain;

import edu.acc.j2ee.hubbubjpa.domain.Post;
import edu.acc.j2ee.hubbubjpa.domain.Profile;
import edu.acc.j2ee.hubbubjpa.domain.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-02-25T17:34:08")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, String> password;
    public static volatile ListAttribute<User, User> followers;
    public static volatile ListAttribute<User, User> followees;
    public static volatile SingularAttribute<User, Profile> profile;
    public static volatile ListAttribute<User, Post> posts;
    public static volatile SingularAttribute<User, String> username;

}