package pl.bristleback.server.bristle.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation allows to define non default serialization operations, for example, by providing validation.
 * Multiple non default serialization operations may be defined per each target type. If operation name is specified,
 * one of <code>sendNamedMessage()</code> methods from {@link pl.bristleback.server.bristle.message.ConditionObjectSender ConditionObjectSender} class
 * must be used. If operation name is empty, such non default serialization definition is considered as default for given target type
 * and every object of that type serialized by annotated object sender will be serialized using information from this annotation.
 * It is possible to define serialization of generic containers (maps, collections and arrays) by specifying {@link Serialize#containerElementClass()}.
 * Unfortunately, containers of parametrized types cannot be defined.
 * <p/>
 * Created on: 2011-09-04 16:40:46 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target({ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Serialize {

  /**
   * Name of this serialization definition.
   * May remain empty, then all objects of type equal to this definition target type will be serialization using this definition.
   *
   * @return name of the serialization definition.
   */
  String serializationName() default "";

  /**
   * Type of object covered by this serialization definition.
   *
   * @return type of object covered by this serialization definition.
   */
  Class target() default Object.class;

  /**
   * Because of type erasure, specifying container element type cannot be done using {@link Serialize#target()} method.
   * This attribute can be helpful in such situations.
   *
   * @return container element class.
   */
  Class containerElementClass() default Object.class;

  /**
   * Serialization definition of nested properties.
   *
   * @return serialization definition of nested properties.
   */
  Property[] properties() default {};

  /**
   * This property should be used when serialized object is of simple type.
   *
   * @return true if this simple action parameter is required, false otherwise.
   */
  boolean required() default false;
}
