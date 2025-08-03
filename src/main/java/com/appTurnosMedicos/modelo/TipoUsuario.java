package com.appTurnosMedicos.modelo;

public class TipoUsuario {

    private int id_tipo;
    private String tipo_usuario;
    
    public TipoUsuario(int id, String tipo) {
        this.id_tipo=id;
        this.tipo_usuario=tipo;
    }
    
    public int getId_tipo() {
        return id_tipo;
    }
    public String getTipo_usuario() {
        return tipo_usuario;
    }
}
