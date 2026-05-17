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
import java.util.ArrayList;
import javax.swing.JOptionPane;
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
    private Ocorrencia ocorrenciaEmCurso;
    private int contadorSuspeito = 0;
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
            contadorSuspeito = carregarMaiorIDSuspeito();
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
        Detencao.StatusDetencao status;
        if (desfecho.startsWith("TRANSFERIDO")){
            status = Detencao.StatusDetencao.PARA_PROCESSO;
        } else {
            status = Detencao.StatusDetencao.LIBERTADO;
        }
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

            detencao.encerrarDetencao(status, desfecho);
            ficheiro.saveDetencao(ficheiro.getDetencoes());
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String horaEncerramento = LocalDateTime.now().format(fmt);
            JOptionPane.showMessageDialog(null,"DETENCAO ENCERRADA! ID: " + viewDetencao.getIDDetencao() +"DESFECHO: " + desfecho + "ENCERRADA EM: " + horaEncerramento);
            viewDetencao.limparCamposDetencao();
         }
    }
    
    //todas as configuracoes dos botoes estao nesse metodos
    public void eventosOcorrencia ( TelaOcorrencia telaOcorrencia){
        telaOcorrencia.getBtnLimpar().addActionListener (e -> {
           limparCampos ();  
           ocorrenciaEmCurso = null;
        });
        telaOcorrencia.getBtnAdicionarCivil().addActionListener(e -> {
            adicionarCivil();
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
    private boolean biJaExisteNoBD(String idBI) {
        for (Ocorrencia oc : ficheiro.getOcorrencias()) {
            for (Civil c : oc.getEnvolvidos()) {
                if (c.getBI().equals(idBI)) {
                    return true;
                }
            }
        }
        return false;
    }
  // METODO USADO PARA REGISTAR OCORRENCIA ATRAVES DO CONTROLLER, PONTE ENTRE A VIEW TELA OCORRENCIA E MODEL OCORRENCIA 
    public void registarOcorrencia(){
       //verificar se tem ocorrencia em curso
       if (ocorrenciaEmCurso == null){
            OcorrenciaDto ocorrenciaDto = viewOcorrencias.recolherDadosOcorrencia();
            if (ocorrenciaDto.localOcorrencia.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "INSIRA O LOCAL DA OCORRENCIA.", "AVISO", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (ocorrenciaDto.nomeOficial.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "INSIRA O NOME DO OFICIAL.", "AVISO", JOptionPane.WARNING_MESSAGE);
                return;
            }  
            ocorrenciaEmCurso = new Ocorrencia (ocorrenciaDto.localOcorrencia, ocorrenciaDto.nomeOficial);
            ocorrenciaEmCurso.setNumeroBO((ocorrenciaEmCurso.gerarIDBO(ficheiro.proximoIDBO())));
        } 
        // e preciso verificar se tem pelo menus um envolvido envolvido
        if (ocorrenciaEmCurso.getEnvolvidos().isEmpty()) {
            JOptionPane.showMessageDialog(null, "ADICIONE PELO MENOS UM ENVOLVIDO.", "AVISO", JOptionPane.WARNING_MESSAGE);
            return;
        }
        CrimeDto crimeDto = viewOcorrencias.recolherDadosCrime();
        if (crimeDto.dataDoCrime == null){
            JOptionPane.showMessageDialog(null, "SELECCIONE A DATA DO CRIME.", "AVISO", JOptionPane.WARNING_MESSAGE);
            return;  
        }
        viewOcorrencias.setLblBO(ocorrenciaEmCurso.getNumeroBO());
        viewOcorrencias.setLblData(ocorrenciaEmCurso.getDataHora());
        
        ocorrenciaEmCurso.getCrimes().add(new Crime (crimeDto.localDoCrime, crimeDto.dataDoCrime, crimeDto.armaDoCrime,crimeDto.estadoDeFlagrancia, crimeDto.tipoDeCrime));
        
        ficheiro.adicionarOcorrencia(ocorrenciaEmCurso);
        ficheiro.saveOcorrencia(ficheiro.getOcorrencias()); 
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
              // oc.alterarEstado(Ocorrencia.EstadoBO.DETENCAO_ASSOCIADA);
             //  ficheiro.saveOcorrencia(ficheiro.getOcorrencias());  
               break;
            }
        }
        if (!existe)
            JOptionPane.showMessageDialog(null, "NUMERO DE BO NAO ENCONTRADO");
        else { 

            

            Ocorrencia associadaOcorrencia = ficheiro.procurarBO(detencaoDto.numeroBO);

            if (associadaOcorrencia != null) {
                ArrayList <Civil> suspeitos = new ArrayList<>();
                for (Civil civil: associadaOcorrencia.getEnvolvidos()){
                    if (civil.getPapel() == papelCivil.SUSPEITO){
                        suspeitos.add(civil);
                    }

                }
                if (suspeitos.isEmpty()){
                    JOptionPane.showMessageDialog(null, "ESTA OCORRENCIA NAO TEM SUSPEITOS. NAO PODE REALIZAR UMA DETENCAO", "AVISO", JOptionPane.WARNING_MESSAGE); 
                    return;     
                }
                String [] opcoes = new String [suspeitos.size()];
                for (int i = 0; i< suspeitos.size(); i++){
                    opcoes[i] = suspeitos.get(i).getIdSuspeito() + "-"+ suspeitos.get(i).getNome();

                }
                String escolha = (String) JOptionPane.showInputDialog(null, "SELECIONE O SUSPEITO:", "SUSPEITOS", JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
                if (escolha == null) return;
                
                Civil suspeitoEscolhido = null;
                for (Civil civil: suspeitos){
                    if (escolha.startsWith(civil.getIdSuspeito())){
                        suspeitoEscolhido = civil;
                        break;
                    }
                }
                
                boolean detido = false;
                ficheiro.readDetencao();
                for (Detencao detencoes : ficheiro.getDetencoes()){
                    if (detencoes.getIdSuspeito() != null && detencoes.getIdSuspeito().equals(suspeitoEscolhido.getIdSuspeito()) && detencoes.getStatus()== Detencao.StatusDetencao.ATIVA){
                        detido = true;
                        break;
                    }
                }
                
                if (detido){
                    JOptionPane.showMessageDialog(null,"ESTE SUSPEITO JA TEM UMA DETENCAO ACTIVA.", "AVISO", JOptionPane.WARNING_MESSAGE);
                    return;  
                }
                
                detencao = new Detencao (detencaoDto.agenteResponsavel, detencaoDto.numeroBO);
                detencao.setIdDetencao(detencao.gerarIDDetencao(ficheiro.proximoIDDetencao()));
                detencao.setNomeDoDetido(suspeitoEscolhido.getNome());
                detencao.setIdSuspeito(suspeitoEscolhido.getIdSuspeito());
                associadaOcorrencia.iniciarDetencao(detencao);
                
                associadaOcorrencia.alterarEstado(Ocorrencia.EstadoBO.DETENCAO_ASSOCIADA);
                ficheiro.saveOcorrencia(ficheiro.getOcorrencias());
                viewDetencao.setLblID(detencao.getIdDetencao());
                viewDetencao.setLblHora(detencao.getDataDeDetencao());
                viewDetencao.setLblLimite(detencao.getLimiteLegal());


                ficheiro.adicionarDetencao(detencao);
                ficheiro.saveDetencao(ficheiro.getDetencoes());
                JOptionPane.showMessageDialog(null, "DETENCAO REGISTADA COM SUCESSO");  
            }
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
    
    public int carregarMaiorIDSuspeito (){
           try {

            ficheiro.readOcorrencia();

        } catch (ClassNotFoundException e) {

            e.printStackTrace();

        }

        int maior = 0;

        for(Ocorrencia oc : ficheiro.getOcorrencias()){

            for(Civil c : oc.getEnvolvidos()){

                if(c.getIdSuspeito() != null){

                    String texto = c.getIdSuspeito().replace("SUS", "");

                    int numero = Integer.parseInt(texto);

                    if(numero > maior){
                        maior = numero;
                    }
                }
            }
        }

        return maior;
    }
    
    public String gerarIDSuspeito() {
        contadorSuspeito ++;
        return "SUS" + contadorSuspeito ;
    }
    
    //Adicionar civil para melhor a logica de Swing, ao inves de do.. while
    public void adicionarCivil(){
        //primeiro criar uma ocorrencia em curso se nao existir uma...
        if (ocorrenciaEmCurso == null){
            OcorrenciaDto ocorrenciaDto = viewOcorrencias.recolherDadosOcorrencia();
            if (ocorrenciaDto.localOcorrencia.trim().isEmpty()){
              JOptionPane.showMessageDialog(null, "INSIRA O LOCAL DA OCORRENCIA.", "AVISO", JOptionPane.WARNING_MESSAGE);
            return;    
            }
           if (ocorrenciaDto.nomeOficial.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "INSIRA O NOME DO OFICIAL.", "AVISO", JOptionPane.WARNING_MESSAGE);
            return;
        } 
            ocorrenciaEmCurso = new Ocorrencia (ocorrenciaDto.localOcorrencia, ocorrenciaDto.nomeOficial);
            ocorrenciaEmCurso.setNumeroBO(ocorrenciaEmCurso.gerarIDBO(ficheiro.proximoIDBO()));
            
        }
        //dados do civil, necessarias para a ocorrencia
        CivilDto civilDto = viewOcorrencias.recolherDadosCivil();
        String idBI = viewOcorrencias.pedirNumeroBI();
       
        //Validacao do ID do civil
        if (idBI.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "INSIRA O NUMERO DE BI.", "AVISO", JOptionPane.WARNING_MESSAGE);
        return;
        }
        if (idBI.length() < 13 || idBI.length() > 13){
            JOptionPane.showMessageDialog(null, "O NUMERO DE BI DEVE TER 13 CARACTERES.", "AVISO", JOptionPane.WARNING_MESSAGE);
            viewOcorrencias.limparCampoBI();
            return;   
        }
        for (Civil c : ocorrenciaEmCurso.getEnvolvidos()) {
            if (c.getBI().equals(idBI)) {
                JOptionPane.showMessageDialog(null, "BI JA EXISTE.", "AVISO", JOptionPane.WARNING_MESSAGE);
                viewOcorrencias.limparCampoBI();
                return;
             }
        }
        LocalDate dataNascimento = viewOcorrencias.pedirDataNascimento();
        if (dataNascimento == null) {
            JOptionPane.showMessageDialog(null, "SELECCIONE A DATA DE NASCIMENTO.", "AVISO", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (civilDto.papel == papelCivil.SUSPEITO && !Civil.validarAnos(dataNascimento)) {
            JOptionPane.showMessageDialog(null, "SUSPEITO MENOR DE IDADE.", "AVISO", JOptionPane.WARNING_MESSAGE);
            viewOcorrencias.limparCampoDataNasc();
            return;
          }
        
        if (civilDto.papel == papelCivil.TESTEMUNHA && ! Civil.validarIdadeTestemunha(dataNascimento)){
            JOptionPane.showMessageDialog(null, "MENOR DE 5 ANOS NAO PODE SER TESTEMUNHA.", "AVISO", JOptionPane.WARNING_MESSAGE);
            viewOcorrencias.limparCampoDataNasc();
            return; 
        }
        
        String contacto = viewOcorrencias.pedirContacto();
        if (contacto.length() < 9 || contacto.length() > 9){
             JOptionPane.showMessageDialog(null, "O CONTACTO DEVE TER 9 NUMEROS.", "AVISO", JOptionPane.WARNING_MESSAGE);
             viewOcorrencias.limparCampoContacto();
            return;   
        }
        
        //adicionar civil
        civilDto.idBI = idBI;
        civilDto.dataNascimento = dataNascimento.toString();
        Civil novoCivil = new Civil(civilDto.nome, civilDto.idBI, civilDto.contacto, civilDto.papel,civilDto.dataNascimento, civilDto.nacionalidade, civilDto.sexo,civilDto.enderenco, civilDto.estadoCivil);

        if (civilDto.papel == papelCivil.SUSPEITO) {
            novoCivil.setIdSuspeito(gerarIDSuspeito());
        }
        
        ocorrenciaEmCurso.getEnvolvidos().add(novoCivil); 
        
        viewOcorrencias.limparCamposCivil();
        viewOcorrencias.limparCampoBI();
        viewOcorrencias.limparCampoDataNasc();
        JOptionPane.showMessageDialog(null, "ENVOLVIDO ADICIONADO! TOTAL: " + ocorrenciaEmCurso.getEnvolvidos().size(),"SUCESSO", JOptionPane.INFORMATION_MESSAGE);
        
    }
    
}


