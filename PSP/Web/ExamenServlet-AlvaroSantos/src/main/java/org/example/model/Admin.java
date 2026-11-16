package org.example.model;

public class Admin {
    private String nombre;
    private String contrasenia;

    public Admin(String user) {
        nombre=user;
        contrasenia = "123";
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
