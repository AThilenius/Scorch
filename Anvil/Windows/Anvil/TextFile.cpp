//
//  File.cpp
//  Anvil
//
//  Created by Alec Thilenius on 2/7/15.
//  Copyright (c) 2015 Alec Thilenius. All rights reserved.
//
#include "PrefixHeader.h"
#include "TextFile.h"

#define BOOST_FILESYSTEM_VERSION 3
#define BOOST_FILESYSTEM_NO_DEPRECATED
#include <boost/filesystem.hpp>

namespace Util {
    
    
std::vector<TextFile> TextFile::GlobDirectory (std::string dir) {
    std::vector<TextFile> allFiles;
    
    boost::filesystem::path rootPath(boost::filesystem::current_path().string() + dir);
    
    if(!boost::filesystem::exists(rootPath) || !boost::filesystem::is_directory(rootPath)) {
        return allFiles;
    }
    
    boost::filesystem::recursive_directory_iterator it(rootPath);
    boost::filesystem::recursive_directory_iterator endit;
    
    while(it != endit)
    {
        if(boost::filesystem::is_regular_file(*it)) {
            boost::filesystem::path filePath = it->path();
            TextFile file;
            
            // Name
            file.Name = filePath.stem().string();
            
            // Extension
            file.Extension = filePath.extension().string();
            
            // Modify Date
            std::time_t modifyTime = boost::filesystem::last_write_time(it->path());
            std::tm* utcTime = std::gmtime(&modifyTime);
            char buffer[32];
            std::strftime(buffer, 32, "%Y-%m-%d %H:%M:%S", utcTime);
            file.ModifyDate = std::string(buffer);
            
            // Contents
            std::ifstream t(filePath.string());
            file.Contents = std::string((std::istreambuf_iterator<char>(t)),
                            std::istreambuf_iterator<char>());
            
            allFiles.push_back(file);
        }
        
        ++it;
    }
    
    
    return allFiles;
}


std::string TextFile::ReadFileToString (std::string path) {
    std::ifstream t(path);
    return std::string((std::istreambuf_iterator<char>(t)),
                       std::istreambuf_iterator<char>());
}
    
    
} // namespace Util