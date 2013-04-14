package pl.bristleback.server.bristle.api.annotations;

import pl.bristleback.server.bristle.security.authentication.AuthenticationInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Actions marked with this annotation are considered as the <code>authenticators</code>,
 * which means they are intercepted with {@link AuthenticationInterceptor}.
 * Annotated actions must return {@link pl.bristleback.server.bristle.api.users.UserDetails} implementation.
 * They also should validate input data and check if processed request contains proper credentials.
 * Additional checks like account expiration check are also performed by the authenticators.
 * Authentication interceptor uses returned user details object and creates new
 * {@link pl.bristleback.server.bristle.security.authentication.UserAuthentication} object.
 * Application creators don't have to define their own authenticator method.
 * By default, {@link pl.bristleback.server.bristle.security.authentication.AuthenticatingAction#authenticate(String, String)}
 * action is used. In that case, bean implementing {@link pl.bristleback.server.bristle.api.users.UserDetailsService}
 * must be specified in <code>&lt;bb:security/&gt;</code> tag.
 * <p/>
 * <p/>
 * Created on: 17.02.13 10:21 <br/>
 *
 * @author Wojciech Niemiec
 * @see AuthenticationInterceptor
 * @see pl.bristleback.server.bristle.security.authentication.AuthenticatingAction
 */
@Target(ElementType.METHOD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Intercept(AuthenticationInterceptor.class)
public @interface Authenticator {


}
