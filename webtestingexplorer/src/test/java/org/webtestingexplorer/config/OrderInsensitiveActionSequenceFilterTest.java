/*
Copyright 2012 Google Inc. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.webtestingexplorer.config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;

import org.junit.Before;
import org.junit.Test;
import org.webtestingexplorer.actions.Action;
import org.webtestingexplorer.actions.ActionSequence;
import org.webtestingexplorer.actions.BackAction;
import org.webtestingexplorer.actions.ForwardAction;
import org.webtestingexplorer.actions.SetTextAction;
import org.webtestingexplorer.config.OrderInsensitiveActionSequenceFilter;
import org.webtestingexplorer.identifiers.NameWebElementIdentifier;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;

/**
 * Tests for the {@link OrderInsensitiveActionSequenceFilter} class.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class OrderInsensitiveActionSequenceFilterTest {

  private OrderInsensitiveActionSequenceFilter filter;
  private Deque<ActionSequence> existingActionSequences;
  
  @Before
  public void setUp() {
    Set<Action> orderInsensitiveActions = Sets.newHashSet();
    orderInsensitiveActions.add(createSetTextAction("foo", "blah"));
    orderInsensitiveActions.add(createSetTextAction("bar", "baz"));
    
    filter = new OrderInsensitiveActionSequenceFilter(orderInsensitiveActions );
    existingActionSequences = new ArrayDeque<ActionSequence>();
    ActionSequence existing = new ActionSequence();
    existing.addAction(new BackAction());
    existing.addAction(createSetTextAction("bar", "baz"));
    existing.addAction(createSetTextAction("foo", "blah"));
    existing.addAction(new ForwardAction());
    existingActionSequences.add(existing);    
  }
  
  /**
   * We want to make sure that all actions from the new sequence are already
   * covered. In other words, if A and B are order-insensitive, the following
   * sequences should NOT be considered the same:
   *   <A,A>
   *   <A,B>
   */
  @Test
  public void actionNotInExisting() {
    ActionSequence notRedundant2 = new ActionSequence();
    notRedundant2.addAction(new BackAction());
    notRedundant2.addAction(createSetTextAction("foo", "blah"));
    notRedundant2.addAction(createSetTextAction("foo", "blah"));
    notRedundant2.addAction(new ForwardAction());
    assertTrue(filter.shouldExplore(notRedundant2, existingActionSequences));
  }

  @Test
  public void sameInsensitiveActionsWithInterveningAction() {
    ActionSequence notRedundant1 = new ActionSequence();
    notRedundant1.addAction(new BackAction());
    notRedundant1.addAction(createSetTextAction("foo", "blah"));
    notRedundant1.addAction(new ForwardAction());
    notRedundant1.addAction(createSetTextAction("bar", "baz"));
    assertTrue(filter.shouldExplore(notRedundant1, existingActionSequences));
  }

  @Test
  public void sameInsensitiveActionsDifferentOrder() {
    ActionSequence redundant = new ActionSequence();
    redundant.addAction(new BackAction());
    redundant.addAction(createSetTextAction("foo", "blah"));
    redundant.addAction(createSetTextAction("bar", "baz"));
    redundant.addAction(new ForwardAction());
    assertFalse(filter.shouldExplore(redundant , existingActionSequences));
  }

  private SetTextAction createSetTextAction(String name, String text) {
    return new SetTextAction(new NameWebElementIdentifier(name), text);
  }
}
