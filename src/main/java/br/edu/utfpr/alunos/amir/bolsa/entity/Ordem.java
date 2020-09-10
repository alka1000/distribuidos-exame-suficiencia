package br.edu.utfpr.alunos.amir.bolsa.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Ordem {
	private String idCliente;
	private Acao acao;
	private Double preco;
	private Integer quantidade;
	private Calendar prazo;
	
	public Ordem() {
		
	}
	
	public Ordem(String idCliente, String ordem) {
		super();
		this.idCliente = idCliente;
		this.acao = new Acao(ordem.split(";")[0]);
		this.preco = Double.parseDouble(ordem.split(";")[1]);
		this.quantidade = Integer.parseInt(ordem.split(";")[2]);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date date = new Date();;
		try {
			date = sdf.parse(ordem.split(";")[3]);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.prazo = Calendar.getInstance();
		this.prazo.setTime(date);
	}
	
	public Ordem(String idCliente, Acao acao, Double preco, Integer quantidade, Calendar prazo) {
		super();
		this.idCliente = idCliente;
		this.acao = acao;
		this.preco = preco;
		this.quantidade = quantidade;
		this.prazo = prazo;
	}

	public String getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}

	public Acao getAcao() {
		return acao;
	}

	public void setAcao(Acao acao) {
		this.acao = acao;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Calendar getPrazo() {
		return prazo;
	}

	public void setPrazo(Calendar prazo) {
		this.prazo = prazo;
	}

	@Override
	public String toString() {
		return "Ordem [idCliente=" + idCliente + ", acao=" + acao + ", preco=" + preco + ", quantidade=" + quantidade
				+ ", prazo=" + prazo + "]";
	}
}
