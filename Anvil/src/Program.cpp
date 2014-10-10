//
//  PCH.h
//  Anvil
//
//  Created by Alec Thilenius on 10/9/2014.
//  Copyright (c) 2013 Thilenius. All rights reserved.
//
#include "PCH.h"
#include <iostream>

#include "Blaze.h"

DEFINE_string(blaze_ip, "127.0.0.1",
			  "The IP address for the Blaze server.");
DEFINE_uint64(blaze_port, 9091,
			  "The port number for the Blaze server.");

int main(int argc, char* argv[]) 
{
	// Init Google Flags and Google Logging
	std::string usage = std::string(argv[0]) + " <args>";
	gflags::SetUsageMessage(usage);
	gflags::ParseCommandLineFlags(&argc, &argv, false);
	google::InitGoogleLogging(argv[0]);
	FLAGS_logtostderr = 1;
	FLAGS_colorlogtostderr = 1;

	RCF::RcfInitDeinit rcfInit;

	LOG(INFO) << "Connecting to Blaze server at IP: " << FLAGS_blaze_ip
		<< ". Port: " << FLAGS_blaze_port;

	Blaze::Services::RcfClient<Blaze::Services::IServiceSpark> blazeClient (
		RCF::TcpEndpoint(FLAGS_blaze_ip, FLAGS_blaze_port));

	LOG(INFO) << "Connected to Blaze server at IP: " << FLAGS_blaze_ip
		<< ". Port: " << FLAGS_blaze_port;
	blazeClient.DispatchOrientation(Blaze::Services::Spark(6),
		Blaze::Services::OrientationTypes::TurnLeft);
	blazeClient.DispatchOrientation(Blaze::Services::Spark(6),
		Blaze::Services::OrientationTypes::TurnRight);
	blazeClient.DispatchMovement(Blaze::Services::Spark(6),
		Blaze::Services::MovementTypes::Forward);
	blazeClient.DispatchMovement(Blaze::Services::Spark(6),
		Blaze::Services::MovementTypes::Backward);
	blazeClient.DispatchMovement(Blaze::Services::Spark(6),
		Blaze::Services::MovementTypes::Up);
	blazeClient.DispatchMovement(Blaze::Services::Spark(6),
		Blaze::Services::MovementTypes::Down);

	std::cin.ignore();
	return 0;
}