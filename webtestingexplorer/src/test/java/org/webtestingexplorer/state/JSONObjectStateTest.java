package org.webtestingexplorer.state;

import static org.junit.Assert.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

/**
 * Tests for the {@link JSONObjectState} class.
 * 
 * @author scott.d.mcmaster@gmail.com (Scott McMaster)
 */
public class JSONObjectStateTest {

  @Test
  public void diffNone() throws JSONException {
    JSONObject firstJson = new JSONObject();
    firstJson.put("a", "1");
    firstJson.put("b", "2");
    firstJson.put("c", new JSONObject().put("d", "5"));
    JSONObjectState first = new TestJSONObjectState(firstJson);
    
    JSONObject secondJson = new JSONObject();
    secondJson.put("b", "2");
    secondJson.put("a", "1");
    secondJson.put("c", new JSONObject().put("d", "5"));
    JSONObjectState second = new TestJSONObjectState(secondJson);
    
    assertTrue(first.diff(second).isEmpty());
    assertTrue(second.diff(first).isEmpty());
  }
  
  @Test
  public void diffMissing() throws JSONException {
    JSONObject firstJson = new JSONObject();
    firstJson.put("a", "1");
    firstJson.put("b", "2");
    JSONObjectState first = new TestJSONObjectState(firstJson);
    
    JSONObject secondJson = new JSONObject();
    secondJson.put("a", "1");
    JSONObjectState second = new TestJSONObjectState(secondJson);
    
    assertFalse(first.diff(second).isEmpty());
    assertTrue(first.diff(second).get(0) instanceof MissingPropertyStateDifference);
    assertFalse(second.diff(first).isEmpty());
    assertTrue(second.diff(first).get(0) instanceof MissingPropertyStateDifference);
  }
  
  @Test
  public void diffValue() throws JSONException {
    JSONObject firstJson = new JSONObject();
    firstJson.put("a", "1");
    JSONObjectState first = new TestJSONObjectState(firstJson);
    
    JSONObject secondJson = new JSONObject();
    secondJson.put("a", "2");
    JSONObjectState second = new TestJSONObjectState(secondJson);
    
    assertFalse(first.diff(second).isEmpty());
    assertTrue(first.diff(second).get(0) instanceof PropertyValueStateDifference);
    assertFalse(second.diff(first).isEmpty());
    assertTrue(second.diff(first).get(0) instanceof PropertyValueStateDifference);
  }
  
  private static class TestJSONObjectState extends JSONObjectState {
    public TestJSONObjectState(JSONObject json) {
      super(json);
    }
    
    @Override
    public StateChecker createStateChecker() {
      return null;
    }
  }
}
