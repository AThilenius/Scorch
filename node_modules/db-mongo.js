/*
 * Mongodb access through etcd
 */
var mongo = require('mongoskin');

// Again, local debug for now. This will (in the end) need to pull the mongodb
// address from etcd
exports.getDbByName = function(dbName) {
  return mongo.db("mongodb://localhost:27017/" + dbName, {
    native_parser: true
  });
};
