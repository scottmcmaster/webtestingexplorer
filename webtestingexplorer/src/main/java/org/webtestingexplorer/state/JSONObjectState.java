package org.webtestingexplorer.state;

import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

/**
 * State implementation based on org.json's {@link JSONObject}.
 * This class is abstract because we assume the tester will supply a
 * {@link StateChecker} implementation that creates subclasses
 * perhaps by calling a JSON-returning web service.
 * 
 * @author Scott McMaster (scott.d.mcmaster@gmail.com)
 */
public abstract class JSONObjectState implements State {

  private final JSONObject json;

  /**
   * Looks for differences deeply between two JSONObjects and appends state differences
   * to the given list.
   */
  private static void appendJSONObjectDifferences(JSONObject first, JSONObject second,
      List<StateDifference> differences) {
    for (String name : JSONObject.getNames(first)) {
      Object value = null;
      try {
        value = first.get(name);
      } catch (JSONException e) {
        // This one can't happen.
      }
      
      Object otherValue = null;
      try {
        otherValue = second.get(name);
      } catch (JSONException e) {
        differences.add(new MissingPropertyStateDifference(name, value, null));
        continue;
      }
      
      if (value instanceof JSONObject && otherValue instanceof JSONObject) {
        appendJSONObjectDifferences((JSONObject) value, (JSONObject) otherValue, differences);
      } else if (!Objects.equal(value, otherValue)) {
        differences.add(new PropertyValueStateDifference(null, name, value, otherValue));
      }
    }
    
    for (String name : JSONObject.getNames(second)) {
      Object otherValue = null;
      try {
        otherValue = second.get(name);
      } catch (JSONException e) {
        // This one can't happen.
      }

      try {
        first.get(name);
      } catch (JSONException e) {
        differences.add(new MissingPropertyStateDifference(name, null, otherValue));
        continue;
      }
    }
  }
  
  public JSONObjectState(JSONObject json) {
    this.json = json;
  }
  
  @Override
  public boolean equals(Object other) {
    if (other == this) {
      // Same states
      return true;
    }
    if (!(other instanceof JSONObjectState)) {
      // Compare with non-ElementsState instance
      return false;
    }

    JSONObjectState otherState = (JSONObjectState) other;
    return this.diff(otherState).isEmpty();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(json)
        .hashCode();
  }

  @Override
  public List<StateDifference> diff(State otherState) {
    if (!(otherState instanceof JSONObjectState)) {
      throw new IllegalArgumentException("Invalid state class: " + otherState.getClass().getName());
    }
    
    List<StateDifference> result = Lists.newArrayList();
    JSONObjectState other = (JSONObjectState) otherState;
    JSONObject otherJson = other.json;
    appendJSONObjectDifferences(json, otherJson, result);
    return result;
  }
}
