package edu.acc.j2ee.hubbubjpa.domain;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-02-25T17:34:08")
@StaticMetamodel(Profile.class)
public class Profile_ { 

    public static volatile SingularAttribute<Profile, String> firstName;
    public static volatile SingularAttribute<Profile, String> lastName;
    public static volatile SingularAttribute<Profile, Date> joined;
    public static volatile SingularAttribute<Profile, String> mime;
    public static volatile SingularAttribute<Profile, String> biography;
    public static volatile SingularAttribute<Profile, byte[]> avatar;
    public static volatile SingularAttribute<Profile, Integer> id;
    public static volatile SingularAttribute<Profile, String> email;

}