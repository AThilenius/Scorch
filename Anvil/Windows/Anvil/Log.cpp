//
//  Log.cpp
//  Anvil
//
//  Created by Alec Thilenius on 1/1/15.
//  Copyright (c) 2014 Alec Thilenius. All rights reserved.
//
#include "PrefixHeader.h"
#include "Log.h"


#if defined(WIN32) || defined(_WIN32) || defined(__WIN32)
#include <Windows.h>
#include <iostream>
#endif


namespace Util {


Log::Log(void)
{

}


Log::~Log(void)
{

}
    
void Log::Info(std::string message) {
    SetColor(Green);
    std::cout << "[INFO] ";
    SetColor(White);
    std::cout << message << std::endl;
}

void Log::Error( std::string message )
{
#ifdef _WIN32
	// For visual studio, write out the error and wait for a key. The console
	// will auto close if not.
	SetColor(Red);
	std::cout << "[ERROR] " << message <<std::endl;
	SetColor(White);
	std::cin.ignore();
#else
	// On OSX and Linux print and exit
	SetColor(Red);
	std::cout << "[ERROR] " << message <<std::endl;
	SetColor(White);
	exit(EXIT_FAILURE);
#endif
}

void Log::SetColor( ConsoleColor color )
{
#if defined(WIN32) || defined(_WIN32) || defined(__WIN32)
	//Windows

	switch ( color )
	{
	case White:
		//HANDLE hStdout = GetStdHandle(STD_OUTPUT_HANDLE); 
		SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE), 
			FOREGROUND_RED|FOREGROUND_GREEN|FOREGROUND_BLUE);
		break;

	case Red:
		//HANDLE hStdout = GetStdHandle(STD_OUTPUT_HANDLE); 
		SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE), 
			FOREGROUND_RED|FOREGROUND_INTENSITY);
		break;

	case Yellow:
		//HANDLE hStdout = GetStdHandle(STD_OUTPUT_HANDLE); 
		SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE), 
			FOREGROUND_GREEN|FOREGROUND_RED|FOREGROUND_INTENSITY);
		break;

	case Green:
		//HANDLE hStdout = GetStdHandle(STD_OUTPUT_HANDLE); 
		SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE), 
			FOREGROUND_GREEN|FOREGROUND_INTENSITY);
		break;

	case Blue:
		//HANDLE hStdout = GetStdHandle(STD_OUTPUT_HANDLE); 
		SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE), FOREGROUND_BLUE
			|FOREGROUND_GREEN|FOREGROUND_INTENSITY);
		break;
	}

#else
	// Linux

	switch ( color )
	{
	case White:
		std::cout << "\033[0m";
		break;

	case Red:
		std::cout << "\033[31m";
		break;

	case Yellow:
		std::cout << "\033[33m";
		break;

	case Green:
		std::cout << "\033[32m";
		break;

	case Blue:
		std::cout << "\033[36m";
		break;
	}
#endif
}


} // namespace Util