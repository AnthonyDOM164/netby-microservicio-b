package ec.com.banco.riesgos.bancoaustro.model;

import java.util.Objects;

public record ScoreEvaluacion(Integer score) {

    public ScoreEvaluacion {
        score = Objects.requireNonNull(score, "score no puede ser null");

        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("El score debe ser entre 0 y 100");
        }
    }

}
