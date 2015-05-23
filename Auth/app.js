var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var flags = require('flags');
var utils = require('utils');

// Database
var mongo = require('mongoskin');
var db = mongo.db("mongodb://localhost:27017/auth", {native_parser:true});

var user = require('./routes/user');
var authenticate = require('./routes/authenticate');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

// uncomment after placing your favicon in /public
//app.use(favicon(__dirname + '/public/favicon.ico'));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

// Make our db accessible to our router
app.use(function(req,res,next){
    req.db = db;
    next();
});

app.use('/user', user);
app.use('/authenticate', authenticate);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  //var err = new Error('Not Found');
  //err.status = 404;
  //next(err);
  res.json({
    did_pass : false,
    failure_message : "Route Not Found",
    error_code : 404
  });
});

// error handlers

// Developemnt error handler that includes the stack trace and prints to console
if (app.get('env') === 'development') {
  app.use(function(err, req, res, next) {
    // Render JSON response
    res.json(utils.error(err.message, err.status, err.stack));
    console.log("Error: " + err.status);
    console.log("Message: " + err.message);
    console.log("Stack Trace:");
    console.log(err.stack);
    console.log();
  });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
    // Render JSON response without stack trace
    res.json({
      did_pass : false,
      failure_message : err.message,
      error_code : err.status
    });
});


module.exports = app;
