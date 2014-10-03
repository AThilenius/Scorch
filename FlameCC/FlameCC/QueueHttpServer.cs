using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.IO;

namespace Flame
{
    public class QueueHttpServer : HttpServer
    {
        public class ComputerBuffers
        {
            // Blocking, Concurrent Queues
            private BlockingCollection<String> m_commandBuffer 
                = new BlockingCollection<String>(new ConcurrentQueue<String>());
            private BlockingCollection<String> m_returnBuffer 
                = new BlockingCollection<String>(new ConcurrentQueue<String>());

            /// <summary>
            /// Used by calling code (Thrift)
            /// Enqueues a command into the command buffer and waits for a
            /// return in the return buffer.
            /// </summary>
            public String ThriftRunCommand(String command)
            {
                m_commandBuffer.Add(command);
                return m_returnBuffer.Take();
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
            public void CCEnqueueReturnBuffer(String returnValue)
            {
                m_returnBuffer.Add(returnValue);
            }

        }

        private enum UrlCommandTypes
        {
            Link,
            Command
        }

        public Dictionary<int, ComputerBuffers> ComputerBuffersById
            = new Dictionary<int, ComputerBuffers>();

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
                        // Get the ComputerBuffer, or create it
                        ComputerBuffers buffer = GetComputerBuffers(computerId);

                        // Write the command in the GET response
                        p.WriteSuccess();
                        p.outputStream.WriteLine(buffer.CCDeQueueCommandBuffer());
                        break;
                    }

                case UrlCommandTypes.Link:
                    {
                        ComputerBuffers buffer = GetComputerBuffers(computerId);
                        p.WriteSuccess();
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

                case UrlCommandTypes.Link:
                    {
                        // Nothing to do here any more
                        break;
                    }
            }

            p.WriteSuccess();
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

            ID = Int32.Parse(tokens[1]);

            if (tokens[0] == "link")
                return UrlCommandTypes.Link;

            else if (tokens[0] == "command")
                return UrlCommandTypes.Command;

            else
                throw new NotSupportedException("Unsupported URL command type: " + tokens[0]);
        }

    }
}
