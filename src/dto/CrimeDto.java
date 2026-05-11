/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.time.LocalDate;
import model.Crime;
import model.Crime.EstadoFlagrancia;

/**
 *
 * @author Leo
 */
public class CrimeDto {
    public String localDoCrime;
    public LocalDate dataDoCrime;
    public  String armaDoCrime;
    public   Crime.EstadoFlagrancia estadoDeFlagrancia;
    public String tipoDeCrime;
    public String autorDoCrime;
    public CrimeDto(String localCrime, LocalDate dataCrime, String armaCrime, EstadoFlagrancia estadoD, String tipoCrime) {
        this.localDoCrime = localCrime;
        this.dataDoCrime = dataCrime;
        this.armaDoCrime = armaCrime;
        this.tipoDeCrime = tipoCrime;
        this.estadoDeFlagrancia = estadoD;
        
    }
   
}
