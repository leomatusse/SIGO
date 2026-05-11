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
    
   public void saveDetencao(ArrayList<Detencao> detencao) throws FileNotFoundException {
    try (ObjectOutputStream save = new ObjectOutputStream(new FileOutputStream("C:\\Users\\user\\Desktop\\detencoes.dat"))) {
        save.writeObject(detencoes);
        save.writeInt(contadorDetencao); // guardar contador
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public void readDetencao() {
    try (ObjectInputStream read = new ObjectInputStream(new FileInputStream("C:\\Users\\user\\Desktop\\detencoes.dat"))) {
        this.detencoes = (ArrayList<Detencao>) read.readObject();
        this.contadorDetencao = read.readInt(); // ler contador
        System.out.println("Contador Detencao carregado: " + contadorDetencao);
    } catch (IOException e) {
        this.detencoes = new ArrayList<>();
        this.contadorDetencao = 0;
    } catch (ClassNotFoundException ex) {
        ex.printStackTrace();
    }
}
    
  public void saveOcorrencia(ArrayList<Ocorrencia> ocorrencia) {
    try (ObjectOutputStream save = new ObjectOutputStream(new FileOutputStream("C:\\Users\\user\\Desktop\\ocorrencias.dat"))) {
        save.writeObject(ocorrencias);
        save.writeInt(contadorBO); // guardar contador
    } catch (IOException e) {
        e.printStackTrace();
    }
  }

public void readOcorrencia() throws ClassNotFoundException {
    try (ObjectInputStream read = new ObjectInputStream(new FileInputStream("C:\\Users\\user\\Desktop\\ocorrencias.dat"))) {
        this.ocorrencias = (ArrayList<Ocorrencia>) read.readObject();
        this.contadorBO = read.readInt(); // ler contador
        System.out.println("Contador BO carregado: " + contadorBO);
    } catch (IOException e) {
        this.ocorrencias = new ArrayList<>();
        this.contadorBO = 0;
    } catch (ClassNotFoundException ex) {
        ex.printStackTrace();
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
