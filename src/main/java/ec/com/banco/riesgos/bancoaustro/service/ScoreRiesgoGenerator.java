package ec.com.banco.riesgos.bancoaustro.service;

import ec.com.banco.riesgos.bancoaustro.model.CedulaEcuatoriana;
import ec.com.banco.riesgos.bancoaustro.model.ScoreEvaluacion;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Objects;
import java.util.random.RandomGenerator;

@ApplicationScoped
public class ScoreRiesgoGenerator {

    private final RandomGenerator random;

    public ScoreRiesgoGenerator() {
        this(RandomGenerator.of("L64X128MixRandom"));
    }

    ScoreRiesgoGenerator(RandomGenerator random) {
        this.random = Objects.requireNonNull(random, "random no puede ser nulo");
    }

    public ScoreEvaluacion generar(CedulaEcuatoriana cedula) {
        Objects.requireNonNull(cedula, "cedula no puede ser nulo");
        return new ScoreEvaluacion(random.nextInt(101));
    }

}
