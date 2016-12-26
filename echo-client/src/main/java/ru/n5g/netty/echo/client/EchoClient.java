package ru.n5g.netty.echo.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;


public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: " + EchoClient.class.getSimpleName() + " <host> <port>");
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        new EchoClient(host, port).start();
    }

    public void start() throws Exception {
        //assigned to handle the event processing
        //which includes creating new connections and processing inbound and outbound data
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //Creates Bootstrap
            //created to initialize the client
            Bootstrap b = new Bootstrap();
            //Specifies EventLoopGroup to handle client events
            //NIO is implementation is needed
            b.group(group)
                    //Channel type is the one for NIO transport
                    .channel(NioSocketChannel.class)
                    //Sets the server's InetSocketAddress'
                    .remoteAddress(new InetSocketAddress(host, port))
                    //Adds an EchoClientHandler to the pipeline when a Channel is created
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            //Connects to the remote peer
            //waits until the connect completes
            ChannelFuture f = b.connect().sync();
            //Block until the Channel close
            f.channel().closeFuture().sync();
        } finally {
            //Shuts down the thread pools and the release of all resources
            group.shutdownGracefully().sync();
        }
    }
}
