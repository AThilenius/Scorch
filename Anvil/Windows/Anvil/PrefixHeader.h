//
//  PrefixHeader.pch
//  Anvil
//
//  Created by Alec Thilenius on 11/14/14.
//  Copyright (c) 2014 Alec Thilenius. All rights reserved.
//
#pragma once

// Defines
#define PROTOBUF_USE_DLLS

// STD
#include <algorithm>
#include <assert.h>
#include <cstring>
#include <exception>
#include <fstream>
#include <functional>
#include <iomanip>
#include <iomanip>
#include <iostream>
#include <map>
#include <math.h>
#include <memory>
#include <regex>
#include <set>
#include <sstream>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string>
//#include <unistd.h>
#include <unordered_map>
#include <unordered_set>
#include <vector>

// Anvil
#include "ProjectConfig.h"

// Macros
#define SAFE_DELETE(x) if(x){delete x;x=nullptr;}
#define SAFE_FREE(x) if(x){free(x);x=nullptr;}

// Typedefs
typedef uint8_t UInt8;
typedef int8_t Int8;
typedef int16_t Int16;
typedef uint16_t UInt16;
typedef int32_t Int32;
typedef uint32_t UInt32;
typedef int64_t Int64;
typedef uint64_t UInt64;