package ec.com.banco.riesgos.bancoaustro.model;

import java.util.List;
import java.util.Objects;

public record DeudasEvaluacion(List<DeudaRegistrada> deudas) {

    public DeudasEvaluacion {
        deudas = List.copyOf(Objects.requireNonNull(deudas, "deudas no puede ser nulo"));
    }

}
