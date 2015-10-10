package net.servehttp.bytecom.controller.financeiro;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.servehttp.bytecom.controller.extra.GenericoController;
import net.servehttp.bytecom.persistence.jpa.entity.comercial.Cliente;
import net.servehttp.bytecom.persistence.jpa.entity.financeiro.Mensalidade;
import net.servehttp.bytecom.service.comercial.ClienteService;
import net.servehttp.bytecom.service.financeiro.MensalidadeService;
import net.servehttp.bytecom.util.web.AlertaUtil;
import net.servehttp.bytecom.util.web.WebUtil;

/**
 *
 * Created by <a href="https://github.com/clairtonluz">Clairton Luz</a>
 */
@Named
@ViewScoped
public class CadastrarBoletosController extends GenericoController implements Serializable {

  private static final long serialVersionUID = -5517379889465547854L;
  private Mensalidade mensalidade;
  private Cliente cliente;
  private int clienteId;
  private int numeroBoletoInicio;
  private int numeroBoletoFim;
  private double descontoGeracao;
  private double valor;
  private LocalDate dataInicio;

  @Inject
  private MensalidadeService service;
  @Inject
  private ClienteService clientService;

  @PostConstruct
  public void init() {
    getParameters();
    if (cliente != null) {
      if (mensalidade == null) {
        mensalidade = getNovaMensalidade();
        dataInicio = mensalidade.getDataVencimento();
        valor = mensalidade.getValor();
      }
    }
  }

  private void getParameters() {
    String clienteId = WebUtil.getParameters("clienteId");
    if (clienteId != null && !clienteId.isEmpty()) {
      cliente = clientService.buscarPorId(Integer.parseInt(clienteId));
    }
  }

  public void cadastrarBoletosCaixa() {
    if (boletosNaoRegistrado(numeroBoletoInicio, numeroBoletoFim)) {
      LocalDate c = dataInicio;

      if (numeroBoletoInicio < numeroBoletoFim) {
        for (int i = numeroBoletoInicio; i <= numeroBoletoFim; i++) {
          gravarBoleto(c, i);
          c = c.plusMonths(1);
        }
      } else {
        for (int i = numeroBoletoInicio; i >= numeroBoletoFim; i--) {
          gravarBoleto(c, i);
          c = c.plusMonths(1);
        }
      }
      AlertaUtil.info("Boletos gerados com sucesso!");
    }
  }

  private void gravarBoleto(LocalDate c, int numeroBoleto) {
    Mensalidade m = service.getNovaMensalidade(cliente, c);
    m.setNumeroBoleto(numeroBoleto);
    m.setDesconto(descontoGeracao);
    m.setValor(valor);
    jpa.salvar(m);
  }

  public Mensalidade getNovaMensalidade() {
    LocalDate d =
        LocalDate.now().plusMonths(1).withDayOfMonth(cliente.getContrato().getVencimento());
    return service.getNovaMensalidade(cliente, d);
  }

  private boolean boletosNaoRegistrado(int inicio, int fim) {
    boolean validos = true;
    List<Mensalidade> listMensalidades = service.buscarMensalidadesPorBoleto(inicio, fim);
    if (!listMensalidades.isEmpty()) {
      validos = false;
      StringBuilder sb = new StringBuilder("Os seguintes boletos já estão cadastrados");
      for (Mensalidade m : listMensalidades) {
        sb.append(" : " + m.getNumeroBoleto());
      }
      AlertaUtil.error(sb.toString());
    }
    return validos;
  }

  public Cliente getCliente() {
    return cliente;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  public int getClienteId() {
    return clienteId;
  }

  public void setClienteId(int clienteId) {
    this.clienteId = clienteId;
  }

  public Mensalidade getMensalidade() {
    return mensalidade;
  }

  public void setMensalidade(Mensalidade mensalidade) {
    this.mensalidade = mensalidade;
  }

  public int getNumeroBoletoInicio() {
    return numeroBoletoInicio;
  }

  public void setNumeroBoletoInicio(int numeroBoletoInicio) {
    this.numeroBoletoInicio = numeroBoletoInicio;
  }

  public int getNumeroBoletoFim() {
    return numeroBoletoFim;
  }

  public void setNumeroBoletoFim(int numeroBoletoFim) {
    this.numeroBoletoFim = numeroBoletoFim;
  }

  public LocalDate getDataInicio() {
    return dataInicio;
  }

  public void setDataInicio(LocalDate dataInicio) {
    this.dataInicio = dataInicio;
  }

  public double getDescontoGeracao() {
    return descontoGeracao;
  }

  public void setDescontoGeracao(double descontoGeracao) {
    this.descontoGeracao = descontoGeracao;
  }

public double getValor() {
	return valor;
}

public void setValor(double valor) {
	this.valor = valor;
}
}
