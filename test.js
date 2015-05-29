var proxy = require('proxy');
var auth = require('auth');
console.log("Using hostname: " + proxy.getProxyHostname());

var authService = new auth.RestService();
authService.createUser("ServiceOne", "12345678").on('complete', function(data) {
  console.log(JSON.stringify(data, null, 2));
});
