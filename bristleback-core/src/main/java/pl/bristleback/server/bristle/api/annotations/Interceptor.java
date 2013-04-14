package pl.bristleback.server.bristle.api.annotations;

import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.api.action.ActionInterceptorContextResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Beans marked with this annotation must implement {@link pl.bristleback.server.bristle.api.action.ActionInterceptor} interface.
 * This annotation defines action execution stages, to which marked interceptor applies
 * and the class that prepares interception execution context.
 * Interceptor will be invoked <strong>AFTER</strong> execution stages listed in <code>stages</code> annotation property.
 * Following stages can be intercepted:
 * <ul>
 * <li>After {@link ActionExecutionStage#ACTION_EXTRACTION}</li>
 * <li>After {@link ActionExecutionStage#PARAMETERS_EXTRACTION}</li>
 * <li>After {@link ActionExecutionStage#ACTION_EXECUTION}</li>
 * </ul>
 * <p/>
 * Created on: 24.01.13 20:16 <br/>
 *
 * @author Wojciech Niemiec
 * @see ActionExecutionStage
 */
@Target({ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Interceptor {

  ActionExecutionStage[] stages();

  Class<? extends ActionInterceptorContextResolver> contextResolver();
}
