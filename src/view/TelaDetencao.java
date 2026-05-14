package view;
import dto.DetencaoDto;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import model.Civil;
import model.Detencao;
import model.Ficheiros;
import model.Ocorrencia;

public class TelaDetencao extends JFrame {
    private Ficheiros ficheiro;
    private JLabel lblAlerta;
    private JButton btnActualizar;
    private JButton btnPesquisar;
    private JTable tabelaDetencoes;
    private DefaultTableModel modeloTabela;
    private JTextField campoPesquisa;   
    private JButton btnRegistar;
    private JButton btnEncerrar;
    private JButton btnLimpar;
    private JTextField campoDetencaoID;
    private JLabel lblID;
    private JLabel lblHora;
    private JLabel lblLimite;
    private JTextField campoNumeroBO;
    private JTextField campoAgente;
    private JTextField campoIDDetencao;
    private JComboBox<String> campoDesfecho;
    private JTextArea  campoObservacoes;

    private static final Color COR_AZUL   = new Color(10, 20, 70);
    private static final Color COR_FUNDO  = new Color(235, 235, 240);
    private static final Color COR_BRANCO = Color.WHITE;
    private static final Color COR_VERDE  = new Color(0, 120, 60);
    private static final Color COR_VERM   = new Color(160, 20, 20);

    public TelaDetencao() {
        ficheiro = new Ficheiros();
        setTitle("PRM : Gestao de Detencoes");
        setSize(680, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(COR_FUNDO);

        principal.add(criarCabecalho(), BorderLayout.NORTH);

        JTabbedPane abas = new JTabbedPane();
        abas.setFont(new Font("SansSerif", Font.BOLD, 13));
        abas.addTab("Registar Detencao",  criarAbaRegistar());
        abas.addTab("Encerrar Detencao",  criarAbaEncerrar());
        abas.addTab ("Listagem", criarAbaListagem());
        principal.add(abas, BorderLayout.CENTER);

        principal.add(criarRodape(), BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        add(principal);
        setVisible(true);
    }

    //  Cabecalho 
    private JPanel criarCabecalho() {
        JPanel cab = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 12));
        cab.setBackground(COR_AZUL);
        JLabel lbl = new JLabel("REGISTROS DE DETENCAO");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        lbl.setForeground(COR_BRANCO);
        cab.add(lbl);
        lblAlerta = new JLabel("");
        lblAlerta.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblAlerta.setForeground(new Color(255, 200, 0));
        cab.add(lblAlerta);
        return cab;
    }
    
    public void mostrarAlertas(ArrayList<Detencao> detencoes) {
        long count = detencoes.stream()
            .filter(d -> d.alertarDeLimite())
            .count();
        if (count > 0) {
            lblAlerta.setText("⚠ " + count + " detencao(oes) a expirar em menos de 30h!");
        } else {
            lblAlerta.setText("");
        }
    }

    private JPanel criarAbaListagem() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(COR_FUNDO);
        p.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

      
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        painelPesquisa.setOpaque(false);
        campoPesquisa = new JTextField(20);
        estilizarCampo(campoPesquisa);
         btnPesquisar = botao("Pesquisar", COR_AZUL);
        btnPesquisar.setPreferredSize(new Dimension(120, 36));
        painelPesquisa.add(new JLabel("ID da Detencao:"));
        painelPesquisa.add(campoPesquisa);
        painelPesquisa.add(btnPesquisar);
        p.add(painelPesquisa, BorderLayout.NORTH);

    
        String[] colunas = {"ID", "Agente", "Detido","Nº BO", "Data", "Limite 48h", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabelaDetencoes = new JTable(modeloTabela);
        tabelaDetencoes.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabelaDetencoes.setRowHeight(24);
        tabelaDetencoes.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabelaDetencoes.getTableHeader().setBackground(COR_AZUL);
        tabelaDetencoes.getTableHeader().setForeground(COR_BRANCO);
        JScrollPane scroll = new JScrollPane(tabelaDetencoes);
        tabelaDetencoes.getColumnModel().getColumn(0).setPreferredWidth(75);
        tabelaDetencoes.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabelaDetencoes.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabelaDetencoes.getColumnModel().getColumn(3).setPreferredWidth(75);
        tabelaDetencoes.getColumnModel().getColumn(4).setPreferredWidth(75);
        tabelaDetencoes.getColumnModel().getColumn(5).setPreferredWidth(75);
        tabelaDetencoes.getColumnModel().getColumn(6).setPreferredWidth(75);

        p.add(scroll, BorderLayout.CENTER);

   
        btnActualizar = botao("Actualizar Lista", COR_VERDE);
        btnActualizar.setName("btnActualizar");
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.setOpaque(false);
        painelBotoes.add(btnActualizar);
        p.add(painelBotoes, BorderLayout.SOUTH);

        btnPesquisar.setName("btnPesquisar");
    
        return p;
    }
    
    private JPanel criarAbaRegistar() {
          try {
                ficheiro.readOcorrencia();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(COR_FUNDO);
        p.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        GridBagConstraints g = new GridBagConstraints();
        g.insets  = new Insets(10, 5, 10, 5);
        g.fill    = GridBagConstraints.HORIZONTAL;
        g.anchor  = GridBagConstraints.WEST;

        campoNumeroBO = new JTextField();
        campoAgente   = new JTextField();

        adicionarCampo(p, g, "Nº do BO Correspondente:", campoNumeroBO, 0);
        adicionarCampo(p, g, "Agente Responsavel:",          campoAgente,   1);

        // Campos gerados automaticamente
        g.gridx = 0; g.gridy = 2; g.weightx = 0;
        p.add(rotulo("ID da Detencao:"), g);
        g.gridx = 1; g.weightx = 1;
        lblID = new JLabel("[Gerado automaticamente]");
        lblID.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblID.setForeground(Color.GRAY);
        p.add(lblID, g);

        g.gridx = 0; g.gridy = 3; g.weightx = 0;
        p.add(rotulo("Hora da Detencao:"), g);
        g.gridx = 1; g.weightx = 1;
        lblHora = new JLabel("[Gerada automaticamente]");
        lblHora.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblHora.setForeground(Color.GRAY);
        p.add(lblHora, g);

        g.gridx = 0; g.gridy = 4; g.weightx = 0;
        p.add(rotulo("Limite das 48 Horas:"), g);
        g.gridx = 1; g.weightx = 1;
        lblLimite = new JLabel("[Calculado automaticamente]");
        lblLimite.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblLimite.setForeground(new Color(180, 0, 0));
        p.add(lblLimite, g);

        // Botao registar
        g.gridx = 0; g.gridy = 5; g.gridwidth = 2;
        g.insets = new Insets(20, 5, 5, 5);
        btnRegistar = botao("Registar Detencao", COR_VERDE);
        btnRegistar.addActionListener((ActionEvent e) -> {
            String numeroBO = campoNumeroBO.getText().trim();
            String agente   = campoAgente.getText().trim();


            if (numeroBO.trim().isEmpty() ||
                    agente.trim().isEmpty()) {
                JOptionPane.showMessageDialog(TelaDetencao.this, "Preencha todos os campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            } 
          
                Ocorrencia ocorrencia = ficheiro.procurarBO(numeroBO);
                
                if (!ocorrencia.temSuspeito()){
                 JOptionPane.showMessageDialog(
                    TelaDetencao.this,"Esta ocorrência não possui suspeitos.");
                    return;
    
                }
                String idSuspeito = JOptionPane.showInputDialog (TelaDetencao.this, "DIGITE O ID DO SUSPEITO: ");
                if (idSuspeito == null || idSuspeito.trim().isEmpty()){
                      JOptionPane.showMessageDialog(TelaDetencao.this,"ID DO SUSPEITO E OBRIGATORIO.");
                      return;

                }
               Civil suspeito = ocorrencia.procurarSuspeitoPorId(idSuspeito);
               if (suspeito == null){
                 JOptionPane.showMessageDialog(
                    TelaDetencao.this,"SUSPEITO NAO ENCONTRADO.");
                    return;
               }
               
                dadosDetencao();
            
        });
        p.add(btnRegistar, g);

        return p;
    }

    // encerrar detencao e desfeicho
    private JPanel criarAbaEncerrar() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(COR_FUNDO);
        p.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints g = new GridBagConstraints();
        g.insets  = new Insets(8, 5, 8, 5);
        g.fill    = GridBagConstraints.HORIZONTAL;
        g.anchor  = GridBagConstraints.WEST;

        // ID da detencao
        campoIDDetencao = new JTextField();
        adicionarCampo(p, g, "ID da Detencao:", campoIDDetencao, 0);

        // Separador
        g.gridx = 0; g.gridy = 1; g.gridwidth = 2;
        g.insets = new Insets(10, 0, 5, 0);
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(180, 180, 200));
        p.add(sep, g);
        g.gridwidth = 1;
        g.insets = new Insets(8, 5, 8, 5);

        // Label seccao desfecho
        g.gridx = 0; g.gridy = 2; g.gridwidth = 2;
        JLabel lblSeccao = new JLabel("DESFECHO DA DETENCAO");
        lblSeccao.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblSeccao.setForeground(COR_AZUL);
        lblSeccao.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        p.add(lblSeccao, g);
        g.gridwidth = 1;

        // ComboBox de desfecho
        String[] desfechos = {
            "Seleccionar desfecho...",
            "LIBERTADO POR : Falta de Provas",
            "LIBERTADO POR : Caucao Paga",
            "LIBERTADO POR : Ordem do Ministerio Publico",
            "LIBERTADO POR : Prazo de 48 Horas Expirado",
            "TRANSFERIDO PARA : Prisao Preventiva",
            "TRANSFERIDO PARA : Outro Estabelecimento"
        };
        campoDesfecho = new JComboBox<>(desfechos);
        campoDesfecho.setFont(new Font("SansSerif", Font.PLAIN, 13));
        campoDesfecho.setBackground(COR_BRANCO);
        campoDesfecho.setPreferredSize(new Dimension(300, 36));

        // Muda a cor do ComboBox conforme o desfecho
        campoDesfecho.addActionListener(e -> {
            String sel = (String) campoDesfecho.getSelectedItem();
            if (sel != null && sel.startsWith("LIBERTADO")) {
                campoDesfecho.setForeground(COR_VERDE);
            } else if (sel != null && sel.startsWith("TRANSFERIDO")) {
                campoDesfecho.setForeground(COR_VERM);
            } else {
                campoDesfecho.setForeground(Color.BLACK);
            }
        });

        adicionarCampo(p, g, "Desfecho:", campoDesfecho, 3);

        
        g.gridx = 0; g.gridy = 4; g.weightx = 0; g.gridwidth = 1;
        p.add(rotulo("Observacoes:"), g);

        campoObservacoes = new JTextArea(4, 20);
        campoObservacoes.setFont(new Font("SansSerif", Font.PLAIN, 13));
        campoObservacoes.setLineWrap(true);
        campoObservacoes.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(campoObservacoes);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(180, 190, 220)));
        g.gridx = 1; g.weightx = 1; g.gridwidth = 1;
        p.add(scroll, g);

        
        g.gridx = 0; g.gridy = 5; g.gridwidth = 2;
        g.insets = new Insets(18, 5, 5, 5);
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        painelBotoes.setOpaque(false);

        btnEncerrar = botao("Encerrar Detencao", COR_VERM);
        btnLimpar   = botao("Limpar",            new Color(100, 100, 110));


        painelBotoes.add(btnEncerrar);
        painelBotoes.add(btnLimpar);
        p.add(painelBotoes, g);

        return p;
    }


    private JPanel criarRodape() {
        JPanel rod = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        rod.setBackground(COR_FUNDO);
        rod.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
            new Color(200, 200, 210)));
        JButton btnFechar = botao("Fechar", new Color(80, 80, 90));
        btnFechar.addActionListener(e -> dispose());
        rod.add(btnFechar);
        return rod;
    }

    private void adicionarCampo(JPanel p, GridBagConstraints g,
        String label, JComponent campo, int linha) {
            g.gridx = 0; g.gridy = linha; g.weightx = 0; g.gridwidth = 1;
            p.add(rotulo(label), g);
            g.gridx = 1; g.weightx = 1;
            if (campo instanceof JTextField jTextField) estilizarCampo(jTextField);
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

    private JButton botao(String texto, Color cor) {
        JButton b = new JButton(texto);
        b.setBackground(cor);
        b.setForeground(COR_BRANCO);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(200, 38));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
    
    public DetencaoDto dadosDetencao (){
        return new DetencaoDto (campoAgente.getText(), campoNumeroBO.getText());
    }


    public JButton getBtnRegistar() {
        return btnRegistar;
    }

    public void setLblID(String IDdetencao) {
        this.lblID.setText(String.valueOf(IDdetencao)); 
    }

    public void setLblHora(LocalDateTime horaDetencao) {
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"); 
        this.lblHora. setText(horaDetencao.format(formatter)); 
    }

    public void setLblLimite(LocalDateTime limiteDetencao) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
       this.lblLimite.setText(limiteDetencao.format(formatter));
    }

    public JButton getBtnEncerrar() {
        return btnEncerrar;
    }

    public JButton getBtnLimpar() {
        return btnLimpar;
    }

    public String getIDDetencao (){
        return campoIDDetencao.getText();
    }
    public String getDesfecho (){
      return (String) campoDesfecho.getSelectedItem();
    }
    
    public int getIndexDesfecho (){
        return campoDesfecho.getSelectedIndex();
    }
    
    public void limparCamposDetencao (){
        campoIDDetencao.setText("");
        campoDesfecho.setSelectedIndex(0);
        campoObservacoes.setText("");
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnPesquisar() {
        return btnPesquisar;
    }
   
    public String getCampoPesquisa() {
        return campoPesquisa.getText().trim();
    }

    public void preencherTabela(ArrayList<Detencao> detencoes) {
        modeloTabela.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Detencao d : detencoes) {
            modeloTabela.addRow(new Object[]{
                d.getIdDetencao(),
                d.getAgenteResponsavel(),
                d.getNomeDoDetido() != null ? d.getNomeDoDetido() : "-",
                d.getNumeroBO(),
                d.getDataDeDetencao() != null ? d.getDataDeDetencao().format(fmt) : "-",
                d.getLimiteLegal() != null ? d.getLimiteLegal().format(fmt) : "-",
                d.getStatus() != null ? d.getStatus().toString(): "-"
            });
        }
    }

    public void mostrarResultadoPesquisa(Detencao d) {
        modeloTabela.setRowCount(0);
        if (d == null) {
            JOptionPane.showMessageDialog(this, "Detencao nao encontrada.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        modeloTabela.addRow(new Object[]{
            d.getIdDetencao(),
            d.getAgenteResponsavel(),
            d.getNomeDoDetido() != null ? d.getNomeDoDetido() : "-", 
            d.getNumeroBO(),
            d.getDataDeDetencao() != null ? d.getDataDeDetencao().format(fmt) : "-",
            d.getLimiteLegal() != null ? d.getLimiteLegal().format(fmt) : "-",
            d.getStatus() != null ? d.getStatus().toString(): "-"
        });
    }
}
