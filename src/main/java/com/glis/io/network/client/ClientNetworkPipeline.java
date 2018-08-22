package com.glis.io.network.client;

import com.glis.io.network.codec.AuthorizationEncoder;
import com.glis.io.network.codec.AuthorizationResponseDecoder;
import com.glis.io.network.codec.AuthorizationResponseEncoder;
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
        pipeline.addFirst(new ClientAuthorizationHandler());
        pipeline.addFirst(new AuthorizationResponseDecoder());
        pipeline.addFirst(new AuthorizationEncoder());
    }
}
