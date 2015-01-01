//
//  TcpSocket.cpp
//  Anvil
//
//  Created by Alec Thilenius on 11/14/14.
//  Copyright (c) 2014 Alec Thilenius. All rights reserved.
//
#include "PrefixHeader.h"
#include "TcpSocket.h"
#include "TcpMessage.h"

#ifdef _WIN32
	#define WIN32_LEAN_AND_MEAN
	#include <winsock2.h>
	#include <ws2tcpip.h>
	#include <stdio.h>
	#include <stdlib.h>
	#pragma comment(lib,"Ws2_32.lib")
#else
	#include <arpa/inet.h>
	#include <netdb.h>
	#include <netinet/in.h>
	#include <sys/socket.h>
#endif

namespace Socket {

	TcpSocket::TcpSocket() {
	}

	TcpSocket::~TcpSocket() {

	}

	bool TcpSocket::Connect(std::string ipAddressStr, UInt16 port) {
		sf::Socket::Status status = m_socket.connect(ipAddressStr, port);
		if (status != sf::Socket::Done) {
			return false;
		}

		return true;
	}

	TcpMessagePtr TcpSocket::Read() {

		// First read in the 4 byte size (in network endian)
		UInt32 size = 0;

		if (!ReadCompleate((char*)&size, 4)) {
			return TcpMessagePtr(nullptr);
		}

		// Convert to host endian
		size = ntohl(size);

		// Read in that size
		void* buffer = malloc(size);

		if (!ReadCompleate((char*)buffer, size)) {
			return TcpMessagePtr(nullptr);
		}

		// Wrap it in a shared ptr
		TcpMessage* message = new TcpMessage(buffer, size, this);
		return TcpMessagePtr(message);
	}

	bool TcpSocket::Write(void* data, int count) {

		// First write out the size (in network endian)
		UInt32 size = htonl(count);
		if (!WriteComplete((char*)&size, 4)) {
			return false;
		}

		// Next write out the message
		if (!WriteComplete((char*)data, count)) {
			return false;
		}

		return true;
	}

	bool TcpSocket::ReadCompleate(char* buffer, UInt32 count) {
		assert(count != 0);

		size_t readCount = 0;
		while (readCount < count) {
			size_t n = 0;
			if (m_socket.receive(&buffer[readCount], count - readCount, n) != sf::Socket::Done) {
				return false;
			}

			readCount += n;
		}
		return true;
	}

	bool TcpSocket::WriteComplete(char* buffer, UInt32 count) {
		// SFML takes care of most of this for me...
		assert(count != 0);

		if (m_socket.send(buffer, count) != sf::Socket::Done) {
			return false;
		}
		return true;
	}


} // namespace Socket