package com.glis.io.network.client;

import com.glis.io.network.codec.AuthorizationEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author Glis
 */
public final class ClientNetworkPipeline extends ChannelInitializer<SocketChannel> {
    /**
     * {@inheritDoc}
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        final ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addFirst(new AuthorizationEncoder());
        pipeline.addLast(new ClientAuthorizationHandler());
    }
}
