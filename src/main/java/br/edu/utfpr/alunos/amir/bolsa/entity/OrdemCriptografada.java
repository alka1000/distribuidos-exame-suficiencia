package br.edu.utfpr.alunos.amir.bolsa.entity;

public class OrdemCriptografada {
	private String ordem;

	public OrdemCriptografada() {
		
	}
	
	public OrdemCriptografada(String ordem) {
		super();
		this.ordem = ordem;
	}

	public String getOrdem() {
		return ordem;
	}

	public void setOrdem(String ordem) {
		this.ordem = ordem;
	}
	
}
