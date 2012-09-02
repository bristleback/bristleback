package pl.bristleback.server.bristle.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Classes marked with this annotation are considered as Bristleback Client Action Classes.
 * Client action classes are high level mechanism, allowing sending messages to client using Java methods.
 * To enable client actions, Bristleback intercepts client action classes methods and transparently sends messages to client.
 * <p/>
 * Created on: 2012-05-26 10:34:25 <br/>
 *
 * @author Wojciech Niemiec
 * @see pl.bristleback.server.bristle.api.annotations.ClientAction ClientAction
 */
@Target({ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface ClientActionClass {

  String name() default "";
}
