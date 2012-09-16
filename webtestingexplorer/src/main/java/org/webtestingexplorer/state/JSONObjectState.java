package org.webtestingexplorer.state;

import java.util.List;

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

  public JSONObjectState(JSONObject json) {
    this.json = json;
  }
  
  @Override
  public List<StateDifference> diff(State otherState) {
    if (!(otherState instanceof JSONObjectState)) {
      throw new IllegalArgumentException("Invalid state class: " + otherState.getClass().getName());
    }
    
    List<StateDifference> result = Lists.newArrayList();
    JSONObjectState other = (JSONObjectState) otherState;
    JSONObject otherJson = other.json;
    for (String name : JSONObject.getNames(json)) {
      Object value = null;
      try {
        value = json.get(name);
      } catch (JSONException e) {
        // This one can't happen.
      }
      
      Object otherValue = null;
      try {
        otherValue = otherJson.get(name);
      } catch (JSONException e) {
        result.add(new MissingPropertyStateDifference(name, value, null));
        continue;
      }
      
      if (!Objects.equal(value, otherValue)) {
        result.add(new PropertyValueStateDifference(null, name, value, otherValue));
      }
    }
    
    for (String name : JSONObject.getNames(otherJson)) {
      Object otherValue = null;
      try {
        otherValue = otherJson.get(name);
      } catch (JSONException e) {
        // This one can't happen.
      }

      try {
        json.get(name);
      } catch (JSONException e) {
        result.add(new MissingPropertyStateDifference(name, null, otherValue));
        continue;
      }
    }
    return result;
  }
}
