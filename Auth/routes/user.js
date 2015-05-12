var express = require('express');
var router = express.Router();

// Post: user/create
router.post('/create', function(req, res) {
  //var db = req.db;
  //db.collection('user').find().toArray(function (err, items) {
    //console.log(items);
    //res.json(items)
  //});
  res.json( { hello : "create" } );
});

// Post: user/read
router.post('/read', function(req, res) {
  res.json( { hello : "read" } );
});

// Post: user/update
router.post('/update', function(req, res) {
  res.json( { hello : "update" } );
});

// Post: user/delete
router.post('/delete', function(req, res) {
  res.json( { hello : "delete" } );
});

module.exports = router;
