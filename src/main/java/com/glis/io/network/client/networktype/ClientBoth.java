package com.glis.io.network.client.networktype;

import com.glis.io.network.codec.SubscribeMessageEncoder;
import com.glis.io.network.networktype.Both;
import com.glis.io.network.networktype.Downstream;
import com.glis.io.network.networktype.Upstream;

import java.util.NoSuchElementException;

/**
 * @author Glis
 */
public final class ClientBoth extends Both {
    /**
     * {@inheritDoc}
     */
    public ClientBoth(Upstream upstream, Downstream downstream) {
        super((channelHandlerContext, linkData) -> {
            try {
                channelHandlerContext.pipeline().remove(SubscribeMessageEncoder.class);
            } catch (NoSuchElementException ignored) {}
        }, upstream, downstream);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTypeIdentifier() {
        return 2;
    }
}
