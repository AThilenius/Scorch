/*
 * Simple URL helpers for proxy based service interaction
 */
var flags = require('flags');

// Flags
flags.defineString('proxy_hostname', 'localhost',
  'The hostname of the primary proxy for API calls.');
flags.defineString('proxy_protocol', 'http',
  'The protocol to use when talking with proxy. [http, https]');
flags.defineInteger('proxy_port', 3000,
  'The port of the primary proxy for API calls.');
flags.parse();

// Cache full host name so it doesn't need to be rebuild every time
var fullHostname = null;

/*
 * Returns the full host name, including protocol and optional port
 */
exports.getProxyHostname = function() {
  if (fullHostname === null) {
    // Trim trailing slashes and whitespace on host name
    var hostname = flags.get('proxy_hostname').replace(/^\s*\/|\/+\s*$/, "");
    fullHostname = flags.get('proxy_protocol') + '://' + hostname + (flags.get(
      'proxy_port') === 80 ? '' : ':' + flags.get('proxy_port'));
  }
  return fullHostname;
};

/*
 * Returns the full resolved path to a proxyed resource
 */
exports.getPath = function(route) {
  route = route.replace(/^\s*\/|\/+\s*$/, "");
  return module.exports.getProxyHostname() + '/' + route;
};
