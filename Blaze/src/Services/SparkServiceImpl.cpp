//
//  SparkServiceImpl.cpp
//  Blaze
//
//  Created by Alec Thilenius on 10/9/2014.
//  Copyright (c) 2013 Thilenius. All rights reserved.
//
#include "PCH.h"
#include "FlameClient.h"
#include "FlameService.h"
#include "SparkServiceImpl.h"

namespace Blaze {
namespace Services {

SparkServiceImpl::SparkServiceImpl( FlameClient* flameClient ):
	m_flameClient(flameClient) {

}

SparkServiceImpl::~SparkServiceImpl() {

}

bool SparkServiceImpl::DispatchMovement(
	const Spark& spark,
	const MovementTypes& movementType) {
	Flame::Spark fSpark;
	fSpark.sparkID = spark.SparkID;
	return m_flameClient->GetServiceClient()->DispatchMovementCommand(fSpark,
		(Flame::MovementTypes::type)(int)movementType);
}

void SparkServiceImpl::DispatchOrientation(
	const Spark& spark,
	const OrientationTypes& orientationType) {
	Flame::Spark fSpark;
	fSpark.sparkID = spark.SparkID;
	m_flameClient->GetServiceClient()->DispatchOrientationCommand(fSpark,
		(Flame::OrientationTypes::type)(int)orientationType);
}

} // namespace Services
} // namespace Blaze