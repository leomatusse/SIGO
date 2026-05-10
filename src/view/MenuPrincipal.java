package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MenuPrincipal extends JFrame {
    private static final Color COR_AZUL_ESCURO = new Color(10, 20, 70);
    private static final Color COR_AZUL_MEDIO  = new Color(20, 60, 150);
    private static final Color COR_DOURADO     = new Color(200, 160, 40);
    private static final Color COR_BRANCO      = Color.WHITE;

    private JLabel labelRelogio;

    public MenuPrincipal() {
        mostrarLogin();
    }
    private Runnable entradaSistema; 
    private JButton btnOcorrencia;
    private JButton btnDetencao;
    private JButton btnSair;
    //  Esquerda: imagem (coloca em imagens/fundo.jpg)
    //  Direita:  cinza claro
    
    private JPanel criarFundo() {
        return new JPanel() {
            // Tenta carregar a imagem
            Image img = carregarImagem();

            private Image carregarImagem() {
                try {
                    java.io.File f = new java.io.File("imagem/fundo.jpg.jpg");
                    if (f.exists()) return new ImageIcon("imagem/fundo.jpg.jpg").getImage();
                    f = new java.io.File("imagem/fundo.png");
                    if (f.exists()) return new ImageIcon("imagem/fundo.png").getImage();
                } catch (Exception e) {}
                return null;
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                                    RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);

                int metade = getWidth() / 2;

               
                if (img != null) {
                    // Imagem real
                    g2.drawImage(img, 0, 0, metade, getHeight(), this);
                } else {
                    // Gradiente como alternativa
                    GradientPaint gp = new GradientPaint(
                        0, 0, new Color(5, 15, 60),
                        metade, getHeight(), new Color(0, 80, 40));
                    g2.setPaint(gp);
                    g2.fillRect(0, 0, metade, getHeight());
                }

                // Camada escura sobre a imagem
                g2.setColor(new Color(0, 0, 0, 90));
                g2.fillRect(0, 0, metade, getHeight());

                
                g2.setColor(new Color(235, 235, 235));
                g2.fillRect(metade, 0, metade, getHeight());

                // Linha dourada em baixo
                g2.setColor(new Color(200, 160, 40, 200));
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawLine(0, getHeight() - 38, getWidth(), getHeight() - 38);

                // Linha divisÃ³ria vertical
             /*   g2.setColor(new Color(200, 160, 40, 150));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawLine(metade, 0, metade, getHeight());*/
            }
        };
    }

    
    private void mostrarLogin() {
        setTitle("PRM : Sistema de Gestao de Ocorrencias na Esquadra");
        setSize(900, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel fundo = criarFundo();
        fundo.setLayout(new GridLayout(1, 2));
        setContentPane(fundo);

        JPanel esquerdo = new JPanel(new GridBagLayout());
        esquerdo.setOpaque(false);

        GridBagConstraints ge = new GridBagConstraints();
        ge.gridx  = 0;
        ge.anchor = GridBagConstraints.CENTER;
        ge.insets = new Insets(6, 0, 6, 0);

        JPanel direito = criarFormularioLogin();

        fundo.add(esquerdo);
        
        
        fundo.add(direito);
         pack();
          setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel criarFormularioLogin() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setOpaque(false);
        painel.setBorder(BorderFactory.createEmptyBorder(40, 45, 40, 45));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx  = 0;
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        
        JLabel titulo = new JLabel("INICIAR SESSAO");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(COR_AZUL_ESCURO);
        g.gridy = 0; g.insets = new Insets(0, 0, 25, 0);
        painel.add(titulo, g);

        
        g.gridy = 1; g.insets = new Insets(5, 0, 3, 0);
        painel.add(rotulo("Utilizador"), g);

        JTextField campoUser = campo();
        g.gridy = 2; g.insets = new Insets(0, 0, 12, 0);
        painel.add(campoUser, g);

       
        g.gridy = 3; g.insets = new Insets(5, 0, 3, 0);
        painel.add(rotulo("Senha"), g);

        JPasswordField campoSenha = new JPasswordField();
        estilizarCampo(campoSenha);
        g.gridy = 4; g.insets = new Insets(0, 0, 8, 0);
        painel.add(campoSenha, g);

        
        JLabel lblErro = new JLabel(" ");
        lblErro.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblErro.setForeground(new Color(180, 0, 0));
        g.gridy = 5; g.insets = new Insets(0, 0, 6, 0);
        painel.add(lblErro, g);

        
        JButton btnEntrar = criarBotaoEntrar();
        g.gridy = 6; g.insets = new Insets(4, 0, 0, 0);
        painel.add(btnEntrar, g);

        JLabel info = new JLabel(
            "<html><center><font color='#888888' size='2'>" +
            "Acesso restrito a agentes autorizados" +
            "</font></center></html>", SwingConstants.CENTER);
        g.gridy = 7; g.insets = new Insets(14, 0, 0, 0);
        painel.add(info, g);

       
        ActionListener acaoLogin;
        acaoLogin = (ActionEvent e) -> {
            String user  = campoUser.getText().trim();
            String senha = new String(campoSenha.getPassword()).trim();
            if (user.isEmpty() || senha.isEmpty()) {
                lblErro.setText("Preencha todos os campos.");
                sacudir();
            } else if (user.equals("admin") && senha.equals("1234")) {
                lblErro.setText(" ");
                abrirMenu();
                
                if (entradaSistema != null){
                    entradaSistema.run();
                }
            } else {
                lblErro.setText("Utilizador ou senha incorrectos.");
                campoSenha.setText("");
                campoUser.setText(" ");
                sacudir();
            }
        };

        btnEntrar.addActionListener(acaoLogin);
        campoSenha.addActionListener(acaoLogin);
        campoUser.addActionListener(ev -> campoSenha.requestFocus());

        return painel;
    }

    public void abrirMenu() {
        getContentPane().removeAll();
        setSize(860, 560);
        setLocationRelativeTo(null);

        JPanel fundo = criarFundo();
        fundo.setLayout(new BorderLayout());
        setContentPane(fundo);

        fundo.add(criarCabecalho(), BorderLayout.NORTH);
        fundo.add(criarAreaMenu(),  BorderLayout.CENTER);
        fundo.add(criarRodape(),    BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private JPanel criarCabecalho() {
        JPanel cab = new JPanel(new BorderLayout());
        cab.setOpaque(false);
        cab.setPreferredSize(new Dimension(0, 75));
        cab.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel esq = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        esq.setOpaque(false);

        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setOpaque(false);
        JLabel t1 = new JLabel("POLICIA DA REPUBLICA DE MOCAMBIQUE");
        t1.setFont(new Font("SansSerif", Font.BOLD, 13));
        t1.setForeground(COR_BRANCO);
        textos.add(t1);
        esq.add(textos);

        labelRelogio = new JLabel();
        labelRelogio.setFont(new Font("Courier New", Font.BOLD, 12));
        labelRelogio.setForeground(new Color(0, 0, 0));
        atualizarRelogio();
        new Timer(1000, e -> atualizarRelogio()).start();

        cab.add(esq,          BorderLayout.WEST);
        cab.add(labelRelogio, BorderLayout.EAST);
        return cab;
    }

    private JPanel criarAreaMenu() {
        JPanel area = new JPanel(new GridLayout(1, 2));
        area.setOpaque(false);

        
        JPanel esquerdo = new JPanel(new GridBagLayout());
        esquerdo.setOpaque(false);

        GridBagConstraints ge = new GridBagConstraints();
        ge.gridx = 0; ge.anchor = GridBagConstraints.CENTER;
        ge.insets = new Insets(6, 0, 6, 0);
        
        JSeparator sep = new JSeparator();
        sep.setPreferredSize(new Dimension(280, 2));
        sep.setForeground(COR_DOURADO);
        ge.gridy = 1; ge.insets = new Insets(4, 0, 10, 0);
        esquerdo.add(sep, ge);

        JPanel direito = new JPanel(new GridBagLayout());
        direito.setOpaque(false);
        direito.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 40));

        GridBagConstraints gd = new GridBagConstraints();
        gd.gridx  = 0;
        gd.fill   = GridBagConstraints.HORIZONTAL;
        gd.insets = new Insets(8, 0, 8, 0);

        gd.gridy = 0;
        Component add;
        btnOcorrencia = criarBotaoMenu("1", "BO");
        direito.add(btnOcorrencia, gd);

        gd.gridy = 1;
        btnDetencao = criarBotaoMenu("2", "Detencao");
        direito.add(btnDetencao, gd);

      /*  gd.gridy = 2;
        direito.add(criarBotaoMenu("3", "Ocorrencia", e ->
            JOptionPane.showMessageDialog(this,
                "Modulo de Ocorrencias", "Ocorrencia",
                JOptionPane.INFORMATION_MESSAGE)), gd);*/

        gd.gridy = 3;
        btnSair =criarBotaoMenu("3", "Sair");
        direito.add(btnSair, gd);

        area.add(esquerdo);
        area.add(direito);
        return area;
    }

    private JPanel criarRodape() {
        JPanel rod = new JPanel(new BorderLayout());
        rod.setOpaque(false);
        rod.setBorder(BorderFactory.createEmptyBorder(6, 20, 10, 20));
        JLabel esq = new JLabel("PRM mz© 2026  |  Todos os direitos reservados");
        esq.setFont(new Font("SansSerif", Font.PLAIN, 11));
        esq.setForeground(new Color(0, 0, 0));
        JLabel dir = new JLabel("Utilizador: Admin");
        dir.setFont(new Font("SansSerif", Font.PLAIN, 11));
        dir.setForeground(new Color(0, 0, 0));
        rod.add(esq, BorderLayout.WEST);
        rod.add(dir, BorderLayout.EAST);
        return rod;
    }

    
    private JButton criarBotaoMenu(String icone, String texto) {
        JButton b = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                Color cor = getModel().isRollover()
                    ? new Color(150, 150, 150, 230)
                    : new Color(180, 180, 180, 210);
                g2.setColor(cor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(255, 255, 255, 80));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                super.paintComponent(g);
            }
        };

        b.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(280, 60));

        JLabel lblIcone = new JLabel(icone);
        lblIcone.setFont(new Font("SansSerif", Font.PLAIN, 24));

        JLabel lblTexto = new JLabel(texto);
        lblTexto.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTexto.setForeground(COR_AZUL_ESCURO);

        b.add(lblIcone);
        b.add(lblTexto);
      //  b.addActionListener(acao);

        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { b.repaint(); }
            @Override
            public void mouseExited(MouseEvent e)  { b.repaint(); }
        });

        return b;
    }

    
    private JButton criarBotaoEntrar() {
        JButton b = new JButton("ENTRAR") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? COR_AZUL_MEDIO : COR_AZUL_ESCURO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setForeground(COR_BRANCO);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(280, 44));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { b.repaint(); }
            @Override
            public void mouseExited(MouseEvent e)  { b.repaint(); }
        });
        return b;
    }
    private void atualizarRelogio() {
        if (labelRelogio != null)
            labelRelogio.setText(LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm:ss")));
    }

    private JLabel rotulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setForeground(new Color(30, 50, 100));
        return l;
    }

    private JTextField campo() {
        JTextField t = new JTextField();
        estilizarCampo(t);
        return t;
    }

    private void estilizarCampo(JTextField t) {
        t.setFont(new Font("SansSerif", Font.PLAIN, 13));
        t.setPreferredSize(new Dimension(280, 40));
        t.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(170, 185, 220), 1),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        t.setBackground(new Color(245, 248, 255));
    }

    private void sacudir() {
        Point p = getLocation();
        Timer t = new Timer(30, null);
        int[] passos = {-8, 8, -6, 6, -4, 4, -2, 2, 0};
        int[] i = {0};
        t.addActionListener(e -> {
            if (i[0] < passos.length) setLocation(p.x + passos[i[0]++], p.y);
            else { setLocation(p); ((Timer) e.getSource()).stop(); }
        });
        t.start();
    }
   
   /* public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ignored) {}
        SwingUtilities.invokeLater(MenuPrincipal::new);
    }*/

    public JButton getBtnOcorrencia() {
        return btnOcorrencia;
    }

    public JButton getBtnDetencao() {
        return btnDetencao;
    }

    public void setEntradaSistema(Runnable entradaSistema) {
        this.entradaSistema = entradaSistema;
    }

    public JButton getBtnSair() {
        return btnSair;
    }
    
}
