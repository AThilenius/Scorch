var express = require('express');
var router = express.Router();

// Post: /authenicate/create_ptoken
router.post('/create_ptoken', function(req, res) {
  console.log("create_ptoken");
  res.json( { hello : "create_ptoken" } );
});

// Post: /authenicate/create_stoken
router.post('/create_stoken', function(req, res) {
  console.log("create_stoken");
  res.json( { hello : "create_stoken" });
});

// Post: /authenicate/validate_token
router.post('/validate_token', function(req, res) {
  console.log("validate_token");
  res.json( { hello : "validate_tokens" });
});

module.exports = router;
