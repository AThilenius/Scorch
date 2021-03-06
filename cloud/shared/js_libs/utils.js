/*
 * Helpers for JSON based REST APIs.
 */

/*
 * Generates a standard error object from a message, error code, and stack trace
 */
exports.error = function(message, errorCode, stackTrace) {
  return {
    did_pass: false,
    failure_message: message,
    error_code: errorCode,
    stack_trace: stackTrace
  };
};

/*
 * Accepts a varadic list of fields that need to be checked for existence and
 * optionally type. Nested fields can be checked using a : delimited. Types can
 * be checked by putting the acceptable type before the name of the field.
 * For example "string message:target_email"
 */
exports.fieldCheck = function(req, res) {
  var didPass = true;
  var missingFields = "The field(s) ";
  for (var i = 2; i < arguments.length; i++) {
    var field = arguments[i];
    var type = field.split(' ');
    var argPath = null;
    // Check if we need to enforce type safety
    if (type.length > 1) {
      argPath = type[1].split(':');
      type = type[0];
    } else {
      argPath = field.split(':');
      type = null;
    }
    console.log("Checking Type: " + type + " with args: " + argPath);
    var cursor = req.body;
    while (argPath.length !== 0) {
      var arg = argPath.shift();
      if (!cursor.hasOwnProperty(arg)) {
        missingFields += "[" + field + "] ";
        didPass = false;
        break;
      }
      // If arg is the last one in the path, check for type safety
      if (type !== null && argPath.length === 0 && typeof cursor[arg] != type) {
        missingFields += "[" + field + "] ";
        didPass = false;
      }
      cursor = cursor[arg];
    }
  }
  if (!didPass) {
    res.json(module.exports.error(missingFields +
      "is required but not provided.", 400));
  }
  return didPass;
};

exports.errorCheck = function(res, err) {
  if (err === null) {
    return false;
  } else {
    res.json(module.exports.error("Internal Server Error: " + err, 500, null));
    return true;
  }
};
