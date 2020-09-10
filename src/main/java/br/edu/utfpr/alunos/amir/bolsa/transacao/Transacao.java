package br.edu.utfpr.alunos.amir.bolsa.transacao;

import br.edu.utfpr.alunos.amir.bolsa.entity.Ordem;

public class Transacao {
	Ordem ordemCompra;
	Ordem ordemVenda;
	
	public Transacao() {
		super();
	}

	public Transacao(Ordem ordemCompra, Ordem ordemVenda) {
		super();
		this.ordemCompra = ordemCompra;
		this.ordemVenda = ordemVenda;
	}

	public Ordem getOrdemCompra() {
		return ordemCompra;
	}

	public void setOrdemCompra(Ordem ordemCompra) {
		this.ordemCompra = ordemCompra;
	}

	public Ordem getOrdemVenda() {
		return ordemVenda;
	}

	public void setOrdemVenda(Ordem ordemVenda) {
		this.ordemVenda = ordemVenda;
	}

	@Override
	public String toString() {
		return "Transacao [ordemCompra=" + ordemCompra + ", ordemVenda=" + ordemVenda + "]";
	}
	
	
}
