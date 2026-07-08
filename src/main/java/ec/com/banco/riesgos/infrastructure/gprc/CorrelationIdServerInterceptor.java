package ec.com.banco.riesgos.infrastructure.gprc;

import io.grpc.*;
import io.quarkus.grpc.GlobalInterceptor;
import jakarta.enterprise.context.ApplicationScoped;

@GlobalInterceptor
@ApplicationScoped
public class CorrelationIdServerInterceptor implements ServerInterceptor {

    private static final Metadata.Key<String> CORRELATION_ID_HEADER = Metadata.Key.of("correlation-id", Metadata.ASCII_STRING_MARSHALLER);
    private static final Metadata.Key<String> X_CORRELATION_ID_HEADER = Metadata.Key.of("x-correlation-id", Metadata.ASCII_STRING_MARSHALLER);


    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        String correlationId = metadata.get(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = metadata.get(X_CORRELATION_ID_HEADER);
        }

        Context context = correlationId == null || correlationId.isBlank() ? Context.current() : Context.current().withValue(CorrelationIdContext.CORRELATION_ID, correlationId);

        return Contexts.interceptCall(context, serverCall, metadata, serverCallHandler);
    }
}
