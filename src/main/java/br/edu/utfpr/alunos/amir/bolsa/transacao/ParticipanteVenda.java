package br.edu.utfpr.alunos.amir.bolsa.transacao;

import java.io.IOException;
import java.util.List;

import br.edu.utfpr.alunos.amir.bolsa.entity.ClienteAcao;
import br.edu.utfpr.alunos.amir.bolsa.util.Logger;
import br.edu.utfpr.alunos.amir.bolsa.util.TratamentoDadosCSV;

public class ParticipanteVenda extends Participante {

	public static Boolean efetivar(Transacao transacao) {
		Logger.writeLog("TRANSACAO DE VENDA " + transacao + " - EFETIVANDO ");
		if (arquivosTransacoes.get(transacao.getOrdemVenda().getIdCliente()) == null) {
			Logger.writeLog("TRANSACAO DE VENDA " + transacao + " - FALHA AO EFETIVAR ");
			return false;
		}
		
		
		List<ClienteAcao> acoesCliente = TratamentoDadosCSV.getAcoesCliente(transacao.getOrdemVenda().getIdCliente() + "_TEMP");
		
		try {
			TratamentoDadosCSV.gravaAcoesCliente(transacao.getOrdemVenda().getIdCliente(), acoesCliente);
		} catch (IOException e) {
			Logger.writeLog("TRANSACAO DE VENDA " + transacao + " - FALHA AO EFETIVAR ");
			return false;
		}
		
		TratamentoDadosCSV.deletaArquivo(transacao.getOrdemVenda().getIdCliente() + "_TEMP");
		Logger.writeLog("TRANSACAO DE VENDA " + transacao + " - EFETIVADA ");
		synchronized (arquivosTransacoes) {
			arquivosTransacoes.remove(transacao.getOrdemVenda().getIdCliente());
		}
		return true;
	}
	
	public static void abortar(Transacao transacao) {
		Logger.writeLog("TRANSACAO DE VENDA " + transacao + " - ABORTANDO ");
		synchronized (arquivosTransacoes) {
			arquivosTransacoes.remove(transacao.getOrdemVenda().getIdCliente());
		}
		TratamentoDadosCSV.deletaArquivo(transacao.getOrdemVenda().getIdCliente() + "_TEMP");
		Logger.writeLog("TRANSACAO DE VENDA " + transacao + " - ABORTADA ");
	}
	
	public static Boolean preparar(Transacao transacao) {
		Logger.writeLog("TRANSACAO DE VENDA " + transacao + " - INICIO PREPARACAO ");
		synchronized (arquivosTransacoes) {
			if (arquivosTransacoes.get(transacao.getOrdemVenda().getIdCliente()) != null) {
				return false;
			}
			arquivosTransacoes.put(transacao.getOrdemVenda().getIdCliente(), transacao);
		}
		
		Integer quantidadeTransicionada = Math.min(transacao.getOrdemCompra().getQuantidade(), transacao.getOrdemVenda().getQuantidade());
		
		List<ClienteAcao> acoesCliente = TratamentoDadosCSV.getAcoesCliente(transacao.getOrdemVenda().getIdCliente());
		
		for (ClienteAcao acaoCliente : acoesCliente) {
			if (acaoCliente.getAcao().equals(transacao.getOrdemVenda().getAcao())) {
				acaoCliente.setQuantidade(acaoCliente.getQuantidade() - quantidadeTransicionada);
			}
		}
		
		try {
			TratamentoDadosCSV.gravaAcoesCliente(transacao.getOrdemVenda().getIdCliente() + "_TEMP", acoesCliente);
		} catch (IOException e) {
			Logger.writeLog("TRANSACAO DE VENDA " + transacao + " - FALHA AO PREPARAR ");
			return false;
		}
		Logger.writeLog("TRANSACAO DE VENDA " + transacao + " - PREPARADA ");
		return true;
	}
	
}
