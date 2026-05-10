
package model;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Year;
import model.Ocorrencia;


public class Detencao implements Serializable {

    public Detencao() {
       
    }

    public Detencao(String agenteResponsavel) {
        this.agenteResponsavel = agenteResponsavel;
    }

    public Detencao(String agenteResponsavel, String numeroBO) {
        this.agenteResponsavel = agenteResponsavel;
        this.numeroBO = numeroBO;
        this.idDetencao = gerarIDDetencao();
        this.dataDeDetencao = LocalDateTime.now();
        this.status = StatusDetencao.ATIVA;
               
    }

    public void setHoraDoFimDaDetencao(LocalDateTime now) {
        
    }

    public enum StatusDetencao {
        ATIVA,
        EXPIRADA,
        LIBERTADO,
        PARA_PROCESSO
    }

    //Desfechos possiveis
    public static final String DESFECHO_FALTA_PROVAS    = "LIBERTADO POR : Falta de Provas";
    public static final String DESFECHO_CAUCAO_PAGA    = "LIBERTADO POR : Caucao Paga";
    public static final String DESFECHO_ORDEM_MP          = "LIBERTADO POR : Ordem do Ministerio Publico";
    public static final String DESFECHO_48H_EXPIRADO   = "LIBERTADO POR : Prazo de 48 Horas Expirado";
    public static final String DESFECHO_PRISAO_PREV       = "TRANSFERIDO PARA : Prisao Preventiva";
    public static final String DESFECHO_TRANSFERIDO     = "TRANSFERIDO PARA :Outro Estabelecimento";
    
    
    private String idDetencao, numeroBO;
    private String  nomeDoDetido;
    private String motivoDaDetencao;
    private String LocalDaDetencao;
    private String agenteResponsavel;
    private StatusDetencao status;
    private String   desfecho;          

    private LocalDateTime horaDoFimDaDetencao;
    private LocalDateTime dataDeDetencao;

    private Ocorrencia  ocorrencia;
    int contador = 0;
    


    public Detencao(String nomeDoDetido, String agenteResponsavel,StatusDetencao status, Ocorrencia ocorrencia) {
        this.idDetencao          = gerarIDDetencao();
        this.nomeDoDetido        = detido(ocorrencia);
        this.agenteResponsavel   = agenteResponsavel;
        this.status              = status;
        this.horaDoFimDaDetencao = null;
        this.dataDeDetencao      = LocalDateTime.now();
        this.ocorrencia          = ocorrencia;
        this.desfecho            = null;      
    }

    
    public String getIdDetencao() { 
        return idDetencao; 
    }

    public void setIdDetencao(String idDetencao) {
        this.idDetencao = idDetencao;
    }
    

    public LocalDateTime getDataDeDetencao() {
        return dataDeDetencao;
    }
    public void setDataDeDetencao(LocalDateTime d){
        this.dataDeDetencao = d;
    }

    public LocalDateTime getHoraDofiDaDetencao(){
        return horaDoFimDaDetencao;
    }
    public void setHoraDofiDaDetencao(LocalDateTime h)   { this.horaDoFimDaDetencao = h; }

    public String getNomeDoDetido()                      { return nomeDoDetido; }
    public void setNomeDoDetido(String n)                { this.nomeDoDetido = n; }

    public String getMotivoDaDetencao()                  { return motivoDaDetencao; }
    public void setMotivoDaDetencao(String m)            { this.motivoDaDetencao = m; }

    public String getLocalDaDetencao()                   { return LocalDaDetencao; }
    public void setLocalDaDetencao(String l)             { this.LocalDaDetencao = l; }

    public String getAgenteResponsavel()                 { return agenteResponsavel; }
    public void setAgenteResponsavel(String a)           { this.agenteResponsavel = a; }

    public StatusDetencao getStatus()                    { return status; }
    public void setStatus(StatusDetencao s)              { this.status = s; }

    public String getDesfecho()                          { return desfecho; }  
    public void setDesfecho(String d)                    { this.desfecho = d; } 

    public Ocorrencia getOcorrencia()                    { return ocorrencia; }

    
    //  Encerrar Detencao

    public void encerrarDetencao(StatusDetencao novoStatus, String desfecho) {
        this.horaDoFimDaDetencao = LocalDateTime.now();
        this.status              = StatusDetencao.LIBERTADO;
        this.desfecho            = desfecho;      // â† regista o desfecho
    }

  
    public void encerrarDetencao(StatusDetencao novoStatus) {
        this.horaDoFimDaDetencao = LocalDateTime.now();
        this.status              = StatusDetencao.LIBERTADO;
        this.desfecho            = "Nao especificado";
    }

    //  Limite legal das 48 horas
    public LocalDateTime getLimiteLegal() {
        return dataDeDetencao.plusHours(48);
    }

    public boolean alertarDeLimite() {
        return horaDoFimDaDetencao == null &&
               LocalDateTime.now().isAfter(getLimiteLegal().minusHours(30));
    }

   
    //  Mostrar dados no terminal
    public void mostrarDados() {
        System.out.println("data limite: "       + getLimiteLegal());
        System.out.println("data do fim: "       + this.horaDoFimDaDetencao);
        System.out.println("data da detencao: "  + this.dataDeDetencao);
        System.out.println("status: "            + this.status);
        System.out.println("desfecho: "          + this.desfecho);
    }

    
    // // Gerar ID da detencao
    public String gerarIDDetencao() {
        int anoAtual = Year.now().getValue();
        contador++;
        return String.valueOf(contador) +"/"+ anoAtual;
    }

   
    //  Dados do BO associado
    
    public String dadosDoBO(Ocorrencia ocorrencia) {
        return "OCORRENCIA NUMERO: " + ocorrencia.getNumeroBO()      + '\n'
             + "LOCAL DA OCORRENCIA: " + ocorrencia.getLocalOcorrencia() + '\n'
             + "HORA DA OCORRENCIA: "  + ocorrencia.getDataHora();
    }

    // Resumo da Detencao
    public String DadosDetencaoEncerada(Detencao detencao) {
        return "ID DA DETENCAO: "                   + detencao.getIdDetencao()          + '\n'
             + "OFICIAL RESPONSAVEL: "              + detencao.getAgenteResponsavel()   + '\n'
             + "NOME DO DETIDO: "                   + detencao.getNomeDoDetido()        + '\n'
             + "DATA DA DETENCAO: "                 + detencao.getDataDeDetencao()      + '\n'
             + "DATA DO ENCERRAMENTO: "             + detencao.getHoraDofiDaDetencao()  + '\n'
             + "DESFECHO: "                         + detencao.getDesfecho();           
    }

    public String detido(Ocorrencia ocorrencia) {
        return ocorrencia.procurarSuspeito();
    }
    
    public String gerarIDDetencao (int contador){
      int anoAtual = Year.now().getValue();
      return contador + "/"+ anoAtual;
    }
    
    
}
