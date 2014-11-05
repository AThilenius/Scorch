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
namespace cpp Flame
namespace csharp Flame
namespace java com.thilenius.flame.service.thrift.genfiles


/******************************************************************************/
/* Structs                                                                    */
/******************************************************************************/
struct Spark {
  1:i32 sparkID
}

struct Location {
	1:i32 x,
	2:i32 y,
	3:i32 z
}


/******************************************************************************/
/* Enums                                                                      */
/******************************************************************************/
enum MovementTypes {
  Forward   = 1,
  Backward  = 2,
  Up        = 3,
  Down      = 4
}

enum OrientationTypes {
  TurnLeft  = 1,
  TurnRight = 2
}


/******************************************************************************/
/* Services                                                                   */
/******************************************************************************/
service FlameService {

	/**************************************************************************/
	/* Spark                                                                  */
	/**************************************************************************/
   	//===   Add / Remove   =====================================================
   	Spark 			CreateSpark 			(1:Location worldLocation),
   	list<Spark> 	GetAllSparks 			( ),
   	void			RemoveSpark				(1:Spark sparkToRemove),
   	void			RemoveAllSparks 		( ),


   	//===   Movement   =========================================================
   	bool 			DispatchMovementCommand	(1:Spark spark, 
   											 2:MovementTypes movementCommand)
    void      DispatchOrientationCommand (1:Spark spark,
                         2:OrientationTypes orientationCommand)
}