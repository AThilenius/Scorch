//
//  SparkServiceImpl.h
//  Blaze
//
//  Created by Alec Thilenius on 10/9/2014.
//  Copyright (c) 2013 Thilenius. All rights reserved.
//
#pragma  once

#include <RCF\RCF.hpp>
#include "Blaze.h"

namespace Blaze {
namespace Services {

class FlameClient;

class SparkServiceImpl {
public:
	SparkServiceImpl(FlameClient* flameClient);
	~SparkServiceImpl();

	bool DispatchMovement( const Spark& spark, const MovementTypes& movementType );
	void DispatchOrientation( const Spark& spark, const OrientationTypes& orientationType );

private:
	RCF::RcfServer* m_server;
	FlameClient* m_flameClient;
};

} // namespace Services
} // namespace Blaze