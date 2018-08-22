package com.glis.io.network.client.networktype;

import com.glis.io.network.input.library.MessageLibrary;
import com.glis.io.network.networktype.CustomNetworkTypeHandler;
import com.glis.io.network.networktype.Downstream;

/**
 * @author Glis
 */
public final class ClientDownStream extends Downstream {
    /**
     * {@inheritDoc}
     */
    public ClientDownStream(CustomNetworkTypeHandler customNetworkTypeHandler, MessageLibrary messageLibrary) {
        super(customNetworkTypeHandler, messageLibrary);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTypeIdentifier() {
        return 0;
    }
}
