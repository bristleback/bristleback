package pl.bristleback.server.bristle.serialization.system.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * If there is more than one serialization to be specified in {@link pl.bristleback.server.bristle.message.ConditionObjectSender} object,
 * this container annotation gathers multiple {@link Serialize} annotations.
 * Note that there can be maximum one serialization per payload type.
 * <p/>
 * <p/>
 * Created on: 15.12.12 21:53 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target({ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface SerializeBundle {

  Serialize[] value();
}
