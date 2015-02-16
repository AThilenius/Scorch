"""
This is a thin Python shim around Anvil. It is important to note that ctypes can only link against a dynamically loaded
assembly, thus Anvil itself has been wrapped in a dynlib with an exposed C API. Headers are not needed for managed
marshaling of unmanaged assemblies.
"""
import sys
import os
import ctypes
import platform
import struct

if "windows" in platform.system().lower():
    if 8 * struct.calcsize("P") != 32:
        print("I'm sorry, but you must run Anvil with Python 32 bit on Windows. Look for the Python X86 installer instead of the X64.")
        sys.exit(0)
    dir = os.path.dirname(__file__)
    libPath = os.path.join(dir, 'AnvilDll.dll')
else:
    dir = os.path.dirname(__file__)
    libPath = os.path.join(dir, 'libAnvilDyn.dylib')

anvilLib = ctypes.CDLL(libPath)

class Anvil:
    @staticmethod
    def sayHello():
        anvilLib.DynAnvilSayHello()

    @staticmethod
    def loadLevel(levelNumber):
        sparkCount = anvilLib.DynAnvilLoadLevel(levelNumber)
        if sparkCount < 0:
            print "Failed to load level."
            return None
        else:
            return Level(levelNumber, sparkCount)

class Level:
    def __init__(self, levelNumber, sparkCount):
        self.LevelNumber = levelNumber
        self.SparkCount = sparkCount

    def getSpark(self, sparkNumber = 0):
        if self.SparkCount <= sparkNumber:
            print "Invalid spark id"
            return None
        else:
            return Spark(self.LevelNumber, sparkNumber)



class Spark:
    def __init__(self, levelNumber, sparkId):
        self.LevelNumber = levelNumber
        self.SparkID = sparkId

    def moveForward(self):
        return anvilLib.DynSparkMoveForward(self.LevelNumber, self.SparkID)

    def moveBackward(self):
        return anvilLib.DynSparkMoveBackward(self.LevelNumber, self.SparkID)

    def turnLeft(self):
        anvilLib.DynSparkTurnLeft(self.LevelNumber, self.SparkID)

    def turnRight(self):
        anvilLib.DynSparkTurnRight(self.LevelNumber, self.SparkID)