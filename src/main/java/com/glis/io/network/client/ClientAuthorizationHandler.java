package com.glis.io.network.client;

import com.glis.exceptions.InvalidTypeException;
import com.glis.io.network.AuthorizationHandler;
import com.glis.io.network.AuthorizationResponse;
import com.glis.io.network.codec.AuthorizationResponseDecoder;
import com.glis.message.AuthorizationMessage;
import io.github.cdimascio.dotenv.Dotenv;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Objects;
import java.util.logging.Level;

/**
 * @author Glis
 */
public final class ClientAuthorizationHandler extends AuthorizationHandler {
    /**
     * {@inheritDoc}
     */
    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        ctx.writeAndFlush(new AuthorizationMessage(Dotenv.load().get("clientId"), Dotenv.load().get("clientSecret")));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof AuthorizationResponse) {
            final AuthorizationResponse authorizationResponse = (AuthorizationResponse) msg;
            if (authorizationResponse == AuthorizationResponse.SUCCESS) {
                logger.info("Authorization success. Awaiting network type.");
            } else {
                logger.severe(String.format("Received an invalid response to authentication, '%s'.", authorizationResponse.toString()));
            }
            return;
        }
        if(msg instanceof Integer) {
            final int response = (int)msg;
            logger.info("Got the network type back, connecting...");
            networkTypes.get(response).link(ctx, null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        logger.log(Level.WARNING, "Exception attempting to authorize to the server.", cause);
    }
}
