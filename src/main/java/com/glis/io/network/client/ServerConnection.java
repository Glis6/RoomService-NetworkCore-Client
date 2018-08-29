package com.glis.io.network.client;

import io.github.cdimascio.dotenv.Dotenv;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.Closeable;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Glis
 */
public class ServerConnection implements Closeable {
    /**
     * The time between reconnection attempts. In seconds.
     */
    private final static int RECONNECTION_TIMER = 10;
    /**
     * The {@link Logger} to use.
     */
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    /**
     * The timer to schedule the connection on.
     */
    private Timer timer = new Timer();

    /**
     * The {@link EventLoopGroup} that attempts to sustain the connection.
     */
    private EventLoopGroup workerGroup;

    /**
     * Attempts to connect.
     */
    public void connect() throws Exception {
        logger.info("Attempting to connect...");
        workerGroup = new NioEventLoopGroup();
        try {
            final Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ClientNetworkPipeline());
            // Start the client.
            ChannelFuture f = bootstrap.connect(Objects.requireNonNull(Dotenv.load().get("host")), Integer.parseInt(Objects.requireNonNull(Dotenv.load().get("port")))).sync();
            // Wait until the connection is closed.
            f.channel().closeFuture().addListener(channelFuture -> {
                logger.info("Connection lost.");
                scheduleConnect(RECONNECTION_TIMER);
                logger.info("Attempting to reconnect in " + RECONNECTION_TIMER + " seconds...");
                workerGroup.shutdownGracefully();
            }).sync();
        } catch (Exception e) {
            logger.info("Unable to connect to the server.");
            scheduleConnect(RECONNECTION_TIMER);
            logger.info("Attempting to reconnect in " + RECONNECTION_TIMER + " seconds...");
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * @param secondsDelay The delay in seconds
     */
    private void scheduleConnect(long secondsDelay) throws Exception {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    connect();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "", e);
                }
            }
        }, secondsDelay * 1000);
    }

    /**
     * Called when the connection gets closed.
     */
    @Override
    public void close() {
        if(workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }
}
