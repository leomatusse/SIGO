/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author Leo
 */


import com.toedter.calendar.JDateChooser;
import dto.CivilDto;
import dto.CrimeDto;
import dto.OcorrenciaDto;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import model.Civil.papelCivil;
import model.Crime.EstadoFlagrancia;
import model.Ocorrencia;

public class TelaOcorrencia extends JFrame {
    private JTable tabelaOcorrencias;
    private DefaultTableModel modeloTabelaOcorrencia;
    private JButton btnActualizarOcorrencias;
     private JButton btnLimpar;
     private JButton btnGuardar;
     private JButton btnFechar;
     private JLabel lblBO, lblData; 

    // Campos
    private JTextField campoLocal;
    private JTextField campoOficial;
    private JTextField campoNomeCivil;
    private JTextField campoContacto;
    private JTextField campoBI;
    private JDateChooser campoDataNasc;
    private JTextField campoNacionalidade;
    private JTextField campoEndereco;
    private JTextField campoEstadoCivil;
    private JComboBox<papelCivil> campoPapel = new JComboBox <> (papelCivil.values());
    private JComboBox<String> campoSexo;

    // Campos crime
    private JTextField campoLocalCrime;
    private JDateChooser campoDataCrime;
    private JTextField campoArmaCrime;
    private JTextField campoTipoCrime;
    private JComboBox<EstadoFlagrancia> campoFlagrancia = new JComboBox <>(EstadoFlagrancia.values());
   // private JComboBox<String> campoAutorCrime;
    // Cores
    private static final Color COR_AZUL    = new Color(10, 20, 70);
    private static final Color COR_FUNDO   = new Color(235, 235, 240);
    private static final Color COR_BRANCO  = Color.WHITE;
    private static final Color COR_VERDE   = new Color(0, 120, 60);
    private static final Color COR_VERM    = new Color(160, 20, 20);

    public TelaOcorrencia() {
        setTitle(" Registro de  Ocorrencias");
        setSize(750, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(COR_FUNDO);

        // Cabecalho
        painelPrincipal.add(criarCabecalho("REGISTAR OCORRENCIA"), BorderLayout.NORTH);

        // Separador em abas
        JTabbedPane abas = new JTabbedPane();
        abas.setFont(new Font("SansSerif", Font.BOLD, 13));
        abas.addTab("Ocorrencia", criarAbaOcorrencia());
        abas.addTab("Civil Envolvido", criarAbaCivil());
        abas.addTab("Crime", criarAbaCrime());
        abas.addTab("Lista", criarAbaLista());
        painelPrincipal.add(abas, BorderLayout.CENTER);

        // Botoes em baixo
        painelPrincipal.add(criarRodape(), BorderLayout.SOUTH);

        add(painelPrincipal);
        setVisible(true);
        pack();
        setLocationRelativeTo(null); 
    }

    // Cabecalho
    private JPanel criarCabecalho(String texto) {
        JPanel cab = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 12));
        cab.setBackground(COR_AZUL);
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        lbl.setForeground(COR_BRANCO);
        cab.add(lbl);
        return cab;
    }
    private JPanel criarAbaLista() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(COR_FUNDO);
        p.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        String[] colunas = {"Nº BO", "Local", "Oficial", "Data/Hora", "Estado"};
        modeloTabelaOcorrencia = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaOcorrencias = new JTable(modeloTabelaOcorrencia);
        tabelaOcorrencias.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabelaOcorrencias.setRowHeight(24);
        tabelaOcorrencias.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabelaOcorrencias.getTableHeader().setBackground(COR_AZUL);
        tabelaOcorrencias.getTableHeader().setForeground(COR_BRANCO);
        p.add(new JScrollPane(tabelaOcorrencias), BorderLayout.CENTER);

        btnActualizarOcorrencias = botao("Actualizar Lista", COR_VERDE);
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.setOpaque(false);
        painelBotoes.add(btnActualizarOcorrencias);
        p.add(painelBotoes, BorderLayout.SOUTH);

        return p;
    }
    
    private JPanel criarAbaOcorrencia() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(COR_FUNDO);
        p.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 5, 8, 5);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        campoLocal   = new JTextField();
        campoOficial = new JTextField();

        adicionarCampo(p, g, "Local da Ocorrencia:", campoLocal,   0);
        adicionarCampo(p, g, "Nome do Oficial:",      campoOficial, 1);

      
        g.gridx = 0; g.gridy = 2; g.weightx = 0;
        p.add(rotulo("Nº do BO:"), g);
        g.gridx = 1; g.weightx = 1;
        lblBO = new JLabel("[Gerado automaticamente]");
        lblBO.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblBO.setForeground(Color.GRAY);
        p.add(lblBO, g);

        g.gridx = 0; g.gridy = 3; g.weightx = 0;
        p.add(rotulo("Data e Hora:"), g);
        g.gridx = 1; g.weightx = 1;
        lblData = new JLabel("[Gerada Automaticamente]");
        lblData.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblData.setForeground(Color.GRAY);
        p.add(lblData, g);

        return p;
    }

 
    private JPanel criarAbaCivil() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(COR_FUNDO);
        p.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 5, 6, 5);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        campoNomeCivil    = new JTextField();
        campoContacto     = new JTextField();
        campoBI           = new JTextField();
        campoDataNasc     = new JDateChooser();
        campoDataNasc.setDateFormatString("dd/MM/yyyy");
        campoDataNasc.setPreferredSize(new Dimension (400, 36));
        campoDataNasc.setBackground(COR_BRANCO);
        campoDataNasc.getCalendarButton().setBackground(COR_AZUL);
        campoDataNasc.getCalendarButton().setForeground(COR_BRANCO);
        campoDataNasc.getCalendarButton().setText("CALENDARIO");
        campoDataNasc.getDateEditor().getUiComponent().setFont(new Font("SansSerif", Font.PLAIN, 13));
        campoDataNasc.setMaxSelectableDate(new Date());
        campoNacionalidade= new JTextField();
        campoEndereco     = new JTextField();
        campoEstadoCivil  = new JTextField();

    
        campoPapel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        campoSexo = new JComboBox<>(new String[]{
            "MASCULINO", "FEMININO"});
        campoSexo.setFont(new Font("SansSerif", Font.PLAIN, 13));

        adicionarCampo(p, g, "Nome do Envolvido:",   campoNomeCivil,     0);
        adicionarCampo(p, g, "Contacto:",             campoContacto,      1);
        adicionarCampo(p, g, "Nº do Bilhete Identidade:",campoBI,           2);
        adicionarCampo(p, g, "Data Nascimento (DDMMYYYY):", campoDataNasc, 3);
        adicionarCampo(p, g, "Papel na Ocorrencia:", campoPapel,         4);
        adicionarCampo(p, g, "Nacionalidade:",        campoNacionalidade, 5);
        adicionarCampo(p, g, "Sexo:",                 campoSexo,         6);
        adicionarCampo(p, g, "Endereco:",             campoEndereco,     7);
        adicionarCampo(p, g, "Estado Civil:",         campoEstadoCivil,  8);

        return p;
    }

   
    private JPanel criarAbaCrime() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(COR_FUNDO);
        p.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 5, 8, 5);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        campoLocalCrime = new JTextField();
        campoDataCrime  = new JDateChooser();
        campoDataCrime = new JDateChooser();
        campoDataCrime.setDateFormatString("dd/MM/yyyy");
        campoDataCrime.setPreferredSize(new Dimension(400, 36));
        campoDataCrime.setBackground(COR_BRANCO);
        campoDataCrime.getCalendarButton().setBackground(COR_AZUL);
        campoDataCrime.getCalendarButton().setForeground(COR_BRANCO);
        campoDataCrime.getCalendarButton().setText("CALENDARIO");
        campoDataCrime.getDateEditor().getUiComponent().setFont(new Font("SansSerif", Font.PLAIN, 13));
        campoDataCrime.setMaxSelectableDate(new Date());
        campoArmaCrime  = new JTextField();
        campoTipoCrime  = new JTextField();


        campoFlagrancia.setFont(new Font("SansSerif", Font.PLAIN, 13));
   //   campoAutorCrime = new JComboBox <>();
  //    campoAutorCrime.setFont(new Font("SansSerif", Font.PLAIN, 13));
    //    campoAutorCrime.addItem("Selecionar autor..."); 
        adicionarCampo(p, g, "Local do Crime:",    campoLocalCrime, 0);
        adicionarCampo(p, g, "Data do Crime:",     campoDataCrime,  1);
        adicionarCampo(p, g, "Arma do Crime:",     campoArmaCrime,  2);
        adicionarCampo(p, g, "Tipo de Crime:",     campoTipoCrime,  3);
        adicionarCampo(p, g, "Estado Flagrancia:", campoFlagrancia, 4);
   //     adicionarCampo(p, g, "Autor do Crime:", campoAutorCrime, 5);
        return p;
    }


    public JPanel criarRodape() {
        JPanel rod = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        rod.setBackground(COR_FUNDO);
        rod.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
            new Color(200, 200, 210)));

        btnGuardar  = botao("Guardar",  COR_VERDE);
        btnLimpar   = botao("Limpar",   new Color(100, 100, 110));
        btnFechar   = botao("Fechar",   COR_VERM);

        rod.add(btnGuardar);
        rod.add(btnLimpar);
        rod.add(btnFechar);
        return rod;
    }
    public void limparCampoBI (){
        campoBI.setText("");
    }
    
    public void limparCampoDataNasc (){
        campoDataNasc.setDate(null);
        campoDataNasc.repaint();
        campoDataNasc.revalidate();
    }

    public void limparCamposOcorrencia() {
        campoLocal.setText("");
        campoOficial.setText("");
        
       
    }
    
    public void limparCamposCivil (){
       campoNomeCivil.setText("");
        campoContacto.setText("");
        campoBI.setText("");
        campoDataNasc.setDate(null);
        campoNacionalidade.setText("");
        campoEndereco.setText("");
        campoEstadoCivil.setText(""); 
    }
    
    public void limparCamposCrime (){
         campoLocalCrime.setText("");
        campoDataCrime.setDate(null);
        campoArmaCrime.setText("");
        campoTipoCrime.setText("");
        campoPapel.setSelectedIndex(0);
        campoSexo.setSelectedIndex(0);
        campoFlagrancia.setSelectedIndex(0);
     //   campoAutorCrime.setSelectedIndex(0);
    }

    //  Auxiliares 
    private void adicionarCampo(JPanel p, GridBagConstraints g,
                                 String label, JComponent campo, int linha) {
        g.gridx = 0; g.gridy = linha; g.weightx = 0;
        p.add(rotulo(label), g);
        g.gridx = 1; g.weightx = 1;
        if (campo instanceof JTextField) estilizarCampo((JTextField) campo);
        p.add(campo, g);
    }

    private JLabel rotulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setForeground(new Color(30, 40, 80));
        return l;
    }

    private void estilizarCampo(JTextField t) {
        t.setFont(new Font("SansSerif", Font.PLAIN, 13));
        t.setPreferredSize(new Dimension(300, 36));
        t.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 190, 220), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        t.setBackground(COR_BRANCO);
    }

    public JButton botao(String texto, Color cor) {
        JButton b = new JButton(texto);
        b.setBackground(cor);
        b.setForeground(COR_BRANCO);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(140, 38));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
    public JButton getBtnLimpar() {
     return btnLimpar;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnFechar() {
        return btnFechar;
    }
    
    public OcorrenciaDto recolherDadosOcorrencia (){
        return new OcorrenciaDto (campoLocal.getText(), campoOficial.getText());
    }
    
    public CivilDto recolherDadosCivil(){
        return new CivilDto (campoNomeCivil.getText(), campoContacto.getText(), (papelCivil)campoPapel.getSelectedItem(),campoNacionalidade.getText(), campoSexo.getSelectedItem().toString(), campoEndereco.getText(), campoEstadoCivil.getText());
    }

    public CrimeDto recolherDadosCrime (){
        CrimeDto dto = new CrimeDto (campoLocalCrime.getText(), pedirDataCrime(), campoArmaCrime.getText(),(EstadoFlagrancia) campoFlagrancia.getSelectedItem(), campoTipoCrime.getText());
     //   dto.autorDoCrime = getAutorCrime();
        return dto;
    }
    
    public String pedirNumeroBI (){
        return campoBI.getText();
    }
    
    public LocalDate pedirDataNascimento (){
        Date data = campoDataNasc.getDate ();
        if (data == null) return null;
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    public void mostrarMensagem(String msg) {
     JOptionPane.showMessageDialog(this, msg);
    }
    
    public int perguntarDecisao (){
        return JOptionPane.showConfirmDialog(this, "DESEJA ADICIONAR MAIS ENVOLVIDOS?", "ENVOLVIDOS", JOptionPane.YES_NO_OPTION);
    }
    
    public int fecharDecisao(){
        return JOptionPane.showConfirmDialog(this,"DESEJA SAIR?");
    }

    public void setLblBO(String numeroBO) {
        this.lblBO.setText(numeroBO);
    }

    public void setLblData(LocalDateTime horaData) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        this.lblData.setText(horaData.format(formatter));
    }
    
    public JButton getBtnActualizarOcorrencias() {
    return btnActualizarOcorrencias;
}

    public void preencherTabelaOcorrencias(ArrayList<Ocorrencia> ocorrencias) {
        modeloTabelaOcorrencia.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Ocorrencia oc : ocorrencias) {
            modeloTabelaOcorrencia.addRow(new Object[]{
                oc.getNumeroBO(),
                oc.getLocalOcorrencia(),
                oc.getNomeOficial(),
                oc.getDataHora() != null ? oc.getDataHora().format(fmt) : "-",
                oc.getEstadoBO()
            });
        }
     }
    public LocalDate pedirDataCrime() {
        Date data = campoDataCrime.getDate();
        if (data == null) return null;
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
   /* public void preencherAutoresCrime (ArrayList <String> suspeitos){
        campoAutorCrime.removeAllItems();
        campoAutorCrime.addItem("Seleccionar autor.");
        for (String suspeito: suspeitos){
            campoAutorCrime.addItem(suspeito);
            
        }
    }
    
   /* public String getAutorCrime (){
        return (String) campoAutorCrime.getSelectedItem();
    }
    
    public int getIndexAutorCrime (){
        return campoAutorCrime.getSelectedIndex();
    }*/
}


