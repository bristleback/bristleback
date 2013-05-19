/*
 * Bristleback Websocket Framework - Copyright (c) 2010-2013 http://bristleback.pl
 * ---------------------------------------------------------------------------
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 3 of the License, or (at your
 * option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but without any warranty; without even the implied warranty of merchantability
 * or fitness for a particular purpose.
 * You should have received a copy of the GNU Lesser General Public License along
 * with this program; if not, see <http://www.gnu.org/licenses/lgpl.html>.
 * ---------------------------------------------------------------------------
 */

package pl.bristleback.server.bristle.conf.resolver.action;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.ClientAction;
import pl.bristleback.server.bristle.conf.BristleInitializationException;

import java.lang.reflect.Method;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-23 16:40:22 <br/>
 *
 * @author Wojciech Niemiec
 */
public final class ActionResolvingUtils {

  private static final String ALLOWED_ACTION_NAME_PATTERN = "^[a-zA-Z_][a-zA-Z0-9_]*$";

  private ActionResolvingUtils() {
    throw new UnsupportedOperationException();
  }

  public static String resolveClientActionName(Method actionMethod, boolean validate) {
    ClientAction actionAnnotation = AnnotationUtils.findAnnotation(actionMethod, ClientAction.class);
    if (actionAnnotation == null || StringUtils.isBlank(actionAnnotation.value())) {
      return actionMethod.getName();
    }
    if (validate) {
      validateActionNameFromAnnotationValue(actionAnnotation.value());
    }
    return actionAnnotation.value();
  }

  public static String resolveActionName(Method actionMethod) {
    Action actionAnnotation = actionMethod.getAnnotation(Action.class);
    if (actionAnnotation == null || StringUtils.isBlank(actionAnnotation.name())) {
      return actionMethod.getName();
    }
    validateActionNameFromAnnotationValue(actionAnnotation.name());
    return actionAnnotation.name();
  }

  private static void validateActionNameFromAnnotationValue(String actionName) {
    if (!actionName.matches(ALLOWED_ACTION_NAME_PATTERN)) {
      throw new BristleInitializationException("Action name must match pattern \"" + ALLOWED_ACTION_NAME_PATTERN + "\"");
    }
  }

  public static String resolveFullName(String actionName, String actionClassName) {
    return actionClassName + pl.bristleback.server.bristle.utils.StringUtils.DOT_AS_STRING + actionName;
  }
}

