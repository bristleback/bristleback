package pl.bristleback.server.bristle.api.annotations;

import pl.bristleback.server.bristle.action.ActionExecutionStage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * //@todo class description
 * <p/>
 * Created on: 24.01.13 20:16 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target({ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Interceptor {

  ActionExecutionStage[] stages();
}
