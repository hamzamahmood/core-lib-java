package io.apimatic.core_lib.request.async;

import java.io.IOException;

import io.apimatic.core_interfaces.http.request.Request;

/**
 * 
 * Request Supplier
 *
 */
@FunctionalInterface
public interface RequestSupplier {
    /**
     * Supplies the Request object.
     * 
     * @return An object of type Request
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    Request supply() throws IOException;
}
