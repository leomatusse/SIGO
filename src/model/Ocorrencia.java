package model;



    


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Leo
 */


import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
public class Ocorrencia implements Serializable {



    public enum EstadoBO {
        EM_ANALISE,
        DETENCAO_ASSOCIADA,
        EM_PROCESSO,
        ENCERRADO
    }
    private String numeroBO;
    private String localOcorrencia, nomeOficial;
    private LocalDateTime dataHora;
    private EstadoBO estadoBO;
    private ArrayList <Civil> envolvidos;
    private ArrayList <Crime> crimes;
    private Detencao detencao;
    int contador = 0;
    
    public Ocorrencia (){
        
    }
    
    public Ocorrencia( String localOcorrencia, String nomeOficial ) {
        this.numeroBO = gerarIDBO();
       // this.tipoDeBO = tipoDeBO;
      //  this.descricaoBO = descricaoBO;
        this.localOcorrencia = localOcorrencia;
        this.dataHora = LocalDateTime.now();
        this.nomeOficial = nomeOficial;
        this.estadoBO = estadoBO.EM_ANALISE;
        envolvidos = new ArrayList <>();
        crimes = new ArrayList<>();

    }

    public String getNumeroBO() {
        return numeroBO;
    }

  

    public LocalDateTime getDataHora() {
        return this.dataHora;
    }

    public String getLocalOcorrencia() {
        return localOcorrencia;
    }

    public String getNomeOficial() {
        return nomeOficial;
    }

    public EstadoBO getEstadoBO() {
        return estadoBO;
    }

    public ArrayList<Civil> getEnvolvidos() {
        return envolvidos;
    }

    public void setLocalOcorrencia(String localOcorrencia) {
        this.localOcorrencia = localOcorrencia;
    }
    
    public void adicionarCivil (Civil c){
        envolvidos.add(c);
    }
    
    public void adicionarCrime(Crime cr){
        crimes.add(cr);
    }
    
    public String procurarSuspeito (){
      for (Civil c: envolvidos)  {
          if (c.getPapel()== Civil.papelCivil.SUSPEITO)
              return c.getNome();
      } 
      return null;
    }
    
    public void alterarEstado (EstadoBO novoEstado){
        this.estadoBO = novoEstado;
    }
    
    public void listarEnvolvidos (){
        for (Civil c: envolvidos){
            System.out.println("Nome: " + c.getNome());
            System.out.println("Papel: "+ c.getPapel());
            System.out.println("Contacto: "+ c.getContacto());
        }
    }
    
    public void iniciarDetencao (Detencao detencao){
        this.detencao = detencao;
        this.estadoBO = EstadoBO.DETENCAO_ASSOCIADA;
    }
     
    public String gerarIDBO (){
       
        int anoAtual = Year.now().getValue();
        contador ++;
        return String.valueOf(contador)+"/"+anoAtual;

    }

    public ArrayList<Crime> getCrimes() {
        return crimes;
    }
    
   public String gerarIDBO (int contador){
       int anoAtual = Year.now().getValue();
       return contador + "/"+ anoAtual;
   }

    public void setNumeroBO(String numeroBO) {
        this.numeroBO = numeroBO;
    }
   
}
