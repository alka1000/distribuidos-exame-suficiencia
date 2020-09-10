package br.edu.utfpr.alunos.amir.bolsa.util;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import br.edu.utfpr.alunos.amir.bolsa.entity.Acao;
import br.edu.utfpr.alunos.amir.bolsa.entity.ClienteAcao;

public class TratamentoDadosCSV {
	
	public static List<ClienteAcao> getAcoesCliente(String idCliente){
		List<ClienteAcao> resultado = new ArrayList<ClienteAcao>();
		
		try {
			Reader reader = Files.newBufferedReader(Paths.get("c:/data/" + idCliente + ".csv"));
			CSVReader csvReader = new CSVReaderBuilder(reader).build();
			List<String[]> dados = csvReader.readAll();
			
			for (String[] linha : dados) {
				ClienteAcao clienteAcao = new ClienteAcao(idCliente, new Acao(linha[0]), Integer.parseInt(linha[1]));
				resultado.add(clienteAcao);
			}
		} catch (IOException e) {
		}
		
		return resultado;
	}
	
	public static void gravaAcoesCliente(String idCliente, List<ClienteAcao> listaAcoesCliente) throws IOException {
		List<String[]> linhas = new ArrayList<String[]>();

		for(ClienteAcao clienteAcao : listaAcoesCliente) {
			linhas.add(new String[] {clienteAcao.getAcao().getId(), clienteAcao.getQuantidade()+"" });
		}
		
		Writer writer = Files.newBufferedWriter(Paths.get("c:/data/" + idCliente + ".csv"));
		
		CSVWriter csvWriter = new CSVWriter(writer);

        csvWriter.writeAll(linhas);

        csvWriter.flush();
        csvWriter.close();
        writer.close();
		
	}
	
	public static void deletaArquivo(String idArquivo) {
		File file = new File("c:/data/" + idArquivo + ".csv"); 
		file.delete();
	}
}
