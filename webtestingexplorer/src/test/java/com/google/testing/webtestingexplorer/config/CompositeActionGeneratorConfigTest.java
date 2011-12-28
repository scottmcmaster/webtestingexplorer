// Copyright 2011 Google Inc. All Rights Reserved.

package com.google.testing.webtestingexplorer.config;

import static org.junit.Assert.*;

import com.google.common.collect.Sets;
import com.google.testing.webtestingexplorer.actions.Action;
import com.google.testing.webtestingexplorer.actions.BackAction;
import com.google.testing.webtestingexplorer.actions.ForwardAction;
import com.google.testing.webtestingexplorer.identifiers.WebElementWithIdentifier;

import org.junit.Test;

import java.util.Set;

/**
 * Tests for the {@link CompositeActionGeneratorConfig} class.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class CompositeActionGeneratorConfigTest {

  @Test
  public void matches() {
    CompositeActionGeneratorConfig config = createCompositeConfig(true);
    assertTrue(config.matches(null));
    Set<Action> result = config.generateActions(null);
    assertTrue(result.contains(new BackAction()));
    assertTrue(result.contains(new ForwardAction()));
  }

  @Test
  public void noMatches() {
    CompositeActionGeneratorConfig config = createCompositeConfig(false);
    assertFalse(config.matches(null));
  }
  
  private CompositeActionGeneratorConfig createCompositeConfig(final boolean shouldMatch) {
    CompositeActionGeneratorConfig config = new CompositeActionGeneratorConfig(
        new ActionGeneratorConfig() {
          
          @Override
          public boolean matches(WebElementWithIdentifier elementWithId) {
            return shouldMatch;
          }
          
          @Override
          public Set<Action> generateActions(WebElementWithIdentifier elementWithId) {
            return Sets.<Action>newHashSet(new BackAction());
          }
        },
        new ActionGeneratorConfig() {
          
          @Override
          public boolean matches(WebElementWithIdentifier elementWithId) {
            return true;
          }
          
          @Override
          public Set<Action> generateActions(WebElementWithIdentifier elementWithId) {
            return Sets.<Action>newHashSet(new ForwardAction());
          }
        });
    return config;
  }  
}
