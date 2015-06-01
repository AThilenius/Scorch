/*
 * API interface for Auth web server
 */
var rest = require('restler');
var proxy = require('proxy');

//exports.Service = rest.service(function() {
//this.defaults.internal_request = true;
//}, {
//baseURL: proxy.getProxyHostname()
//}, {
//createUser: function(emailAddress, password) {
//return this.post('/auth/user/create', {
//data: {
//email_address: emailAddress,
//password: password
//}
//});
//}
//});


// Dev Loopback for now
exports.RestService = rest.service(function() {
  this.defaults.internal_request = true;
}, {
  baseURL: proxy.getProxyHostname()
}, {
  createUser: function(emailAddress, password) {
    return this.post('/user/create', {
      data: {
        email_address: emailAddress,
        password: password
      }
    });
  },

  readUser: function(token) {
    return this.post('/user/read', {
      data: {
        token: token
      }
    });
  },

  deleteUser: function(ptoken) {
    return this.post('/user/delete', {
      data: {
        token: ptoken
      }
    });
  },

  createPToken: function(emailAddress, password) {
    return this.post('/user/create_ptoken', {
      data: {
        email_address: emailAddress,
        password: password
      }
    });
  },

  createSToken: function(token) {
    return this.post('/user/create_stoken', {
      data: {
        token: token
      }
    });
  }
});
