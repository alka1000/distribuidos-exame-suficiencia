package br.edu.utfpr.alunos.amir.bolsa.entity;

public class ClienteAcao {
	private String idCliente;
	private Acao acao;
	private Integer quantidade;
	
	public ClienteAcao() {
		
	}

	public ClienteAcao(String idCliente, Acao acao, Integer quantidade) {
		super();
		this.idCliente = idCliente;
		this.acao = acao;
		this.quantidade = quantidade;
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

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	@Override
	public String toString() {
		return "ClienteAcao [idCliente=" + idCliente + ", acao=" + acao + ", quantidade=" + quantidade + "]";
	}
	
}
