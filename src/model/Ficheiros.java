package model;





import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Leo
 */
public class Ficheiros {
    private ArrayList <Ocorrencia> ocorrencias;
    private int contadorDetencao = 0;
    private int contadorBO = 0;
    
    private ArrayList <Detencao> detencoes;
    public Ficheiros (){
        ocorrencias = new ArrayList <>();

        detencoes = new ArrayList <>();
    }
    
    public void saveDetencao(ArrayList <Detencao> detencao) throws FileNotFoundException{
        try(ObjectOutputStream save = new ObjectOutputStream (new FileOutputStream ("C:\\Users\\user\\Desktop\\detencoes.dat"))) {
            save.writeObject(detencoes);
            
        } catch(IOException e ){
            e.printStackTrace();
            System.out.println("Erro ao guardar ficheiro:"+ e.getMessage());
         }
    }
    
    public void readDetencao(){
        try(ObjectInputStream read = new ObjectInputStream(new FileInputStream("C:\\Users\\user\\Desktop\\ detencoes.dat"))){
            this.detencoes = (ArrayList<Detencao>)read.readObject();
        }catch(IOException e){
            System.out.println("Erro ao ler ficheiro");
            e.printStackTrace();
            
            this.detencoes = new ArrayList<>();
       
        } catch (ClassNotFoundException ex) {
            System.getLogger(Ficheiros.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            System.out.println("Ficheiro nao encontrado");
        }
    }
    
    public void saveOcorrencia (ArrayList <Ocorrencia> ocorrencia){
         try(ObjectOutputStream save = new ObjectOutputStream (new FileOutputStream ("C:\\Users\\user\\Desktop\\ocorrencias.dat"))) {
          
          save.writeObject(ocorrencias);
          
      } catch (IOException e){
        System.out.println("Erro ao guardar ficheiro: " + e.getMessage());
        e.printStackTrace();
      }
    }
    
    public void readOcorrencia () throws ClassNotFoundException{
         try (ObjectInputStream read = new ObjectInputStream(new FileInputStream("C:\\Users\\user\\Desktop\\ocorrencias.dat"))) {
               this.ocorrencias = (ArrayList<Ocorrencia>)read.readObject();
               
           }catch (IOException e){
                System.out.println("Erro ao ler o ficheiro.");
                e.printStackTrace();
                
                this.ocorrencias = new ArrayList<>();
                
           } catch (ClassNotFoundException ex) {
            System.getLogger(Ficheiros.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            System.out.println("Ficheiro nao encontrado");
            
        }   
    }
    


    public ArrayList<Ocorrencia> getOcorrencias()  {
        return ocorrencias;
    }

    public void setOcorrencias(ArrayList<Ocorrencia> ocorrencias) {
        this.ocorrencias = ocorrencias;
    }

    public ArrayList<Detencao> getDetencoes() {
        return detencoes;
    }

    public void setDetencoes(ArrayList<Detencao> detencoes) {
        this.detencoes = detencoes;
    }
    
    public void adicionarOcorrencia (Ocorrencia ocorrencia){
        ocorrencias.add(ocorrencia);
    }
    
    public Ocorrencia procurarBO(String numeroBO){
        for(Ocorrencia ocorrencia: ocorrencias){
            if (numeroBO.equals(ocorrencia.getNumeroBO())){
                return ocorrencia;
            }
        }
        return null;
    }
   public void adicionarDetencao (Detencao detencao){
       detencoes.add(detencao);
   }
   
   public Detencao procurarDetencao (String numeroDetencao){
       for (Detencao detencao: detencoes){
            System.out.println("Digitado: " + numeroDetencao);
            System.out.println("Guardado: " + detencao.getIdDetencao());
           if (numeroDetencao.equals(detencao.getIdDetencao()))
               return detencao;
       }
       return null;
   }
   
   public int proximoIDDetencao (){
       return ++contadorDetencao;
   }
     public int proximoIDBO (){
       return ++contadorBO;
   }
}
