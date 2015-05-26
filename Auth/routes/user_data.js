var express = require('express');
var router = express.Router();
var utils = require('utils');

// Post: user/create
router.post('/create', function(req, res) {
  if (!utils.fieldCheck(req, res, "string email_address", "string password",
        "string first_name", "string last_name")) {
    return;
  }

  // Pre-process some fields
  var emailAddress = req.body.email_address.replace(/ /g, '').toLowerCase();
  var password = req.body.password;
  var firstName = req.body.first_name.replace(/ /g, '');
  var lastName = req.body.last_name.replace(/ /g, '');

  if (password.length < 8) {
    res.json(utils.error("Password must be at least 8 characters in length.",
          400, null));
    return;
  }

  var db = req.db.collection('users');

  db.find({ email_address : emailAddress }).toArray(function (err, items) {
    if (utils.errorCheck(res, err)) { return; }
    if (items.length !== 0) {
      // User already exists.
      res.json(utils.error("A user with the email address already exists." +
            req.body.email_address + " already exists.", 403, null));
      return;
    } else { 
      // User doesn't already exist, create it
      var user = {
        email_address : emailAddress,
        password : password,
        first_name : firstName,
        last_name : lastName 
      };
      db.insert(user, function(err, records){
        if (utils.errorCheck(res, err)) { return; }
        res.json({ did_pass : true });
        console.log("New account created: " +
            JSON.stringify(user, null, 2));
        return;
      });
    }
  });
});

// Post: user/read
router.post('/read', function(req, res) {
  if (!utils.fieldCheck(req, res, "string token")) { return; }
  var usersDb = req.db.collection("users");
  usersDb.findOne(
      {
        $or: [
        { primary_token : req.body.token },
        { secondary_tokens : req.body.token }, ]
      }, function (err, user) {
        if (utils.errorCheck(res, err)) { return; }
        if (user) {
          var permissionsLevel = (user.primary_token !== req.body.token) ?
            PLevels.PSecondary : user.permissions;
          res.json({
            email_address : user.email_address,
            permissions : permissionsLevel 
          });
        } else {
          res.json(utils.error("Invalid token.", 403, null));
        }
      });
});

// Post: /user/create_ptoken
router.post('/create_ptoken', function(req, res) {
  // Require email_address, password
  if (!utils.fieldCheck(req, res, "string email_address",
        "string password")){ return; }
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

// Post: /user/create_stoken
router.post('/create_stoken', function(req, res) {
  // Require primary token
  if (!utils.fieldCheck(req, res, "string token")){ return; }
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
