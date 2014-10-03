using System;
using System.Collections.Generic;

namespace Flame
{
    public class FlameServiceHandler : FlameService.Iface
    {
        private QueueHttpServer m_httpServer;
        private Dictionary<MovementTypes, String> m_movementCommandsLookup
            = new Dictionary<MovementTypes, String>
        {
            { MovementTypes.Forward,    "return turtle.forward()" },
            { MovementTypes.Backward,   "return turtle.back()" },
            { MovementTypes.Up,         "return turtle.up()" },
            { MovementTypes.Down,       "return turtle.down()" },
            { MovementTypes.TurnLeft,   "return turtle.turnLeft()" },
            { MovementTypes.TurnRight,  "return turtle.turnRight()" }
        };

        public FlameServiceHandler(QueueHttpServer httpServer) : base()
        {
            m_httpServer = httpServer;
        }

        public List<Spark> GetAllSparks()
        {
            List<Spark> turtles = new List<Spark>();

            lock (m_httpServer.ComputerBuffersById)
            {
                foreach (var kvp in m_httpServer.ComputerBuffersById)
                    turtles.Add(new Spark { SparkID = kvp.Key });
            }

            return turtles;
        }

        public bool DispatchMovementCommand(Spark spark, MovementTypes movementCommand)
        {
            bool retValue = true;

            try
            {
                String retString = m_httpServer
                    .RunCommand(spark.SparkID, m_movementCommandsLookup[movementCommand]);
                retValue = bool.Parse(retString);
            }
            catch (Exception ex) { }

            return retValue;
        }

        #region Not Suported By FlameCC

        public Spark CreateSpark(Location worldLocation)
        {
            throw new NotImplementedException();
        }

        public void RemoveSpark(Spark sparkToRemove)
        {
            throw new NotImplementedException();
        }

        public void RemoveAllSparks()
        {
            throw new NotImplementedException();
        }

        #endregion
    }
}
