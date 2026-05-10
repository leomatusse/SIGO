/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dto.CivilDto;
import dto.CrimeDto;
import dto.DetencaoDto;
import dto.OcorrenciaDto;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
//import javax.swing.JOptionPane;
import model.Civil;
import model.Crime;
import model.Detencao;
import model.Ficheiros;
import model.Ocorrencia;
import view.MenuPrincipal;
import view.TelaDetencao;
import view.TelaOcorrencia;

/**
 *
 * @author Leo
 */
public class ControllerTrabalho {
    private Ficheiros ficheiro;
    private TelaOcorrencia viewOcorrencias;
    private TelaDetencao viewDetencao;
    private MenuPrincipal viewMenu;
    public ControllerTrabalho (MenuPrincipal viewMenu,  Ficheiros ficheiro){
        this.viewMenu = viewMenu;
        this.ficheiro = ficheiro;
       // viewMenu.abrirMenu();
       viewMenu.setEntradaSistema(() ->{
           configurarEventos();
       });
    

    }
    public void configurarEventos (){
        viewMenu.getBtnOcorrencia().addActionListener(e ->{
          viewOcorrencias = new TelaOcorrencia();
          
          eventosOcorrencia (viewOcorrencias);
          viewOcorrencias.setVisible(true);
          
        
        });
        viewMenu.getBtnDetencao().addActionListener(e ->{
          viewDetencao = new TelaDetencao();
          eventosDetencao (viewDetencao);
          viewDetencao.setVisible(true);
        });
       viewMenu.getBtnSair().addActionListener(e -> {
           System.exit(0);
       });
     
    }
    
    public void eventosDetencao (TelaDetencao telaDetencao){
        telaDetencao.getBtnRegistar().addActionListener(e -> {
            try {
                cadastrarDetencao();
            } catch (ClassNotFoundException ex) {
                System.getLogger(ControllerTrabalho.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (FileNotFoundException ex) {
                System.getLogger(ControllerTrabalho.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
        
        telaDetencao.getBtnLimpar().addActionListener(e ->{
            telaDetencao.limparCamposDetencao();
        });
        
        telaDetencao.getBtnEncerrar().addActionListener(e ->{
            encerrarDetencao ();
        });
        telaDetencao.getBtnActualizar().addActionListener(e -> {
            listarDetencoes();
        });

        telaDetencao.getBtnPesquisar().addActionListener(e -> {
           pesquisarDetencao();
        });  
        try {
            ficheiro.readDetencao();
            telaDetencao.mostrarAlertas((ficheiro.getDetencoes()));
            
        } catch (Exception ex){
            ex.printStackTrace();
        }
        
    }
    
    public void encerrarDetencao (){
        if (viewDetencao.getIDDetencao().isEmpty()){
              JOptionPane.showMessageDialog(null,
                "Insira o ID da detencaoo.", "Aviso",
                JOptionPane.WARNING_MESSAGE);
              return;
        }
        if (viewDetencao.getIndexDesfecho ()==0){
            JOptionPane.showMessageDialog(null,
                "Seleccione o desfecho da detencao.", "Aviso",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        String desfecho = viewDetencao.getDesfecho();
         int conf = JOptionPane.showConfirmDialog(null,
        "Confirma o encerramento? ID: " + viewDetencao.getIDDetencao() + "\nDesfecho: " + desfecho,
        "Confirmar Encerramento", JOptionPane.YES_NO_OPTION);
         
         if (conf == JOptionPane.YES_OPTION){
            ficheiro.readDetencao();
            Detencao detencao = ficheiro.procurarDetencao(viewDetencao.getIDDetencao());
            if (detencao == null){
                JOptionPane.showMessageDialog(null, "ID DE DETENCAO NAO ENCONTRADO.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            detencao.encerrarDetencao(Detencao.StatusDetencao.LIBERTADO, viewDetencao.getDesfecho());
            try{
                ficheiro.saveDetencao(ficheiro.getDetencoes());
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String horaEncerramento = LocalDateTime.now().format(fmt);
                JOptionPane.showMessageDialog(null,"Detencao encerrada!\nID: " + viewDetencao.getIDDetencao() +"\nDesfecho: " + desfecho + "\nEncerrada em: " + horaEncerramento);
                viewDetencao.limparCamposDetencao();
            } catch (FileNotFoundException ex){
                JOptionPane.showMessageDialog(null, "Erro ao guardar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
         }
    }
    public void eventosOcorrencia ( TelaOcorrencia telaOcorrencia){
        telaOcorrencia.getBtnLimpar().addActionListener (e -> {
           limparCampos ();   
        });
        
        telaOcorrencia.getBtnGuardar().addActionListener((ActionEvent e) -> {
            registarOcorrencia();
        });
        telaOcorrencia.getBtnActualizarOcorrencias().addActionListener (e ->{
            listarOcorrencias();
        });
    }
    public void limparCampos (){
       viewOcorrencias.limparCamposOcorrencia();
       viewOcorrencias.limparCamposCivil();
       viewOcorrencias.limparCamposCrime();      
    }
    public void registarOcorrencia(){
        OcorrenciaDto ocorrenciaDto = viewOcorrencias.recolherDadosOcorrencia();
        Ocorrencia ocorrencia = new Ocorrencia(ocorrenciaDto.localOcorrencia, ocorrenciaDto.nomeOficial);
        if (ocorrenciaDto.localOcorrencia.trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Insira o local da ocorrencia.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (ocorrenciaDto.nomeOficial.trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Insira o nome do oficial.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resposta = 0;
        do{
            Civil civil = new Civil ();
            CivilDto civilDto = viewOcorrencias.recolherDadosCivil();
            String idBI;
            boolean existe = false;
            do {
                idBI = viewOcorrencias.pedirNumeroBI();
            
            if (civil.validarID(idBI))
                existe = true;
              else {
                viewOcorrencias.mostrarMensagem("NUMERO DE BI EXISTE OU INVALIDO.");
                int resposta1 = JOptionPane.showConfirmDialog(null, "DIGITE NOVAMENTE", "AVISO", JOptionPane.YES_NO_OPTION);
                if (resposta1 != JOptionPane.YES_OPTION) return;
                viewOcorrencias.limparCampoBI();
                }
            } while (!existe);
              civilDto.idBI= idBI;
           
            existe= false;
            LocalDate dataNascimento;
            do{
                dataNascimento = viewOcorrencias.pedirDataNascimento();
                //dataNascimento = viewOcorrencias.pedirDataNascimento();
                if (dataNascimento == null){
                    JOptionPane.showMessageDialog(null, "Seleccione a data de nascimento.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    continue; 
                }
           
               if (Civil.validarAnos(dataNascimento))
                   existe = true;
               else {
                    viewOcorrencias.mostrarMensagem("MENOR DE IDADE");
                    int resposta1 = JOptionPane.showConfirmDialog(null, "DIGITE A DATA DE NASCIMENTO NOVAMENTE", "AVISO", JOptionPane.YES_OPTION);
                    if (resposta1 != JOptionPane.YES_OPTION) return;
                    viewOcorrencias.limparCampoDataNasc();
               }
            }while ( !existe);
            civilDto.dataNascimento = dataNascimento.toString();
            ocorrencia.getEnvolvidos().add(new Civil (civilDto.nome, civilDto.idBI, civilDto.contacto, civilDto.papel, civilDto.dataNascimento, civilDto.nacionalidade, civilDto.sexo, civilDto.enderenco, civilDto.estadoCivil));
            resposta = viewOcorrencias.perguntarDecisao();
        }while (resposta== JOptionPane.YES_OPTION);
        viewOcorrencias.setLblBO(ocorrencia.getNumeroBO());
        viewOcorrencias.setLblData(  ocorrencia.getDataHora());
      
        CrimeDto crimeDto = viewOcorrencias.recolherDadosCrime();
        if (crimeDto.dataDoCrime == null){
            JOptionPane.showMessageDialog(null, "Seleccione a data do crime.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return; 
        }
        ocorrencia.getCrimes().add(new Crime (crimeDto.localDoCrime, crimeDto.dataDoCrime, crimeDto.armaDoCrime, crimeDto.estadoDeFlagrancia, crimeDto.tipoDeCrime));
        ficheiro.adicionarOcorrencia(ocorrencia);
        ficheiro.saveOcorrencia(ficheiro.getOcorrencias());  
        viewOcorrencias.mostrarMensagem("OCORRENCIA REGISTADA COM SUCESSO");
        
    }
    
    
    
    public void cadastrarDetencao () throws ClassNotFoundException, FileNotFoundException{
  
        boolean existe = false;
   
        DetencaoDto detencaoDto = viewDetencao.dadosDetencao();
     //   Detencao detencao = new Detencao (detencaoDto.agenteResponsavel);
        if (detencaoDto.agenteResponsavel.trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Insira o nome do Agente Responsavel.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (detencaoDto.numeroBO.trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Insira o numero do BO.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        ficheiro.readOcorrencia();
        ArrayList <Ocorrencia> ocorrencias = ficheiro.getOcorrencias();
        for (Ocorrencia oc: ocorrencias){
            if (detencaoDto.numeroBO.equals(oc.getNumeroBO())){
               existe = true;
               break;
            }
        }
        if (!existe) JOptionPane.showMessageDialog(null, "NUMERO DE BO NAO ENCONTRADO");
        else { 
            Detencao detencao = new Detencao (detencaoDto.agenteResponsavel, detencaoDto.numeroBO);
          //  JOptionPane.showMessageDialog(null, "NUMERO DE BO ENCONTRADO");  
            detencao.setIdDetencao(detencao.gerarIDDetencao(ficheiro.proximoIDDetencao()));
            
            Ocorrencia associadaOcorrencia = ficheiro.procurarBO(detencaoDto.numeroBO);
            if (associadaOcorrencia != null) {
                String nomeDoDetido = associadaOcorrencia.procurarSuspeito();
                detencao.setNomeDoDetido(nomeDoDetido != null ? nomeDoDetido : "NAO IDENTIFICADO");
                associadaOcorrencia.iniciarDetencao(detencao);
            }
            viewDetencao.setLblID(detencao.getIdDetencao());
            viewDetencao.setLblHora(detencao.getDataDeDetencao());
            viewDetencao.setLblLimite(detencao.getLimiteLegal());


            ficheiro.adicionarDetencao(detencao);
            ficheiro.saveDetencao(ficheiro.getDetencoes());
            JOptionPane.showMessageDialog(null, "DETENCAO REGISTADA COM SUCESSO");  

        }
    }
    public void listarDetencoes() {
    try {
        ficheiro.readDetencao();
        viewDetencao.preencherTabela(ficheiro.getDetencoes());
        viewDetencao.mostrarAlertas(ficheiro.getDetencoes());
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Erro ao carregar detencoes: " + ex.getMessage());
    }
}

    public void pesquisarDetencao() {
        String id = viewDetencao.getCampoPesquisa();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Insira um ID para pesquisar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!id.matches("\\d+/\\d{4}")){
            JOptionPane.showMessageDialog(null, "Formato invalido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        ficheiro.readDetencao(); 
        Detencao encontrada = ficheiro.procurarDetencao(id);
        viewDetencao.mostrarResultadoPesquisa(encontrada);
    }
    public void listarOcorrencias() {
      try {
          ficheiro.readOcorrencia();
          viewOcorrencias.preencherTabelaOcorrencias(ficheiro.getOcorrencias());
      } catch (Exception ex) {
          JOptionPane.showMessageDialog(null, "Erro ao carregar ocorrencias: " + ex.getMessage());
      }
  }
}
