//
//  AnvilAPI.h
//  AnvilDyn
//
//  Created by Alec Thilenius on 2/10/15.
//  Copyright (c) 2015 Scorch. All rights reserved.
//
#pragma once

#ifdef _WIN32
	#define DLL_EXPORT_API extern "C" __declspec(dllexport)
#else
	#define DLL_EXPORT_API 
#endif

extern "C" {
    
    
DLL_EXPORT_API void DynAnvilSayHello();
DLL_EXPORT_API int DynAnvilLoadLevel(int levelNumber);
DLL_EXPORT_API bool DynSparkMoveForward(int level, int spark);
DLL_EXPORT_API bool DynSparkMoveBackward(int level, int spark);
DLL_EXPORT_API void DynSparkTurnLeft(int level, int spark);
DLL_EXPORT_API void DynSparkTurnRight(int level, int spark);
    
    
}