using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Collections.Concurrent;

namespace ComputerCraftRemote
{
    public class QueueHttpServer : HttpServer
    {
        public class ComputerBuffers
        {
            public String PoolName = "Unassigned";
            public String StartingLocation;

            // Blocking, Concurrent Queues
            private BlockingCollection<String> m_commandBuffer 
                = new BlockingCollection<String>(new ConcurrentQueue<String>());
            private BlockingCollection<Object> m_returnBuffer
                = new BlockingCollection<Object>(new ConcurrentQueue<Object>());

            /// <summary>
            /// Used by calling code (Thrift)
            /// Enqueues a command into the command buffer and waits for a
            /// return in the return buffer.
            /// </summary>
            public String ThriftRunCommand(String command)
            {
                m_commandBuffer.Add(command);
                Object returnValue = m_returnBuffer.Take();

                if (returnValue is Exception)
                    throw (Exception)returnValue;

                return (String) m_returnBuffer.Take();
            }

            /// <summary>
            /// Used by ComputerCraft
            /// DeQueues an item from the command buffer, or blocks until
            /// one is ready.
            /// </summary>
            public String CCDeQueueCommandBuffer()
            {
                return m_commandBuffer.Take();
            }

            /// <summary>
            /// Used by ComputerCraft
            /// Enqueues an item to the return buffer, releasing the blocked
            /// Thrift RPC
            /// </summary>
            public void CCEnqueueReturnBuffer(Object returnValue)
            {
                m_returnBuffer.Add(returnValue);
            }

        }

        private enum UrlCommandTypes
        {
            Link,
            Command,
            Error
        }

        public Dictionary<String, String> OwnerByPoolName = new Dictionary<String, String>();
        public Dictionary<int, ComputerBuffers> ComputerBuffersById = new Dictionary<int, ComputerBuffers>();

        public QueueHttpServer(int port) : base(port)
        {
        }

        public override void HandleGetRequest(HttpProcessor p)
        {
            int computerId = 0;
            switch (ParseCommandAndIdFromUrl(p.http_url, out computerId))
            {
                case UrlCommandTypes.Command:
                    {
                        ComputerBuffers buffer = GetComputerBuffers(computerId);
                        p.WriteSuccess();
                        p.outputStream.WriteLine(buffer.CCDeQueueCommandBuffer());
                        break;
                    }

                case UrlCommandTypes.Link:
                    {
                        ComputerBuffers buffer = GetComputerBuffers(computerId);
                        p.WriteSuccess();

                        lock (OwnerByPoolName)
                        {
                            foreach (var kvp in OwnerByPoolName)
                                p.outputStream.WriteLine("Pool [" + kvp.Key + "] Owned by [" + kvp.Value + "]");
                        }
                        p.outputStream.WriteLine();

                        lock (ComputerBuffersById)
                        {
                            foreach (var kvp in ComputerBuffersById)
                                if (kvp.Value.PoolName == buffer.PoolName)
                                    if (kvp.Key == computerId)
                                        p.outputStream.WriteLine("- [" + kvp.Key + "] In pool [" + kvp.Value.PoolName + "]" );
                                    else
                                        p.outputStream.WriteLine("  [" + kvp.Key + "] In pool [" + kvp.Value.PoolName + "]");
                        }
                        p.outputStream.WriteLine();

                        break;
                    }
            }

            
        }

        public override void HandlePostRequest(HttpProcessor p, StreamReader inputData)
        {
            int computerId = 0;
            string data = inputData.ReadToEnd();

            switch (ParseCommandAndIdFromUrl(p.http_url, out computerId))
            {
                case UrlCommandTypes.Command:
                    {

                        ComputerBuffers buffer = GetComputerBuffers(computerId);
                        // Enqueue the return from the POST data
                        buffer.CCEnqueueReturnBuffer(data);
                        break;
                    }

                case UrlCommandTypes.Error:
                    {

                        ComputerBuffers buffer = GetComputerBuffers(computerId);
                        // Enqueue the return from the POST data
                        buffer.CCEnqueueReturnBuffer(new Exception(data));
                        break;
                    }

                case UrlCommandTypes.Link:
                    {
                        ComputerBuffers buffer = GetComputerBuffers(computerId);

                        // Parse handshake
                        Dictionary<String, String> values = new Dictionary<String, String>();
                        String[] lines = data.Split('\n');
                        foreach (String line in lines)
                        {
                            String[] args = line.Split(':');
                            if (args.Length == 2)
                                values[args[0]] = args[1];
                        }

                        // Set defaults
                        if (!values.ContainsKey("PoolName") || values["PoolName"].Trim() == "") values["PoolName"] = "Unassigned";
                        if (!values.ContainsKey("Location") || values["Location"].Trim() == "") values["Location"] = "0,0,0";

                        buffer.PoolName = values["PoolName"];
                        buffer.StartingLocation = values["Location"];

                        // Add the pool
                        lock (OwnerByPoolName)
                        {
                            if (!OwnerByPoolName.ContainsKey(values["PoolName"]))
                                OwnerByPoolName.Add(values["PoolName"], "None");
                        }

                        p.WriteSuccess();
                        break;
                    }
            }

        }

        public String RunCommand(int computerId, String command)
        {
            // Get the ComputerBuffer, or create it
            ComputerBuffers buffer = GetComputerBuffers(computerId);

            // Run the command
            return buffer.ThriftRunCommand(command);
        }

        private ComputerBuffers GetComputerBuffers(int id)
        {
            lock (ComputerBuffersById)
            {
                if (!ComputerBuffersById.ContainsKey(id))
                    ComputerBuffersById[id] = new ComputerBuffers();

                return ComputerBuffersById[id];
            }
        }

        private UrlCommandTypes ParseCommandAndIdFromUrl(String url, out int ID)
        {
            String[] tokens = url.Trim(new char[] { '/' }).Split('/');

            if (tokens.Length != 2)
                throw new InvalidOperationException("Invalid URL path");

            ID = int.Parse(tokens[1]);

            if (tokens[0] == "link")
                return UrlCommandTypes.Link;

            else if (tokens[0] == "command")
                return UrlCommandTypes.Command;

            else if (tokens[0] == "error")
                return UrlCommandTypes.Error;

            else
                throw new NotSupportedException("Unsupported URL command type: " + tokens[0]);
        }

    }
}
