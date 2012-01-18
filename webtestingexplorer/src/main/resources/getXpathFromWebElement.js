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
return getXpathExpression(arguments[0]);