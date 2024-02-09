package org.cuatrovientos.blablacar.models;

import android.graphics.drawable.Drawable;

import java.util.Date;

public class User {

    private int id;
    private String name;
    private String lastName;
    private Date birthDate;
    private String email;
    private String password;
    private Rute rutes;
    private Drawable userIcon;

    public User(){} //por si se necesita

    // Constructor sin establecer imagen (y obviamente sin rutas)
    public User(int id, String name, String lastName, Date birthDate, String email, String password) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
    }

    /*
    * GETTERS
    * */
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Rute getRutes() {
        return rutes;
    }

    public Drawable getUserIcon() {
        return userIcon;
    }

    /*
    * SETTERS (posible necesidad futura de tener que borrar alguno por seguridad)
    * */

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRutes(Rute rutes) {
        this.rutes = rutes;
    }

    public void setUserIcon(Drawable userIcon) {
        this.userIcon = userIcon;
    }
}
