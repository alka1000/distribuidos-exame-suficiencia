package br.edu.utfpr.alunos.amir.bolsa.entity;

public class ClienteCotacaoAcao {
	private String idCliente;
	private Acao acao;
	private Double limiteGanho;
	private Double limitePerda;
	
	public ClienteCotacaoAcao() {
	}

	public ClienteCotacaoAcao(String idCliente, Acao acao, Double limiteGanho, Double limitePerda) {
		super();
		this.idCliente = idCliente;
		this.acao = acao;
		this.limiteGanho = limiteGanho;
		this.limitePerda = limitePerda;
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

	public Double getLimiteGanho() {
		return limiteGanho;
	}

	public void setLimiteGanho(Double limiteGanho) {
		this.limiteGanho = limiteGanho;
	}

	public Double getLimitePerda() {
		return limitePerda;
	}

	public void setLimitePerda(Double limitePerda) {
		this.limitePerda = limitePerda;
	}

	@Override
	public String toString() {
		return "ClienteCotacaoAcao [idCliente=" + idCliente + ", acao=" + acao + ", limiteGanho=" + limiteGanho
				+ ", limitePerda=" + limitePerda + "]";
	}	
}
