from lib.AnvilApi import Anvil

myLevel = Anvil.loadLevel(2)
mySpark = myLevel.getSpark()
mySpark.moveForward()
mySpark.turnLeft()
mySpark.moveBackward()
mySpark.turnRight()