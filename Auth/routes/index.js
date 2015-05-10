var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  var db = req.db;
  db.collection('users').find().toArray(function (err, items) {
    console.log(items);
  });
  res.render('index', { title: 'Express' });
});

module.exports = router;
