package ec.com.banco.riesgos.application;

import ec.com.banco.riesgos.bancoaustro.model.CedulaEcuatoriana;
import ec.com.banco.riesgos.bancoaustro.model.ScoreEvaluacion;
import ec.com.banco.riesgos.bancoaustro.service.ScoreRiesgoGenerator;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Duration;

@ApplicationScoped
public class ScoreRiesgoApplicationService {

    public static final Duration DELAY_SCORE = Duration.ofMillis(1_500);

    private final ScoreRiesgoGenerator generator;

    @Inject
    public ScoreRiesgoApplicationService(ScoreRiesgoGenerator generator) {
        this.generator = generator;
    }

    public Uni<ScoreEvaluacion> obtenerScore(CedulaEcuatoriana cedula) {
        return Uni.createFrom()
                .item(() -> generator.generar(cedula))
                .onItem()
                .delayIt()
                .by(DELAY_SCORE);
    }

}
