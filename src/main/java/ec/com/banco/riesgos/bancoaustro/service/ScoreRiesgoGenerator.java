package ec.com.banco.riesgos.bancoaustro.service;

import ec.com.banco.riesgos.bancoaustro.model.CedulaEcuatoriana;
import ec.com.banco.riesgos.bancoaustro.model.ScoreEvaluacion;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
public class ScoreRiesgoGenerator {

    public ScoreRiesgoGenerator() {
    }

    public ScoreEvaluacion generar(CedulaEcuatoriana cedula) {
        Objects.requireNonNull(cedula, "cedula no puede ser nulo");
        int scoreRandom = ThreadLocalRandom.current().nextInt(0, 101);
        return new ScoreEvaluacion(scoreRandom);
    }

}
