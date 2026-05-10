/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import model.Civil;
import model.Civil.papelCivil;

/**
 *
 * @author Leo
 */
public class CivilDto {
    public String nome;
    public String idBI;
    public String contacto;
    public Civil.papelCivil papel;
    public String dataNascimento;
    public String nacionalidade;
    public String sexo;
    public String enderenco;
    public String estadoCivil;  

    public CivilDto(String nomeCivil, String contactoCivil, papelCivil papel, String nacionalidade, String sexo, String endereco, String estadoCivil) {
        this.nome = nomeCivil;

        this.contacto = contactoCivil;
  
        this.nacionalidade = nacionalidade;
        this.sexo = sexo;
        this.enderenco = endereco;
        this.estadoCivil = estadoCivil;
        this.papel = papel;
    }

   
 
}
