package model;




import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Leo
 */
public class Civil implements Serializable {
    
    public enum papelCivil{
      VITIMA,
      SUSPEITO,
      TESTEMUNHA
    }
    

    private String nome, idBI, sexo,contacto, dataNascimento, nacionalidade, endereco, estadoCivil;
    private papelCivil papel;
   
    private ArrayList <String> idBILista = new ArrayList<>();
    private ArrayList <String> listaContacto = new ArrayList<>();
    private  boolean validarID, validarContacto, validarAno;
    private boolean existeID, existeContacto;
    
    public Civil (){
        
    }
    
    public Civil (String nome, String idBI, String contacto, papelCivil papel, String dataNascimento, String nacionalidade, String sexo, String enderenco, String estadoCivil){
        this.nome = nome;
        
       
            for (String id: idBILista){
                if(idBI.equals(id)){
                     existeID = true;
                     break;
                }
            }
        if (!existeID) {
           this.idBI = idBI;
           idBILista.add(idBI); 
        } else this.validarID = false;   
      
    
        for (String nCelular : listaContacto){
            if (contacto.equals(nCelular)){
                existeContacto = true;
                break;
            }  
        }
     
        if (!existeContacto){
           this.contacto = contacto;
           listaContacto.add(contacto);
        } else this.validarContacto = false;
        
        this.papel = papel;
        this.dataNascimento = dataNascimento;
        this.nacionalidade = nacionalidade;
        this.sexo = sexo;
        this.endereco = enderenco;
        this.estadoCivil = estadoCivil;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getBI() {
        return idBI;
    }

    public void setBI(String idBI) {
        this.idBI = idBI;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public papelCivil getPapel() {
        return papel;
    }
    
    public static boolean validarAnos (LocalDate dataNascimento){
        if (dataNascimento == null) return false;
        LocalDate hoje = LocalDate.now();
        int idade = hoje.getYear() - dataNascimento.getYear();
        if (dataNascimento.plusYears(idade).isAfter(hoje)) idade--;
        return idade > 18;
        
      
    }
    
    public boolean validarIDBI(){
      return idBILista.isEmpty();
    }
    
    public boolean validarContacto (){
        return !listaContacto.isEmpty();
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public boolean isValidarID() {
        return validarID;
    }

    public void setValidar(boolean validarID) {
        this.validarID = validarID;
    }

    public ArrayList<String> getIdBILista() {
        return idBILista;
    }

    public ArrayList<String> getListaContacto() {
        return listaContacto;
    }

    public boolean isValidarContacto() {
        return validarContacto;
    }

    public void setValidarContacto(boolean validarContacto) {
        this.validarContacto = validarContacto;
    }

    public boolean isValidarAno() {
        return validarAno;
    }

    public void setValidarAno(boolean validarAno) {
        this.validarAno = validarAno;
    }
    
    public boolean validarID(String numeroID){
      
            if (!idBILista.contains(numeroID)){
                    return  true;
            }else{
                System.out.println("NUMERO DE BILHETE DE IDENTIDADE JA EXISTE");
                }   
         
        return false;
    }
}
