package net.servehttp.bytecom.persistence.jpa.entity.provedor.impl;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import net.servehttp.bytecom.persistence.jpa.entity.extra.EntityGeneric;
import net.servehttp.bytecom.persistence.jpa.entity.provedor.IServer;

@Entity
@Table(name = "mikrotik")
public class Mikrotik extends EntityGeneric implements IServer {

  private static final long serialVersionUID = -5454193409609731613L;

  @Lob
  private String description;
  private String name;
  private String host;
  private int port;
  private String login;
  private String pass;

  public Mikrotik() {
    this.login = "admin";
    this.pass = "";
    this.port = 8728;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getHost() {
    return this.host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPort() {
    return this.port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getPass() {
    return this.pass;
  }

  public void setPass(String pass) {
    this.pass = pass;
  }

  public String getLogin() {
    return this.login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

}
