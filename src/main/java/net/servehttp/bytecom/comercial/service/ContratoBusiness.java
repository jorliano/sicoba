package net.servehttp.bytecom.comercial.service;

import java.io.Serializable;

import javax.inject.Inject;

import net.servehttp.bytecom.comercial.jpa.ContratoJPA;
import net.servehttp.bytecom.comercial.jpa.entity.Contrato;

public class ContratoBusiness implements Serializable {

  private static final long serialVersionUID = 8705835474790847188L;
  @Inject
  private ContratoJPA contratoJPA;

  public void remover(Contrato c) {
    contratoJPA.remover(c);
  }

  public Contrato buscarPorId(int id) {
    return contratoJPA.buscarPorId(Contrato.class, id);
  }

  public <T> T salvar(T t) {
    return contratoJPA.salvar(t);
  }

  public <T> T atualizar(T t) {
    return contratoJPA.atualizar(t);
  }

}