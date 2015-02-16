//
//  File.h
//  Anvil
//
//  Created by Alec Thilenius on 2/7/15.
//  Copyright (c) 2015 Alec Thilenius. All rights reserved.
//
#pragma once


namespace Util {
    
    
class TextFile {
public:
    static std::vector<TextFile> GlobDirectory (std::string dir);
    static std::string ReadFileToString (std::string path);
    
public:
    std::string Name;
    std::string Extension;
    std::string Contents;
    std::string ModifyDate;
};


} // namespace Util