package br.edu.utfpr.alunos.amir.bolsa.entity;

public class Acao {
	private String id;
	private Double cotacao;
	
	public Acao() {
		
	}

	public Acao(String id, Double cotacao) {
		super();
		this.id = id;
		this.cotacao = cotacao;
	}

	public Acao(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getCotacao() {
		return cotacao;
	}

	public void setCotacao(Double cotacao) {
		this.cotacao = cotacao;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.id.equals(((Acao)obj).getId());
	}

	@Override
	public String toString() {
		return "Bolsa [id=" + id + ", cotacao=" + cotacao + "]";
	}
	
}
