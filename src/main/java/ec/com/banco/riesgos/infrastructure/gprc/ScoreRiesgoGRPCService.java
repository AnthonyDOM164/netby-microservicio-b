package ec.com.banco.riesgos.infrastructure.gprc;

import ec.com.banco.riesgos.application.ScoreRiesgoApplicationService;
import ec.com.banco.riesgos.bancoaustro.model.CedulaEcuatoriana;
import ec.com.banco.riesgos.bancoaustro.model.ScoreEvaluacion;
import ec.com.banco.riesgos.grpc.CedulaRequest;
import ec.com.banco.riesgos.grpc.ScoreResponse;
import ec.com.banco.riesgos.grpc.ScoreRiesgo;
import io.grpc.Status;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.concurrent.TimeUnit;

@GrpcService
public class ScoreRiesgoGRPCService implements ScoreRiesgo {

    private static final Logger LOGGER = Logger.getLogger(DeudasRiesgoGRPCService.class);

    private final ScoreRiesgoApplicationService applicationService;

    @Inject
    public ScoreRiesgoGRPCService(ScoreRiesgoApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public Uni<ScoreResponse> obtenerScore(CedulaRequest request) {
        long startedAt = System.nanoTime();

        String correlationId = CorrelationIdContext.currrentOrDefault();

        return Uni.createFrom()
                .item(() -> CedulaEcuatoriana.of(request.getCedula()))
                .invoke(cedula -> LOGGER.infov(
                        "event=evaluacion_score_inicio correlaiton_id={0} cedula={1}",
                        correlationId,
                        cedula.enmascarada()
                ))
                .onItem()
                .transformToUni(applicationService::obtenerScore)
                .map(this::toGrpc)
                .invoke(response -> LOGGER.infov(
                        "event=evaluacion_score_fin correaltion_id={0} score={1} epased_ms={2}",
                        correlationId,
                        response.getScore(),
                        elapsedMillis(startedAt)
                ))
                .onFailure(IllegalArgumentException.class)
                .transform(error -> Status.INVALID_ARGUMENT.withDescription(error.getMessage()).asRuntimeException());
    }

    private ScoreResponse toGrpc(ScoreEvaluacion evaluacion){
        return ScoreResponse.newBuilder()
                .setScore(evaluacion.score())
                .build();
    }

    private long elapsedMillis(long startedAt) {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt);
    }

}
