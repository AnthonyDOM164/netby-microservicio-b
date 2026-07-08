package ec.com.banco.riesgos.infrastructure.gprc;

import ec.com.banco.riesgos.application.DeudasRiesgoApplicationService;
import ec.com.banco.riesgos.bancoaustro.model.CedulaEcuatoriana;
import ec.com.banco.riesgos.bancoaustro.model.DeudaRegistrada;
import ec.com.banco.riesgos.bancoaustro.model.DeudasEvaluacion;
import io.grpc.Status;
import io.quarkus.grpc.GrpcService;

import io.smallrye.mutiny.Uni;
import ec.com.banco.riesgos.grpc.*;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

@GrpcService
public class DeudasRiesgoGRPCService implements DeudasRiesgo {
    private static final Logger LOGGER = Logger.getLogger(DeudasRiesgoGRPCService.class);

    private final DeudasRiesgoApplicationService applicationService;

    @Inject
    public DeudasRiesgoGRPCService(DeudasRiesgoApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public Uni<DeudasResponse> obtenerDeudas(CedulaRequest request) {
        long startedAt = System.nanoTime();

        String correlationId = CorrelationIdContext.currrentOrDefault();

        return Uni.createFrom()
                .item(() -> CedulaEcuatoriana.of(request.getCedula()))
                .invoke(cedula -> LOGGER.infov(
                        "event=evaluacion_deduas_inicio correlaiton_id={0} cedula={1}",
                        correlationId,
                        cedula.enmascarada()
                ))
                .onItem()
                .transformToUni(applicationService::obtenerDeudas)
                .map(this::toGrpc)
                .invoke(response -> LOGGER.infov(
                        "event=evaluacion_deudas_fin correaltion_id={0} total_deudas={1} epased_ms={2}",
                        correlationId,
                        response.getDeudasCount(),
                        elapsedMillis(startedAt)
                ))
                .onFailure(IllegalArgumentException.class)
                .transform(error -> Status.INVALID_ARGUMENT.withDescription(error.getMessage()).asRuntimeException());
    }

    private DeudasResponse toGrpc(DeudasEvaluacion evaluacion) {
        DeudasResponse.Builder response = DeudasResponse.newBuilder();
        evaluacion.deudas().stream()
                .map(this::toGrpc)
                .forEach(response::addDeudas);
        return response.build();
    }

    private DeudaResponse toGrpc(DeudaRegistrada deuda) {
        return DeudaResponse.newBuilder()
                .setInstitucion(deuda.institucion())
                .setMonto(deuda.monto().setScale(2, RoundingMode.HALF_UP).toPlainString())
                .build();
    }

    private long elapsedMillis(long startedAt) {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt);
    }
}
