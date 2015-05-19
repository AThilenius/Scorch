var express = require('express');
var router = express.Router();
var utils = require('utils');
var crypto = require('crypto');

// Post: /authenticate/create_ptoken
router.post('/create_ptoken', function(req, res) {
  // Require email_address, password
  if (!utils.fieldCheck(req, res, "email_address", "password")){ return; }
  // Normalize fields
  var emailAddress = req.body.email_address.replace(/ /g, '').toLowerCase();
  // Generate a crypto safe 48 byte token
  crypto.randomBytes(48, function(ex, buf) {
    var newToken = buf.toString('hex');
    var usersDb = req.db.collection('users');
    usersDb.update(
        { email_address : emailAddress, password : req.body.password },
        { $set : { primary_token : newToken } },
        function(err, count, stat) {
          if (utils.errorCheck(res, err)) { return; }
          if (count === 0) {
            res.json(utils.error("Invalid username or password.", 403, null));
          } else {
            res.json({ did_pass : true, message : { token : newToken } });
          }
        });
  });
});

// Post: /authenticate/create_stoken
router.post('/create_stoken', function(req, res) {
  // Require primary token
  if (!utils.fieldCheck(req, res, "token")){ return; }
  crypto.randomBytes(48, function(ex, buf) {
    var newToken = buf.toString('hex');
    var usersDb = req.db.collection('users');
    usersDb.update(
        { primary_token : req.body.token },
        { $addToSet : { secondary_tokens : newToken } },
        function(err, count, stat) {
          if (utils.errorCheck(res, err)) { return; }
          if (count === 0) {
            res.json(utils.error("Invalid TPrimary token", 403, null));
          } else {
            res.json({ did_pass : true, message : { token : newToken } });
          }
        });
  });
});

module.exports = router;
