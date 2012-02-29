function(head, req) 
{
    // Simple JavaScript Templating
// John Resig - http://ejohn.org/ - MIT Licensed
var cache = {};

function template(str, data){
  // Figure out if we're getting a template, or if we need to
  // load the template - and be sure to cache the result.
  var fn = cache[str] ||

  // Generate a reusable function that will serve as a template
  // generator (and which will be cached).
    new Function("obj",
      "var p=[],print=function(){p.push.apply(p,arguments);};" +
          
            // Introduce the data as local variables using with(){}
            "with(obj){p.push('" +
          
            // Convert the template into pure JavaScript
            str
            .replace(/\n/g, "\\n")
            .replace(/[\r\t]/g, " ")
            .replace(/'(?=[^%]*%>)/g,"\t")
            .split("'").join("\\'")
            .split("\t").join("'")
            .replace(/<%=(.+?)%>/g, "',$1,'")
            .split("<%").join("');")
            .split("%>").join("p.push('")
            + "');}return p.join('');");
  cache[str] = fn;
  
  // Provide some basic currying to the user
  return data ? fn( data ) : fn;
};

var templates = {"forums": "<tiles>\n\t<tile name=\"title\">\n\tList of Forums\n\t</tile>\n\t<tile name=\"body\">\n\t\t<h1>List of Forums</h1>\n\t\t<div class=\"forumList\">\n\t\t<%  rows.forEach( function(row) { var doc = row.doc; %>\n\t\t<div class=\"forum\">\n\t\t\t<a href=\"#\"><%= doc.name %></a>&nbsp;<a href=\"<%= assets %>/_show/forum/<%= doc._id %>\">Edit</a><br/>\n\t\t\t<span class=\"desc\"><%= doc.description%></span>\n\t\t</pre>\n\t\t<% }); %>\n\t\t</div>\n\t\t<p>\n\t\t\t<a href=\"<=% assets %>/_show/forum/\">Create new forum</a>\n\t\t</p>\n\t\t\n\t</tile>\n</tiles>\n", "design": {"default": "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n<title><%= title %> :: CouchBBS</title>\n<style type=\"text/css\">\n</style>\n<%= headCode %>\n<script type=\"text/javascript\" src=\"/_utils/script/jquery.js?1.3.1\">\n</script>\n<link rel=\"stylesheet\" href=\"<%= assets %>/style/reset.css\" type=\"text/css\">\n<link rel=\"stylesheet\" href=\"<%= assets %>/style/main.css\" type=\"text/css\">\n</head>\n<body>\n<div id=\"page\">\n\t<div id=\"header\">\n\t\t<h1>Default</h1>\n\t</div>\n\t<div id=\"topnav\">\n\t</div>\n\t<% if ( typeof sidebar !== \"undefined\") { %>\n\t<div id=\"sidebar\">\n\t\t<%= sidebar %>\n\t</div>\n\t<% } %>\n\t<div id=\"content\">\n\t\t<%= body %>\t\t\n\t</div>\n</div>\n</body>\n</html>\n"}};

this.designTemplates = {};

var tileRegex = /<tile\s*name\s*=\s*"([^"]*)"\s*>([\s\S]*?)<\/\s*tile\s*>/gi;

function designNameFromTemplate(templ)
{
    var match = /<tiles\s*design\s*=\s*"([^"]+)">/i.exec(templ);
    if (match)
    {
        return match[1];
    }
    else
    {
        return "default";
    }
}

function designFromTemplate(templ)
{
    var name = designNameFromTemplate(templ);
    if (!designTemplates[name])
    {
        log("create " + name);
        var slots = [];
        var defaultRE = /<%=\s*([a-z0-9_]+)\s*%>/gi;
        var slen = 0;
        var match;
        var designTemplate = templates.design[name];
        while (match = defaultRE.exec(designTemplate))
        {
            slots[slen++] = match[1];
        }
        
        designTemplates[name] = { "fn": template(designTemplate), "slots": slots };
    }
    return designTemplates[name];
}

function renderList(templ, model)
{
    var rows = [];
    while (row = getRow())
    {
        rows.push(row);
    }
    
    model = model || {};
    model.rows = rows;
    render(templ, model);
}

function render(templ, model)
{    
    var design = designFromTemplate(templ);

    var slots = design.slots;
    var slen = slots.length;
    for (var i=0; i < slen; i++)
    {
        model[slots[i]] = "";
    }

    model = model || {};
    model.head = head;
    model.req = req;
    model.toJSON = toJSON;
    model.assets = assetPath();

    while (match = tileRegex.exec(templ))
    {
        var name = match[1];
        var content = match[2];        
        model[name] = template(content, model);        
    }

    //log("assets = " + toJSON(model.assets));

    send(design.fn(model));
}

    // from couch.js
function encodeOptions(options, noJson) {
  var buf = []
  if (typeof(options) == "object" && options !== null) {
    for (var name in options) {
      if (!options.hasOwnProperty(name)) continue;
      var value = options[name];
      if (!noJson && (name == "key" || name == "startkey" || name == "endkey")) {
        value = toJSON(value);
      }
      buf.push(encodeURIComponent(name) + "=" + encodeURIComponent(value));
    }
  }
  if (!buf.length) {
    return "";
  }
  return "?" + buf.join("&");
}

function concatArgs(array, args) {
  for (var i=0; i < args.length; i++) {
    array.push(args[i]);
  };
  return array;
};

function makePath(array) {
  var options, path;
  
  if (typeof array[array.length - 1] != "string") {
    // it's a params hash
    options = array.pop();
  }
  path = array.map(function(item) {return encodeURIComponent(item)}).join('/');
  if (options) {
    return path + encodeOptions(options);
  } else {
    return path;    
  }
};

function assetPath() {
  var p = req.path, parts = ['', p[0], p[1] , p[2]];
  return makePath(concatArgs(parts, arguments));
};

function showPath() {
  var p = req.path, parts = ['', p[0], p[1] , p[2], '_show'];
  return makePath(concatArgs(parts, arguments));
};

function listPath() {
  var p = req.path, parts = ['', p[0], p[1] , p[2], '_list'];
  return makePath(concatArgs(parts, arguments));
};

function olderPath(info) {
  if (!info) return null;
  var q = req.query;
  q.startkey = info.prev_key;
  q.skip=1;
  return listPath('index','recent-posts',q);
}

function makeAbsolute(req, path) {
  return 'http://' + req.headers.Host + path;
}


function currentPath() {
  path = req.path.map(function(item) {return encodeURIComponent(item)}).join('/');
  if (req.query) {
    return path + encodeOptions(req.query, true);
  } else {
    return path;
  }
}
    var templates = {"forums": "<tiles>\n\t<tile name=\"title\">\n\tList of Forums\n\t</tile>\n\t<tile name=\"body\">\n\t\t<h1>List of Forums</h1>\n\t\t<div class=\"forumList\">\n\t\t<%  rows.forEach( function(row) { var doc = row.doc; %>\n\t\t<div class=\"forum\">\n\t\t\t<a href=\"#\"><%= doc.name %></a>&nbsp;<a href=\"<%= assets %>/_show/forum/<%= doc._id %>\">Edit</a><br/>\n\t\t\t<span class=\"desc\"><%= doc.description%></span>\n\t\t</pre>\n\t\t<% }); %>\n\t\t</div>\n\t\t<p>\n\t\t\t<a href=\"<=% assets %>/_show/forum/\">Create new forum</a>\n\t\t</p>\n\t\t\n\t</tile>\n</tiles>\n", "design": {"default": "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n<title><%= title %> :: CouchBBS</title>\n<style type=\"text/css\">\n</style>\n<%= headCode %>\n<script type=\"text/javascript\" src=\"/_utils/script/jquery.js?1.3.1\">\n</script>\n<link rel=\"stylesheet\" href=\"<%= assets %>/style/reset.css\" type=\"text/css\">\n<link rel=\"stylesheet\" href=\"<%= assets %>/style/main.css\" type=\"text/css\">\n</head>\n<body>\n<div id=\"page\">\n\t<div id=\"header\">\n\t\t<h1>Default</h1>\n\t</div>\n\t<div id=\"topnav\">\n\t</div>\n\t<% if ( typeof sidebar !== \"undefined\") { %>\n\t<div id=\"sidebar\">\n\t\t<%= sidebar %>\n\t</div>\n\t<% } %>\n\t<div id=\"content\">\n\t\t<%= body %>\t\t\n\t</div>\n</div>\n</body>\n</html>\n"}};
    provides("html", function()
    {
        renderList(templates.forums);
    });
}
