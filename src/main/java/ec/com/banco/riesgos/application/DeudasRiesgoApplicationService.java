package ec.com.banco.riesgos.application;

import ec.com.banco.riesgos.bancoaustro.model.CedulaEcuatoriana;
import ec.com.banco.riesgos.bancoaustro.model.DeudasEvaluacion;
import ec.com.banco.riesgos.bancoaustro.service.DeudasRiesgoGenerator;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Duration;

@ApplicationScoped
public class DeudasRiesgoApplicationService {

    public static final Duration DELAY_DEUDAS = Duration.ofMillis(1_500);

    private final DeudasRiesgoGenerator generator;

    @Inject
    public DeudasRiesgoApplicationService(DeudasRiesgoGenerator generator) {
        this.generator = generator;
    }

    public Uni<DeudasEvaluacion> obtenerDeudas(CedulaEcuatoriana cedula) {
        return Uni.createFrom()
                .item(() -> generator.generar(cedula))
                .onItem()
                .delayIt()
                .by(DELAY_DEUDAS);
    }

}
