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

package pl.bristleback.server.bristle.action.interceptor.matcher;

import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.action.ActionInterceptorMatcher;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Basic class for all pattern action interceptor matcher classes.
 * All pattern action interceptor matcher implementations use {@link java.util.regex.Pattern} when preparing and executing patterns.
 * <p/>
 * Created on: 13.02.13 19:37 <br/>
 *
 * @author Wojciech Niemiec
 */
public abstract class AbstractPatternMatcher implements ActionInterceptorMatcher {

  private List<String> patterns;

  private List<Pattern> parsedPatterns = new ArrayList<Pattern>();

  @PostConstruct
  public void parsePatterns() {
    if (!parsedPatterns.isEmpty()) {
      return;
    }
    for (String pattern : patterns) {
      parsedPatterns.add(Pattern.compile(pattern));
    }
  }

  protected abstract String getTestedString(ActionInformation actionInformation);

  @Override
  public boolean isInterceptorApplicable(ActionInformation actionInformation, Class<? extends ActionInterceptor> interceptor) {
    String testedString = getTestedString(actionInformation);

    for (Pattern parsedPattern : parsedPatterns) {
      if (parsedPattern.matcher(testedString).matches()) {
        return true;
      }
    }
    return false;
  }

  public void setPatterns(String... patterns) {
    this.patterns = Arrays.asList(patterns);
  }

  public void setParsedPatterns(Pattern... parsedPatterns) {
    this.parsedPatterns = Arrays.asList(parsedPatterns);
  }
}
