/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.time.LocalDateTime;
//import model.Civil;
//import model.Crime;
import model.Ocorrencia;

/**
 *
 * @author Leo
 */
public class OcorrenciaDto {
    public  String numeroBO;
    public String localOcorrencia, nomeOficial;
    public LocalDateTime dataHora;
    public Ocorrencia.EstadoBO estadoBO; 

    public OcorrenciaDto(String local, String oficialNome) {
       this.localOcorrencia =  local; 
       this.nomeOficial = oficialNome;
    }
  
  

  
}
