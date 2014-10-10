//
//  Blaze.h
//  Blaze
//
//  Created by Alec Thilenius on 10/3/2014.
//  Copyright (c) 2013 Thilenius. All rights reserved.
//
#pragma  once

#include <cstdint>
#include <RCF\RCF.hpp>

namespace Blaze {
namespace Services {

//====  Structs  ==================================================================================
class Spark
{
public:
	Spark():
		SparkID(-1)
	{
	}

	Spark(int32_t sparkId):
		SparkID(sparkId)
	{
	}

public:
	int32_t SparkID;

	// Serialization.
	void serialize(SF::Archive &archive)
	{
		archive & SparkID;
	}
};


//====  Enums  ====================================================================================
enum MovementTypes
{
	Forward		= 1,
	Backward	= 2,
	Up			= 3,
	Down		= 4
};

enum OrientationTypes
{
	TurnLeft	= 1,
	TurnRight	= 2
};

//====  Services  =================================================================================
RCF_BEGIN(IServiceSpark, "IServiceSpark")

	RCF_METHOD_R2(bool, DispatchMovement, const Spark&, const MovementTypes&)
	RCF_METHOD_V2(void, DispatchOrientation, const Spark&, const OrientationTypes&)

RCF_END(IServiceSpark)

} // namespace Services
} // namespace Blaze