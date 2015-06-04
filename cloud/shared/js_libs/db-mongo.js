/*
 * Mongodb access through etcd
 */
var mongo = require('mongoskin');

console.log("NodeJS MongoDB using ip: [" + process.env.MONGO_IP + "], port: [" +
  process.env.MONGO_PORT + "]");

exports.getDbByName = function(dbName) {
  return mongo.db("mongodb://" + process.env.MONGO_IP + ":" +
    process.env.MONGO_PORT + "/" + dbName, {
      native_parser: true
    });
};
