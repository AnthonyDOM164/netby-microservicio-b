package ec.com.banco.riesgos.bancoaustro.service;

import ec.com.banco.riesgos.bancoaustro.model.CedulaEcuatoriana;
import ec.com.banco.riesgos.bancoaustro.model.DeudaRegistrada;
import ec.com.banco.riesgos.bancoaustro.model.DeudasEvaluacion;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
public class DeudasRiesgoGenerator {

    public static final List<String> INSTITUCIONES = List.of(
            "Banco Pichincha",
            "Banco de Loja",
            "Banco del Austro"
    );

    public DeudasRiesgoGenerator() {
    }

    public DeudasEvaluacion generar(CedulaEcuatoriana cedula) {
        Objects.requireNonNull(cedula, "cedula no puede ser nulo");

        Integer cantidadDeudas = ThreadLocalRandom.current().nextInt(1, 4);

        List<DeudaRegistrada> deudas = new ArrayList<>(cantidadDeudas);

        for (int i = 0; i < cantidadDeudas; i++) {
            String institucion = INSTITUCIONES.get(ThreadLocalRandom.current().nextInt(INSTITUCIONES.size()));
            Long centavos = ThreadLocalRandom.current().nextLong(10_000L, 800_001L);
            deudas.add(new DeudaRegistrada(institucion, BigDecimal.valueOf(centavos, 2)));
        }
        return new DeudasEvaluacion(deudas);
    }

}
