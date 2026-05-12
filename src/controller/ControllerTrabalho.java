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
import model.Civil.papelCivil;
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
       viewMenu.setEntradaSistema(() ->{
           configurarEventos();
       });
    

    }
    public void configurarEventos (){
        try{
            ficheiro.readOcorrencia();
            ficheiro.readDetencao();
        } catch (Exception ex){
            ex.printStackTrace();
        }
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
            try {
                encerrarDetencao ();
            } catch (FileNotFoundException ex) {
                System.getLogger(ControllerTrabalho.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
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
    
    public void encerrarDetencao () throws FileNotFoundException{
        if (viewDetencao.getIDDetencao().isEmpty()){
              JOptionPane.showMessageDialog(null,
                "INSIRA O ID DA DETENCAO.", "AVISO",
                JOptionPane.WARNING_MESSAGE);
              return;
        }
        if (viewDetencao.getIndexDesfecho ()==0){
            JOptionPane.showMessageDialog(null,
                "SELECCIONE O DESFECHO DA DETENCAO.", "AVISO",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        String desfecho = viewDetencao.getDesfecho();
        int conf = JOptionPane.showConfirmDialog(null,
        "CONFIRMA ENCERRAMENTO? ID: " + viewDetencao.getIDDetencao() + "DESFECHO: " + desfecho,
        "CONFIRMAR ENCERRAMENTO", JOptionPane.YES_NO_OPTION);
         
         if (conf == JOptionPane.YES_OPTION){
            ficheiro.readDetencao();
            Detencao detencao = ficheiro.procurarDetencao(viewDetencao.getIDDetencao());
            if (detencao == null){
                JOptionPane.showMessageDialog(null, "ID DE DETENCAO NAO ENCONTRADO.", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            detencao.encerrarDetencao(Detencao.StatusDetencao.LIBERTADO, viewDetencao.getDesfecho());
            ficheiro.saveDetencao(ficheiro.getDetencoes());
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String horaEncerramento = LocalDateTime.now().format(fmt);
            JOptionPane.showMessageDialog(null,"DETENCAO ENCERRADA! ID: " + viewDetencao.getIDDetencao() +"DESFECHO: " + desfecho + "ENCERRADA EM: " + horaEncerramento);
            viewDetencao.limparCamposDetencao();
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
        
        telaOcorrencia.getBtnFechar().addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(null, "DESEJA SAIR?", "SAIR", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                telaOcorrencia.dispose();
        });
    }
    public void limparCampos (){
       viewOcorrencias.limparCamposOcorrencia();
       viewOcorrencias.limparCamposCivil();
       viewOcorrencias.limparCamposCrime();      
    }
    public void registarOcorrencia(){
        OcorrenciaDto ocorrenciaDto = viewOcorrencias.recolherDadosOcorrencia();
          if (ocorrenciaDto.localOcorrencia.trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "INSIRA O LOCAL DA OCORRENCIA", "AVISO", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (ocorrenciaDto.nomeOficial.trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "INSIRA O NOME DO OFICIAL.", "AVISO", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Ocorrencia ocorrencia = new Ocorrencia(ocorrenciaDto.localOcorrencia, ocorrenciaDto.nomeOficial);
        ocorrencia.setNumeroBO(ocorrencia.gerarIDBO(ficheiro.proximoIDBO()));
       
        int resposta = 0;
        do{
            Civil civil = new Civil ();
            CivilDto civilDto = viewOcorrencias.recolherDadosCivil();
          
            boolean existe = false;
            String idBI = viewOcorrencias.pedirNumeroBI();

            if (idBI.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "INSIRA O NUMERO DE  BI.", "AVISO", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean biDuplicado = false;
            for (Civil c : ocorrencia.getEnvolvidos()) {
                if (c.getBI().equals(idBI)) {
                    biDuplicado = true;
                    break;
                }
            }

            if (biDuplicado) {
                JOptionPane.showMessageDialog(null, "BI JA EXISTE.", "AVISO", JOptionPane.WARNING_MESSAGE);
                viewOcorrencias.limparCampoBI();
                return;
            }


              civilDto.idBI= idBI;
           
            existe= false;
         
             LocalDate dataNascimento = viewOcorrencias.pedirDataNascimento();

            if (dataNascimento == null) {
                JOptionPane.showMessageDialog(null, 
                    "SELECCIONE A DATA DE NASCIMENTO", "AVISO", 
                    JOptionPane.WARNING_MESSAGE);
                return; // utilizador corrige e clica Guardar novamente
            }

       
            if (civilDto.papel == papelCivil.SUSPEITO && !Civil.validarAnos(dataNascimento)) {
                JOptionPane.showMessageDialog(null, 
                    "SUSPEITO MENOR DE IDADE. NAO PODE SER REGISTADO COMO SUSPEITO", 
                    "AVISO", JOptionPane.WARNING_MESSAGE);
                viewOcorrencias.limparCampoDataNasc();
                return;
            }
            civilDto.dataNascimento = dataNascimento.toString();
            ocorrencia.getEnvolvidos().add(new Civil (civilDto.nome, civilDto.idBI, civilDto.contacto, civilDto.papel, civilDto.dataNascimento, civilDto.nacionalidade, civilDto.sexo, civilDto.enderenco, civilDto.estadoCivil));
            
            viewOcorrencias.limparCamposCivil();
            viewOcorrencias.limparCampoBI();
            viewOcorrencias.limparCampoDataNasc();
            
            resposta = viewOcorrencias.perguntarDecisao();
            if (resposta == JOptionPane.NO_OPTION) {
            boolean temSuspeito = false;
            for (Civil c : ocorrencia.getEnvolvidos()) {
                if (c.getPapel() == papelCivil.SUSPEITO) {
                    temSuspeito = true;
                    break;
                }
            }
            if (!temSuspeito) {
                int resposta2 = JOptionPane.showConfirmDialog(null,
                    "NENHUM SUSPEITO REGISTADO. TEM ALGUM SUSPEITO PARA REGISTAR?",
                    "AVISO", JOptionPane.YES_NO_OPTION);
                if (resposta2 == JOptionPane.YES_OPTION) {
                    resposta = JOptionPane.YES_OPTION; 
                }
            }
        }
            
        }while (resposta== JOptionPane.YES_OPTION);
        
        viewOcorrencias.setLblBO(ocorrencia.getNumeroBO());
        viewOcorrencias.setLblData(  ocorrencia.getDataHora());
     /*   ArrayList <String> suspeitos = new ArrayList<>();
        for (Civil civil: ocorrencia.getEnvolvidos()){
            if (civil.getPapel()== papelCivil.SUSPEITO){
                suspeitos.add(civil.getNome()+ " - "+ civil.getBI());
            }
        }
        viewOcorrencias.preencherAutoresCrime(suspeitos);
        */
        CrimeDto crimeDto = viewOcorrencias.recolherDadosCrime();
       /* if (viewOcorrencias.getIndexAutorCrime() == 0){
            JOptionPane.showMessageDialog(null, "SELECCIONE O AUTOR DO CRIME.", "AVISO", JOptionPane.WARNING_MESSAGE
            );
            return;
        }*/
        if (crimeDto.dataDoCrime == null){
            JOptionPane.showMessageDialog(null, "SELECCIONE A DATA DO CRIME.", "AVISO", JOptionPane.WARNING_MESSAGE);
            return; 
        }
        ocorrencia.getCrimes().add(new Crime (crimeDto.localDoCrime, crimeDto.dataDoCrime, crimeDto.armaDoCrime, crimeDto.estadoDeFlagrancia, crimeDto.tipoDeCrime));
        ficheiro.adicionarOcorrencia(ocorrencia);
        ficheiro.saveOcorrencia(ficheiro.getOcorrencias());  
        viewOcorrencias.mostrarMensagem("OCORRENCIA REGISTADA COM SUCESSO");
        
    }
    
    
    
    public void cadastrarDetencao () throws ClassNotFoundException, FileNotFoundException{
         DetencaoDto detencaoDto = new DetencaoDto ();
          Detencao detencao = new Detencao() ;
     
        boolean existe = false;
   
        detencaoDto = viewDetencao.dadosDetencao();
     //   Detencao detencao = new Detencao (detencaoDto.agenteResponsavel);
        if (detencaoDto.agenteResponsavel.trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "INSIRA O NOME DO AGENTE RESPONSAVEL.", "AVISO", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (detencaoDto.numeroBO.trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "INSIRA O NUMERO DE BO.", "AVISO", JOptionPane.WARNING_MESSAGE);
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
            detencao = new Detencao (detencaoDto.agenteResponsavel, detencaoDto.numeroBO);
            Ocorrencia associadaOcorrencia = ficheiro.procurarBO(detencaoDto.numeroBO);

            if (associadaOcorrencia != null) {
                String nomeDoDetido = associadaOcorrencia.procurarSuspeito();

          // Verificar se tem suspeito
                if (nomeDoDetido == null) {
                    JOptionPane.showMessageDialog(null,"Esta ocorrencia nao tem suspeito registado.\nNao e possivel efectuar uma detencao.","Aviso", JOptionPane.WARNING_MESSAGE);
              return;
          }

          detencao.setNomeDoDetido(nomeDoDetido);
          associadaOcorrencia.iniciarDetencao(detencao);
      }
            detencao.setIdDetencao(detencao.gerarIDDetencao(ficheiro.proximoIDDetencao()));
            
       
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
        JOptionPane.showMessageDialog(null, "ERRO AO CARREGAR DETENCOES: " + ex.getMessage());
    }
}

    public void pesquisarDetencao() {
        String id = viewDetencao.getCampoPesquisa();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(null, "INSIRA UM ID PARA PESQUISAR.", "AVISO", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!id.matches("\\d+/\\d{4}")){
            JOptionPane.showMessageDialog(null, "FORMATO INVALIDO.", "AVISO", JOptionPane.WARNING_MESSAGE);
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
          JOptionPane.showMessageDialog(null, "ERRO AO CARREGAR OCORRENCIAS: " + ex.getMessage());
      }
  }
}