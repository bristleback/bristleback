package pl.bristleback.server.bristle.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation should be placed before client action parameters that are not meant to be serialized.
 * <p/>
 * Created on: 2012-07-18 22:33:22 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target({ElementType.PARAMETER})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Ignore {

}
