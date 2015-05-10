package com.thilenius.flame.rest;

/**
 * Created by Alec on 3/29/15.
 */
public class RestServer {

    public RestHttpServer HttpServer;
    public StatementDispatch Dispatch;

    public RestServer() {
        this.HttpServer = new RestHttpServer();
        new Thread(this.HttpServer).start();
        this.Dispatch = new StatementDispatch(this.HttpServer);
    }

    public void onGameTick() {
        Dispatch.dispatchAll();
    }

}
