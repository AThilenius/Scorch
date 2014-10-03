using System;
using System.Threading;
using System.Threading.Tasks;
using Thrift.Server;
using Thrift.Transport;

namespace Flame
{
    public class HostProgram
    {
        public static void Main(String[] args)
        {
            QueueHttpServer httpServer = new QueueHttpServer(80);
            Task.Factory.StartNew(httpServer.listen);

            Task.Factory.StartNew(() =>
                {
                    FlameServiceHandler handler = new FlameServiceHandler(httpServer);
                    FlameService.Processor processor = new FlameService.Processor(handler);
                    TServerTransport serverTransport = new TServerSocket(9090);
                    TServer server = new TThreadPoolServer(processor, serverTransport);

                    Console.WriteLine("Starting Remote Service...");
                    server.Serve();
                });

            Thread.Sleep(100);

            while (true)
            {
                Console.Write("> ");
                String command = Console.ReadLine();
                Int32 id = Int32.Parse(command.Split()[0]);
                command = command.Remove(0, 2);
                Console.Write("[" + command + "]: ");
                String results = httpServer.RunCommand(id, command);
                Console.WriteLine(results);
            }

        }
    }
}
