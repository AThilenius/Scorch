/**
 *  bool        Boolean, one byte
 *  byte        Signed byte
 *  i16         Signed 16-bit integer
 *  i32         Signed 32-bit integer
 *  i64         Signed 64-bit integer
 *  double      64-bit floating point value
 *  string      String
 *  binary      Blob (byte array)
 *  map<t1,t2>  Map from one type to another
 *  list<t1>    Ordered list of one type
 *  set<t1>     Set of unique elements of one type
 *
 */

namespace csharp ComputerCraftRemote

const string DEFAULT_POOL_NAME = "Unassigned"
const string DEFAULT_OWNER_NAME = "None"

struct Work {
  1:i32 computerID,
  2:string command
}

struct TurtleIdPool {
	1:i32 turtleId,
	2:string poolName,
  3:string startLocation
}

struct PoolOwnner {
	1:string poolName,
	2:string ownerName
}

exception LuaException {
  1: string message
}

service ComputerRemote {
	string invokeCommandSync(1:i32 computerId, 2:string command) throws (1:LuaException ex),
	oneway void invokeCommandAsync(1:i32 computerId, 2:string command),

  bool requestPoolOwnership(1:string poolName, 2:string ownerName),
  void freePool(1:string poolName),
  list<TurtleIdPool> getAllTurtles(),
  list<PoolOwnner> getAllPools()
}