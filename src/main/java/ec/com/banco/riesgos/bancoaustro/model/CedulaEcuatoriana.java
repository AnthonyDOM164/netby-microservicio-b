package ec.com.banco.riesgos.bancoaustro.model;

public record CedulaEcuatoriana(String valor) {

    public CedulaEcuatoriana {
        valor = valor == null ? "" : valor.trim();

        if (valor == null || valor.isEmpty()) {
            throw new IllegalArgumentException("la ceudla no puede ser nula o vacia");
        }

        if (!valor.matches("\\d{10}")) {
            throw new IllegalArgumentException("la cedula debe contener 10 digitos");
        }
    }

    public static CedulaEcuatoriana of(String valor) {
        return new CedulaEcuatoriana(valor);
    }

    public String enmascarada() {
        return valor.substring(0, 2) + "*****" + valor.substring(8);
    }

}
