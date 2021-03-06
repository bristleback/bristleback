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

package pl.bristleback.server.bristle.action.client;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.annotations.ClientActionClass;
import pl.bristleback.server.bristle.conf.resolver.action.client.ClientActionClassesResolver;
import pl.bristleback.server.bristle.action.exception.ClientActionException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-26 20:12:47 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ClientActionClasses {

  private Map<String, ClientActionClassInformation> actionClasses;

  @Inject
  private ClientActionClassesResolver actionClassesResolver;

  @PostConstruct
  private void init() {
    actionClasses = actionClassesResolver.resolve();
  }

  public ClientActionClassInformation getClientActionClass(Class<?> actionClass) {
    ClientActionClass clientActionClassAnnotation = actionClass.getAnnotation(ClientActionClass.class);
    if (clientActionClassAnnotation == null) {
      throw new ClientActionException(ClientActionClass.class.getSimpleName() + " annotation not found in " + actionClass.getName() + " class.");
    }
    String actionClassName = getActionClassName(actionClass, clientActionClassAnnotation);

    return actionClasses.get(actionClassName);
  }

  private String getActionClassName(Class<?> actionClass, ClientActionClass clientActionClassAnnotation) {
    if (StringUtils.isNotBlank(clientActionClassAnnotation.name())) {
      return clientActionClassAnnotation.name();
    }
    return actionClass.getSimpleName();
  }

  public Map<String, ClientActionClassInformation> getActionClasses() {
    return actionClasses;
  }
}
