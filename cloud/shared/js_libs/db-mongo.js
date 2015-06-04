/*
 * Mongodb access through etcd
 */
var flags = require('flags');
var mongo = require('mongoskin');

flags.defineString('mongo_ip', 'localhost',
  'The ip address or hostname of the MongoDB instance');
flags.defineInteger('mongo_port', 27017, 'The port of the MongoDB instance');
flags.parse();

exports.getDbByName = function(dbName) {
  return mongo.db("mongodb://" + process.env.MONGO_IP + ":" + process.env.MONGO_PORT +
    "/" + dbName, {
      native_parser: true
    });
};
