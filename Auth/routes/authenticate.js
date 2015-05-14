var express = require('express');
var router = express.Router();
var utils = require('utils');

// Post: /authenicate/create_ptoken
router.post('/create_ptoken', function(req, res) {
  if (!utils.fieldCheck(req, res, "email_address", "password")){
    return;
  }

  res.json( { hello : "create_ptoken" } );
});

// Post: /authenicate/create_stoken
router.post('/create_stoken', function(req, res) {
  if (!utils.fieldCheck(req, res, "token")){
    return;
  }
  res.json( { hello : "create_stoken" });
});

// Post: /authenicate/validate_token
router.post('/validate_token', function(req, res) {
  if (!utils.fieldCheck(req, res, "token")){
    return;
  }
  res.json( { hello : "validate_tokens" });
});

module.exports = router;
