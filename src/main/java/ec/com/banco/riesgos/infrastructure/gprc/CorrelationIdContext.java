package ec.com.banco.riesgos.infrastructure.gprc;

import io.grpc.Context;

public final class CorrelationIdContext {

    static final Context.Key<String> CORRELATION_ID = Context.key("correlation-id");
    private static final String DEFAULT_CORRELATION_ID = "none-correlation-id";

    private CorrelationIdContext() {
    }

    public static String currrentOrDefault() {
        String correlationId = CORRELATION_ID.get();
        return correlationId == null || correlationId.isBlank() ? DEFAULT_CORRELATION_ID : correlationId;
    }

}
