package br.edu.utfpr.alunos.amir.bolsa.transacao;

import java.util.HashMap;
import java.util.Map;

import br.edu.utfpr.alunos.amir.bolsa.entity.Ordem;
import br.edu.utfpr.alunos.amir.bolsa.util.Logger;

public class Coordenador {
	private static Map<Transacao, Integer> statusTransacoes = new HashMap<Transacao,Integer>();
	
	public static Boolean abrirTransacao(Ordem ordemCompra, Ordem ordemVenda) {
		Transacao transacao = new Transacao(ordemCompra, ordemVenda);
		statusTransacoes.put(transacao, 0);
		Logger.writeLog("TRANSACAO " + transacao + " INICIADA ");
		if (ParticipanteCompra.preparar(transacao) && ParticipanteVenda.preparar(transacao)) {
			statusTransacoes.put(transacao, 1);
			ParticipanteVenda.efetivar(transacao);
			ParticipanteCompra.efetivar(transacao);
			Logger.writeLog("TRANSACAO " + transacao + " EFETIVADA ");
			return true;
		} else {
			statusTransacoes.put(transacao, 2);
			ParticipanteVenda.abortar(transacao);
			ParticipanteCompra.abortar(transacao);
			Logger.writeLog("TRANSACAO " + transacao + " ABORTADA ");
			return false;
		}
	}
	
	public static Integer statusTransacao(Transacao transacao) {
		return statusTransacoes.get(transacao);
	}
	
	
	
}
