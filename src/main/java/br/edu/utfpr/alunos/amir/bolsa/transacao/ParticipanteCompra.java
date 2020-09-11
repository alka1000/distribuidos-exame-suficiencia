package br.edu.utfpr.alunos.amir.bolsa.transacao;

import java.io.IOException;
import java.util.List;

import br.edu.utfpr.alunos.amir.bolsa.entity.ClienteAcao;
import br.edu.utfpr.alunos.amir.bolsa.util.Logger;
import br.edu.utfpr.alunos.amir.bolsa.util.TratamentoDadosCSV;

public class ParticipanteCompra extends Participante {

	public static Boolean efetivar(Transacao transacao) {
		Logger.writeLog("TRANSACAO DE COMPRA " + transacao + " - EFETIVANDO ");
		if (arquivosTransacoes.get(transacao.getOrdemCompra().getIdCliente()) == null) {
			Logger.writeLog("TRANSACAO DE COMPRA " + transacao + " - FALHA AO EFETIVAR ");
			return false;
		}
		
		
		List<ClienteAcao> acoesCliente = TratamentoDadosCSV.getAcoesCliente(transacao.getOrdemCompra().getIdCliente() + "_TEMP");
		
		try {
			TratamentoDadosCSV.gravaAcoesCliente(transacao.getOrdemCompra().getIdCliente(), acoesCliente);
		} catch (IOException e) {
			Logger.writeLog("TRANSACAO DE COMPRA " + transacao + " - FALHA AO EFETIVAR ");
			return false;
		}
		
		TratamentoDadosCSV.deletaArquivo(transacao.getOrdemCompra().getIdCliente() + "_TEMP");
		Logger.writeLog("TRANSACAO DE COMPRA " + transacao + " - EFETIVADA ");
		synchronized (arquivosTransacoes) {
			arquivosTransacoes.remove(transacao.getOrdemCompra().getIdCliente());
		}
		return true;
	}
	
	public static void abortar(Transacao transacao) {
		Logger.writeLog("TRANSACAO DE COMPRA " + transacao + " - ABORTANDO ");
		synchronized (arquivosTransacoes) {
			arquivosTransacoes.remove(transacao.getOrdemCompra().getIdCliente());
		}
		TratamentoDadosCSV.deletaArquivo(transacao.getOrdemCompra().getIdCliente() + "_TEMP");
		Logger.writeLog("TRANSACAO DE COMPRA " + transacao + " - ABORTADA ");
	}
	
	public static Boolean preparar(Transacao transacao) {
		Logger.writeLog("TRANSACAO DE COMPRA " + transacao + " - INICIO PREPARACAO ");
		synchronized (arquivosTransacoes) {
			if (arquivosTransacoes.get(transacao.getOrdemCompra().getIdCliente()) != null) {
				return false;
			}
			arquivosTransacoes.put(transacao.getOrdemCompra().getIdCliente(), transacao);
		}
		
		Integer quantidadeTransicionada = Math.min(transacao.getOrdemCompra().getQuantidade(), transacao.getOrdemVenda().getQuantidade());
		
		List<ClienteAcao> acoesCliente = TratamentoDadosCSV.getAcoesCliente(transacao.getOrdemCompra().getIdCliente());
		
		for (ClienteAcao acaoCliente : acoesCliente) {
			if (acaoCliente.getAcao().equals(transacao.getOrdemCompra().getAcao())) {
				acaoCliente.setQuantidade(acaoCliente.getQuantidade() + quantidadeTransicionada);
			}
		}
		
		try {
			TratamentoDadosCSV.gravaAcoesCliente(transacao.getOrdemCompra().getIdCliente() + "_TEMP", acoesCliente);
		} catch (IOException e) {
			Logger.writeLog("TRANSACAO DE COMPRA " + transacao + " - FALHA AO PREPARAR ");
			return false;
		}
		Logger.writeLog("TRANSACAO DE COMPRA " + transacao + " - PREPARADA ");
		return true;
	}
	
}
