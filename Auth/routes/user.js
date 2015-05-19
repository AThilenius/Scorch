var express = require('express');
var router = express.Router();
var utils = require('utils');

// Post: user/create
router.post('/create', function(req, res) {
  if (!utils.fieldCheck(req, res, "email_address", "password", "first_name",
        "last_name")) {
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
  var sendFullData = function(userEntry) {
    res.json({
      email_address : userEntry.email_address,
      first_name : userEntry.first_name,
      last_name : userEntry.last_name,
      permissions : userEntry.permissions
    });
  };
  var sendPartialData = function(userEntry) {
    res.json({
      first_name : userEntry.first_name,
      last_name : userEntry.last_name
    });
  };
  utils.authenticateStdAuth(req, res, function (pLevel, message, callingUser) {
    // Check for self query for PUser and up
    if (pLevel >= utils.PLevels.PUser && callingUser &&
        callingUser.email_address === message.target_email_address) {
      sendFullData(callingUser);
      return;
    }
    // Asked to query different user
    var usersDb = req.db.collection('users');
    usersDb.find({ email_address : message.target_email_address })
      .toArray(function (err, u_items) {
        if (utils.errorCheck(res, err)) { return; }
        if (u_items.length === 0) {
          res.json(utils.error("Failed to find target email address.", 404,
                null));
          return;
        }
        if (pLevel <= utils.PLevels.PUser) {
          sendPartialData(u_items[0]);
        }
        // If the calling user supersedes the query user, allow full data
        if (pLevel > u_items[0].permissions) {
          sendFullData(u_items[0]);
        } else {
          sendPartialData(u_items[0]);
        }
      });
  });
});

// Post: user/update
router.post('/update', function(req, res) {
  if (!utils.fieldCheck(req, res, "token", "target_email_address")) { return; }
  res.json( { hello : "update" } );
});

// Post: user/delete
router.post('/delete', function(req, res) {
  if (!utils.fieldCheck(req, res, "token", "target_email_address")) { return; }
  res.json( { hello : "delete" } );
});

module.exports = router;
