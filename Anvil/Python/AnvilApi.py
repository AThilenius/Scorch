"""
This is a thin Python shim around Anvil. It is important to note that ctypes can only link against a dynamically loaded
assembly, thus Anvil itself has been wrapped in a dynlib with an exposed C API. Headers are not needed for managed
marshaling of unmanaged assemblies.
"""
import json
import urllib2

def postJson(data):
    configFile = open ('BlazeConfig.json')
    config = json.load(configFile)

    req = urllib2.Request(config['url'])
    dataJson = json.dumps(data).replace("<auth_token>", config['auth_token'])
    req.add_header('Content-Type', 'application/json')

    return urllib2.urlopen(req, dataJson)

class Anvil:
    @staticmethod
    def sayHello():
        responseJson = postJson({
            "Thilenius.BFEProtos.BFEInfoQueryRequest.BFEInfoQueryRequest_ext":
                {
                    "auth_token": "<auth_token>"
                }
        }).read()
        print (json.loads(responseJson)
                ['Thilenius.BFEProtos.BFEInfoQueryResponse.BFEInfoQueryResponse_ext']
                ['blaze_response'])

    @staticmethod
    def loadLevel(levelNumber):
        responseJson = postJson({
            "Thilenius.BFEProtos.BFELoadLevelRequest.BFELoadLevelRequest_ext":
                {
                    "auth_token": "<auth_token>",
                    "levelNumber": levelNumber,
                    "seed": 1234
                }
        }).read()
        # {"Thilenius.BFEProtos.BFELoadLevelResponse.BFELoadLevelResponse_ext": {"failure_reason": "...."}}
        # {"Thilenius.BFEProtos.BFELoadLevelResponse.BFELoadLevelResponse_ext": {"spark_count": 0}}
        return Level(levelNumber, int(json.loads(responseJson)
                ['Thilenius.BFEProtos.BFELoadLevelResponse.BFELoadLevelResponse_ext']
                ['spark_count']))


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
        responseJson = postJson({
            "Thilenius.BFEProtos.BFESparkCommand.BFESparkCommand_ext":
                {
                    "command": "MOVE_FORWARD",
                    "spark_id": self.SparkID,
                    "auth_token": "<auth_token>"
                }
        }).read()

        # {"Thilenius.BFEProtos.BFESparkResponse.BFESparkResponse_ext": {"response_bool": true}}

        return bool(json.loads(responseJson)
                ['Thilenius.BFEProtos.BFESparkResponse.BFESparkResponse_ext']
                ['response_bool'])

    def moveBackward(self):
        responseJson = postJson({
            "Thilenius.BFEProtos.BFESparkCommand.BFESparkCommand_ext":
                {
                    "command": "MOVE_BACKWARD",
                    "spark_id": self.SparkID,
                    "auth_token": "<auth_token>"
                }
        }).read()

        # {"Thilenius.BFEProtos.BFESparkResponse.BFESparkResponse_ext": {"response_bool": true}}

        return bool(json.loads(responseJson)
                ['Thilenius.BFEProtos.BFESparkResponse.BFESparkResponse_ext']
                ['response_bool'])

    def turnLeft(self):
        responseJson = postJson({
            "Thilenius.BFEProtos.BFESparkCommand.BFESparkCommand_ext":
                {
                    "command": "TURN_LEFT",
                    "spark_id": self.SparkID,
                    "auth_token": "<auth_token>"
                }
        }).read()

    def turnRight(self):
        responseJson = postJson({
            "Thilenius.BFEProtos.BFESparkCommand.BFESparkCommand_ext":
                {
                    "command": "TURN_RIGHT",
                    "spark_id": self.SparkID,
                    "auth_token": "<auth_token>"
                }
        }).read()


Anvil.sayHello()
spark = Anvil.loadLevel(2).getSpark()
spark.moveForward()
spark.moveBackward()
spark.turnLeft()
spark.turnRight()