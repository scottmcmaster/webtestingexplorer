function getAllElementsPropertiesWrapper() {
	function getXpathExpression(node) {
	  var node_parent = node.parentNode;
	  if (node.tagName && node.tagName.toLowerCase() == '//html') {
	    return '//' + node.tagName + '[1]';
	  }
	  var path = '';
	  if (node_parent && node_parent != node) {
	    path += getXpathExpression(node_parent);
	    path += '/';
	  }
	  if (node.tagName) {
	    path += node.tagName;
	    var idx = 0;
	    var siblings = node_parent.childNodes;
	    for (var i = 0; i < siblings.length; i++) {
	      var n = siblings[i];
	      if (n.tagName == node.tagName) {
	        idx += 1;
	      }
	      if (n == node) {
	        path += '[';
	        path += idx;
	        path += ']';
	      }
	    }
	  }
	  return path;
	}
	
	function isEmpty(ob){
      for(var i in ob)
        { return false;}
      return true;
    }
    
    function escapeDoubleQuote(s) {
      if (typeof s == "string") {
        return s.replace(/"/g, "");
      }
      return s;
    }
    
	function getElementProperties(n, properties) {
	  var xmlString = '';
	    
	  var xpath = getXpathExpression(n);
	  if (xpath.length > 1) {
		  xmlString += "<element>";
		  xmlString += '<xpath>' + xpath + '</xpath>';
		  
		  xmlString += '<attribute tagName="' + escapeDoubleQuote(n.tagName) + '" />';
		  if (n.nodeValue != null && !/^\s*$/.test(n.nodeValue)) {
		    xmlString += '<attribute nodeValue="' + escapeDoubleQuote(n.nodeValue) + '" />';
		  }
		  var attributes = n.attributes;
		  if (attributes != null) {
		    for (var i = 0; i < attributes.length; i++) {
		      var attribute = attributes[i];
		      var name = attribute.name;
		      if (typeof properties != "undefined" && !isEmpty(properties) && properties.name == "undefined") {
		        continue;
		      }
		      var value = attribute.value;
		      xmlString += '<attribute ' + name + '="' + escapeDoubleQuote(value) + '" />';
		    }  
		  }
		  xmlString += '</element>';
	  }
	  
	  var children = n.childNodes;
	  if (children != null) {
	        for (var i = 0; i < children.length; i++) {
	          xmlString += getElementProperties(children[i], properties);
	        }
	  }
	  return xmlString;
	};
	
	var xmlHeader = '<?xml version="1.0" encoding="UTF-8"?>';
	return '<elements>' + getElementProperties(document, propertiesMap) + '</elements>';
}

return getAllElementsPropertiesWrapper();                                                                                                                                                                                                                                     