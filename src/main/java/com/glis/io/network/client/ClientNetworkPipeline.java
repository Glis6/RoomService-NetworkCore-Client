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
     * The client id to authenticate with.
     */
    private final String clientId;

    /**
     * The client secret to authenticate with.
     */
    private final String clientSecret;

    /**
     * @param clientId     The client id to authenticate with.
     * @param clientSecret The client secret to authenticate with.
     */
    public ClientNetworkPipeline(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        final ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addFirst(new ClientAuthorizationHandler(clientId, clientSecret));
        pipeline.addFirst(new AuthorizationResponseDecoder());
        pipeline.addFirst(new AuthorizationEncoder());
    }
}
