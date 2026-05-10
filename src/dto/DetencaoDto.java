/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.time.LocalDateTime;
import model.Detencao;

/**
 *
 * @author Leo
 */
public class DetencaoDto {
 
    public String numeroBO;
    public String idDetencao;
    public String agenteResponsavel;
    public String desfecho;
    public LocalDateTime horaDetencao; 
   
    public DetencaoDto  (String agenteResponsavel, String numeroBO){
        this.agenteResponsavel = agenteResponsavel;
        this.numeroBO =numeroBO;
        
    }

    public DetencaoDto() {
       
    }
    
   

   
}
