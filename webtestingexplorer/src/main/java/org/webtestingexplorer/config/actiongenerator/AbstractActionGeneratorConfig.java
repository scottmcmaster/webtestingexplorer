package org.webtestingexplorer.config.actiongenerator;

import java.util.Set;

import org.webtestingexplorer.actions.Action;
import org.webtestingexplorer.config.ActionGeneratorConfig;
import org.webtestingexplorer.driver.WebDriverWrapper;
import org.webtestingexplorer.identifiers.WebElementWithIdentifier;

import com.google.common.collect.Sets;

/**
 * Base class for common action generator config implementations.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public abstract class AbstractActionGeneratorConfig implements ActionGeneratorConfig {

  @Override
  public Set<Action> generateActions(WebElementWithIdentifier elementWithId) {
    return Sets.newHashSet();
  }

  @Override
  public boolean isActive(WebDriverWrapper driver) {
    return true;
  }
}
