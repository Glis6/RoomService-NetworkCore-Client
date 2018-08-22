package com.glis.io.network.client.networktype;

import com.glis.io.network.networktype.CustomNetworkTypeHandler;
import com.glis.io.network.networktype.Upstream;

/**
 * @author Glis
 */
public final class ClientUpstream extends Upstream {
    /**
     * {@inheritDoc}
     */
    public ClientUpstream(CustomNetworkTypeHandler customNetworkTypeHandler) {
        super(customNetworkTypeHandler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTypeIdentifier() {
        return 1;
    }
}
