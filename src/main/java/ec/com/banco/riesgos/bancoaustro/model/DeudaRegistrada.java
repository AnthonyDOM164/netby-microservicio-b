package ec.com.banco.riesgos.bancoaustro.model;

import java.math.BigDecimal;
import java.util.Objects;

public record DeudaRegistrada(String institucion, BigDecimal monto) {

    public DeudaRegistrada {

        institucion = Objects.requireNonNull(institucion, "institucion no puede ser null").trim();
        monto = Objects.requireNonNull(monto, "monto no puede ser null");

        if (institucion.isBlank()) {
            throw new IllegalArgumentException("institucion no puede ser vacia");
        }

        if (monto.signum() <= 0) {
            throw new IllegalArgumentException("monto no puede ser negativo");
        }

    }

}
