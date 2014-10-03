using System.IO;
using System.Net.Sockets;
using System.Threading.Tasks;

namespace Flame
{
    public abstract class HttpServer
    {
        protected int port;
        TcpListener listener;
        bool is_active = true;

        public HttpServer(int port)
        {
            this.port = port;
        }

        public void listen()
        {
            listener = new TcpListener(port);
            listener.Start();
            while (is_active)
            {
                TcpClient s = listener.AcceptTcpClient();
                HttpProcessor processor = new HttpProcessor(s, this);
                Task.Factory.StartNew(processor.process);
            }
        }

        public abstract void HandleGetRequest(HttpProcessor p);
        public abstract void HandlePostRequest(HttpProcessor p, StreamReader inputData);           
    }

}



