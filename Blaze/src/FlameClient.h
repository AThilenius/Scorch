//
//  FlameClient.h
//  Blaze
//
//  Created by Alec Thilenius on 10/9/2014.
//  Copyright (c) 2013 Thilenius. All rights reserved.
//
#pragma  once

namespace Flame {
class FlameServiceClient;
} // namespace Flame

namespace Blaze {
namespace Services {

class FlameClient {
public:
	FlameClient();
	~FlameClient();

	void Connect();
	Flame::FlameServiceClient* GetServiceClient();

private:
	Flame::FlameServiceClient* m_client;
};

} // namespace Services
} // namespace Blaze