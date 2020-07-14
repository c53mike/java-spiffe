package io.spiffe.workloadapi;

import io.spiffe.exception.JwtBundleException;
import io.spiffe.exception.X509ContextException;
import io.spiffe.spiffeid.TrustDomain;
import io.spiffe.workloadapi.grpc.Workload;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class GrpcConversionUtilsTest {

    @Test
    void toX509Context_emptyResponse() {
        Iterator<Workload.X509SVIDResponse> iterator = Collections.emptyIterator();
        try {
            GrpcConversionUtils.toX509Context(iterator);
        } catch (X509ContextException e) {
            assertEquals("X.509 Context response from the Workload API is empty", e.getMessage());
        }
    }

    @Test
    void toBundleSet() {
        Iterator<Workload.JWTBundlesResponse> iterator = Collections.emptyIterator();
        try {
            GrpcConversionUtils.toBundleSet(iterator);
        } catch (JwtBundleException e) {
            assertEquals("JWT Bundle response from the Workload API is empty", e.getMessage());
        }
    }

    @Test
    void parseX509Bundle_corruptedBytes() {
        try {
            GrpcConversionUtils.parseX509Bundle(TrustDomain.of("example.org"), "corrupted".getBytes());
        } catch (X509ContextException e) {
            assertEquals("X.509 Bundles could not be processed", e.getMessage());
        }
    }

    @Test
    void testPrivateConstructor_InstanceCannotBeCreated() throws IllegalAccessException, InstantiationException {
        val constructor = GrpcConversionUtils.class.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
            fail();
        } catch (InvocationTargetException e) {
            assertEquals("This is a utility class and cannot be instantiated", e.getCause().getMessage());
        }
    }
}