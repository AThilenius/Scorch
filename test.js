var proxy = require('proxy');
var auth = require('auth');
console.log(proxy.getProxyHostname());

var authService = new auth.Service();
authService.createUser("").on('complete', function(data) {
  console.log(JSON.stringify(data, null, 2));
});
