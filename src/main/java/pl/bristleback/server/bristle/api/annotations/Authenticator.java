package pl.bristleback.server.bristle.api.annotations;

import pl.bristleback.server.bristle.security.authentication.AuthenticationInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * //@todo class description
 * <p/>
 * Created on: 17.02.13 10:21 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target(ElementType.METHOD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Intercept(AuthenticationInterceptor.class)
public @interface Authenticator {


}
