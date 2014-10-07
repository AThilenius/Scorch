#include <iostream>

#include <thrift/protocol/TBinaryProtocol.h>
#include <thrift/transport/TSocket.h>
#include <thrift/transport/TTransportUtils.h>

#include <RCF/RCF.hpp>

#include "FlameService.h"

using namespace Flame;
using namespace apache::thrift;
using namespace apache::thrift::protocol;
using namespace apache::thrift::transport;

RCF_BEGIN(I_HelloWorld, "I_HelloWorld")
	RCF_METHOD_V1(void, Print, const std::string &)
RCF_END(I_HelloWorld)

int main(int argc, char* argv[]) 
{
	boost::shared_ptr<TTransport> socket(new TSocket("localhost", 9090));
	boost::shared_ptr<TTransport> transport(new TBufferedTransport(socket));
	boost::shared_ptr<TProtocol> protocol(new TBinaryProtocol(transport));
	FlameServiceClient client(protocol);

	try {
		transport->open();

		Spark spark;
		spark.sparkID = 6;

		client.DispatchMovementCommand(spark, MovementTypes::Forward);
		client.DispatchMovementCommand(spark, MovementTypes::Backward);
		client.DispatchMovementCommand(spark, MovementTypes::Up);
		client.DispatchMovementCommand(spark, MovementTypes::Down);
		client.DispatchMovementCommand(spark, MovementTypes::TurnLeft);
		client.DispatchMovementCommand(spark, MovementTypes::TurnRight);

		std::vector<Spark> allSparks;
		client.GetAllSparks(allSparks);

		for (int i = 0; i < allSparks.size(); i++) {
			std::cout << "Spark: " << allSparks[i].sparkID << std::endl;
		}

	} catch (TException& tx) {
		std::cout << "ERROR: " << tx.what() << std::endl;
	}

	std::cin.ignore();
	return 0;
}