package br.edu.utfpr.alunos.amir.bolsa.entity;

public class OrdemTransicionada {
	private String idAcao;
	private Integer quantidade;
	private Double valor;
	
	public OrdemTransicionada() {
		super();
	}

	public OrdemTransicionada(String idAcao, Integer quantidade, Double valor) {
		super();
		this.idAcao = idAcao;
		this.quantidade = quantidade;
		this.valor = valor;
	}

	public String getIdAcao() {
		return idAcao;
	}

	public void setIdAcao(String idAcao) {
		this.idAcao = idAcao;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "OrdemTransicionada [idAcao=" + idAcao + ", quantidade=" + quantidade + ", valor=" + valor + "]";
	}
	
	
}
