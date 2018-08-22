package com.glis.io.network.client;

import com.glis.io.network.AuthorizationHandler;
import com.glis.message.AuthorizationMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Objects;

/**
 * @author Glis
 */
public final class ClientAuthorizationHandler extends AuthorizationHandler {
    /**
     * {@inheritDoc}
     */
    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        ctx.writeAndFlush(new AuthorizationMessage(Integer.valueOf(Objects.requireNonNull(dotenv.get("networkType"))), dotenv.get("networkName")));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        try {
            if (byteBuf.isReadable()) {
                final int response = byteBuf.readByte();
                if(response == 1) {
                    logger.info("Got a positive response back, connecting...");
                    networkTypes.get(Integer.valueOf(Objects.requireNonNull(dotenv.get("networkType")))).link(ctx, null);
                } else {
                    throw new Exception("Something went wrong connecting to the server.");
                }
            }
        } finally {
            byteBuf.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
