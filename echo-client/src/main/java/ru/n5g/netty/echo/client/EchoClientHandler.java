package ru.n5g.netty.echo.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

@Sharable //Marks this class as one whose instances can be shared among channels
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * Called after the connection to the server is established
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //When notified that the channel is active, sends a message
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    /**
     * Called when a message is received from the server
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        //Logs a dump of the received message
        System.out.println("Client received: " + msg.toString(CharsetUtil.UTF_8));
    }

    /**
     * Called if an exception is raised during processing
     * On exception, logs the error and closes channel
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
