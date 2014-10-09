cmake_minimum_required(VERSION 2.8)

# Project Layout
# - PROJECT_HOME_DIRECTORY
#   - bin
#   - src
#     - TestRunner.cpp
#   - libs
#     - include
#     - lib

# Paths
STRING(REPLACE "src" "" PROJECT_HOME_DIRECTORY ${CMAKE_HOME_DIRECTORY})
string(REGEX REPLACE "(^[\\/]|[\\/]$)" "" PROJECT_HOME_DIR_TRIMMED "${PROJECT_HOME_DIRECTORY}")
string(REGEX MATCH "([^\\/]+)$" PROJECT_NAME "${PROJECT_HOME_DIR_TRIMMED}")
SET(SRC_DIRECTORY "${PROJECT_HOME_DIRECTORY}src")
SET(INCLUDE_DIRECTORY "${PROJECT_HOME_DIRECTORY}libs/include")

if (UNIX)
  SET(LIBS_DIRECTORY "${PROJECT_HOME_DIRECTORY}libs/UnixLib")
  FILE(GLOB THIRD_PARTY_LIBS ${LIBS_DIRECTORY}/Debug/*.a)
ENDIF()

if (WIN32)
  SET(LIBS_DIRECTORY "${PROJECT_HOME_DIRECTORY}libs/WindowsLib")
  FILE(GLOB THIRD_PARTY_LIBS ${LIBS_DIRECTORY}/Debug/*.lib)

  # Aditional Libs for Windows
  LIST(APPEND THIRD_PARTY_LIBS shlwapi.lib)
ENDIF()


MESSAGE(" ")
MESSAGE(" #                 Project Name: ${PROJECT_NAME}")
MESSAGE(" #       Project Home Directory: ${PROJECT_HOME_DIRECTORY}")
MESSAGE(" #             Source Directory: ${SRC_DIRECTORY}")
MESSAGE(" # Additional Include Directory: ${INCLUDE_DIRECTORY}")
MESSAGE(" #    Additional Libs Directory: ${LIBS_DIRECTORY}")
MESSAGE(" # Linking:")
FOREACH(library ${THIRD_PARTY_LIBS})
  MESSAGE(" # - ${library}")
ENDFOREACH()
MESSAGE(" ")

# Project Spec
project(${PROJECT_NAME})
SET(EXE_NAME ${PROJECT_NAME})

# VS work arounds
if( MSVC ) # VS2012 doesn't support correctly the tuples yet
  add_definitions( /D _VARIADIC_MAX=10 )
endif()

# Set Include / Lib folders
INCLUDE_DIRECTORIES(${SRC_DIRECTORY})
INCLUDE_DIRECTORIES(${INCLUDE_DIRECTORY})
LINK_DIRECTORIES(${LIBS_DIRECTORY})

# GLOB all files
FILE(GLOB_RECURSE SOURCE_FILES ${SRC_DIRECTORY}/*.h ${SRC_DIRECTORY}/*.cpp)
FILE(GLOB_RECURSE TEST_FILES ${SRC_DIRECTORY}/*_Test.h ${SRC_DIRECTORY}/*_Test.cpp ${SRC_DIRECTORY}/TestRunner.cpp)
list(REMOVE_ITEM SOURCE_FILES ${TEST_FILES})

# Prune EXE folders, and add each path as a
# include directory
foreach(FILE ${SOURCE_FILES}) 
  get_filename_component(PARENT_DIR "${FILE}" PATH)

  # Add it as an include directory
  INCLUDE_DIRECTORIES(${PARENT_DIR})

  # Trim trailing slashes in both
  string(REGEX REPLACE "(^\\|\\$)" "" PARENT_DIR_TRIMMED "${PARENT_DIR}")
  string(REGEX REPLACE "(^\\|\\$)" "" SRC_DIRECTORY_TRIMMED "${SRC_DIRECTORY}")

  # Make relative
  STRING(REPLACE "${SRC_DIRECTORY_TRIMMED}" "" GROUP ${PARENT_DIR_TRIMMED})
  string(REPLACE "/" "\\" GROUP "${GROUP}")
  source_group("${GROUP}" FILES "${FILE}")
endforeach()

# Prune TEST folders
# include directory
foreach(FILE ${TEST_FILES}) 
  get_filename_component(PARENT_DIR "${FILE}" PATH)

  # Trim trailing slashes in both
  string(REGEX REPLACE "(^\\|\\$)" "" PARENT_DIR_TRIMMED "${PARENT_DIR}")
  string(REGEX REPLACE "(^\\|\\$)" "" SRC_DIRECTORY_TRIMMED "${SRC_DIRECTORY}")

  # Make relative
  STRING(REPLACE "${SRC_DIRECTORY_TRIMMED}" "" GROUP ${PARENT_DIR_TRIMMED})
  string(REPLACE "/" "\\" GROUP "${GROUP}")
  source_group("${GROUP}" FILES "${FILE}")
endforeach()

# C++ 11
ADD_DEFINITIONS(-std=c++11)

# Add main EXE project
ADD_EXECUTABLE(${EXE_NAME} ${SOURCE_FILES} )
TARGET_LINK_LIBRARIES(${EXE_NAME} ${THIRD_PARTY_LIBS})

# Add test EXE project
ADD_EXECUTABLE(${EXE_NAME}_Test ${TEST_FILES} )
TARGET_LINK_LIBRARIES(${EXE_NAME}_Test ${THIRD_PARTY_LIBS})