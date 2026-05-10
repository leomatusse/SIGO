package model;

import java.io.Serializable;
import java.time.LocalDate;



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Leo
 */

public class Crime implements Serializable  {
    LocalDate dataDoCrime;
    private String localDoCrime, armaDoCrime, tipoDeCrime;
    private EstadoFlagrancia estadoDeFlagrancia;
    public enum EstadoFlagrancia {
        FLAGRANTE,
        NAO_FLAGRANTE
    }
    public Crime(String localDoCrime, LocalDate dataDoCrime, String armaDoCrime, EstadoFlagrancia estadoDeFlagrancia, String tipoDeCrime) {
        this.localDoCrime = localDoCrime;
        this.dataDoCrime = dataDoCrime;
        this.armaDoCrime = armaDoCrime;
        this.estadoDeFlagrancia = estadoDeFlagrancia;
        this.tipoDeCrime = tipoDeCrime;
    }

    public String getLocalDoCrime() {
        return localDoCrime;
    }

    public void setLocalDoCrime(String localDoCrime) {
        this.localDoCrime = localDoCrime;
    }

    public LocalDate getDataDoCrime() {
        return dataDoCrime;
    }

    public void setDataDoCrime(LocalDate dataDoCrime) {
        this.dataDoCrime = dataDoCrime;
    }

    public String getArmaDoCrime() {
        return armaDoCrime;
    }

    public void setArmaDoCrime(String armaDoCrime) {
        this.armaDoCrime = armaDoCrime;
    }

    public String getTipoDeCrime() {
        return tipoDeCrime;
    }

    public void setTipoDeCrime(String tipoDeCrime) {
        this.tipoDeCrime = tipoDeCrime;
    }
    
    
}

