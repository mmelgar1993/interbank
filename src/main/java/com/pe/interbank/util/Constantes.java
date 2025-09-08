package com.pe.interbank.util;

public enum Constantes {
    ACTIVO,
    DESACTIVO;

    public String getValor() {
        return this.name(); // Devuelve "ACTIVO" o "DESACTIVO"
    }


}
