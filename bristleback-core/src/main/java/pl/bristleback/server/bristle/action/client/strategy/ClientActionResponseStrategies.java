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

package pl.bristleback.server.bristle.action.client.strategy;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.action.ClientActionSender;
import pl.bristleback.server.bristle.conf.resolver.action.client.ClientActionResponsesResolver;
import pl.bristleback.server.bristle.utils.ReflectionUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-07 21:13:51 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ClientActionResponseStrategies {

  private Map<Class, ClientActionSender> strategies;

  @Inject
  private ClientActionResponsesResolver clientActionResponsesResolver;

  public ClientActionSender getStrategy(Class responseType) {
    return ReflectionUtils.chooseBestClassStrategy(strategies, responseType);
  }

  @PostConstruct
  private void init() {
    strategies = clientActionResponsesResolver.resolve();
  }

  public Map<Class, ClientActionSender> getStrategies() {
    return strategies;
  }

  public void setStrategies(Map<Class, ClientActionSender> strategies) {
    this.strategies = strategies;
  }
}
