package com.thilenius.flame.service.thrift;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import com.thilenius.flame.service.thrift.genfiles.FlameService;

public class FlameServiceServer {

	public static FlameServiceHandler FlameHandler;
	public static FlameService.Processor FlameProcessor;

	public FlameServiceServer() {
		try {
			FlameHandler = new FlameServiceHandler();
			FlameProcessor = new FlameService.Processor(FlameHandler);

			Runnable simple = new Runnable() {
				public void run() {
					simple(FlameProcessor);
				}
			};

			new Thread(simple).start();
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public static void simple(FlameService.Processor processor) {
		try {
			TServerTransport serverTransport = new TServerSocket(9090);
			TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));
			
			System.out.println("Starting the simple server...");
			server.serve();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
