//
//  Log.h
//  Anvil
//
//  Created by Alec Thilenius on 1/1/15.
//  Copyright (c) 2014 Alec Thilenius. All rights reserved.
//
#pragma once


namespace Util {


enum ConsoleColor { White, Red, Yellow, Green, Blue };


class Log
{
public:
	~Log(void);

    static void Info(std::string message);
	static void Error(std::string message);
	static void SetColor(ConsoleColor color);
    static void Suppress();

private:
	Log(void);
    
private:
    static bool s_isSupressed;

};


} // namespace Util