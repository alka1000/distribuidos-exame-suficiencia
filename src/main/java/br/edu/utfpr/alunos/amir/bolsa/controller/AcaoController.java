package br.edu.utfpr.alunos.amir.bolsa.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import br.edu.utfpr.alunos.amir.bolsa.entity.Acao;
import br.edu.utfpr.alunos.amir.bolsa.entity.ClienteCotacaoAcao;
import br.edu.utfpr.alunos.amir.bolsa.entity.Ordem;
import br.edu.utfpr.alunos.amir.bolsa.entity.OrdemTransicionada;
import br.edu.utfpr.alunos.amir.bolsa.transacao.Coordenador;

@Named
@RequestScoped
public class AcaoController {
	
	private static final String LOCK = "###" + AcaoController.class.toString() + "###";
	
	private static List<Acao> listaAcoes = new ArrayList<>();
	
	private static Boolean threadCompraVendaAcoesRunning = false;
	
	private static Boolean novaOrdemCompraVenda = false;
	
	private static List<Ordem> listaOrdensCompra = new ArrayList<>();
	
	private static List<Ordem> listaOrdensVenda = new ArrayList<>();
	
	private static Map<String,List<ClienteCotacaoAcao>> listaCotacoes = new HashMap<String,List<ClienteCotacaoAcao>>();
	
	private static Map<String,List<OrdemTransicionada>> listaOrdensTransicionadas = new HashMap<String,List<OrdemTransicionada>>();
	
	// pegar a lista de bolsas, se for pegar de local externo só alterar esse método
	private synchronized void atualizarListaBolsas() {
		synchronized (listaAcoes) {
			if (listaAcoes.isEmpty()) {
				listaAcoes.add(new Acao("PETR4", 22.92));
				listaAcoes.add(new Acao("VALE3", 60.49));
				listaAcoes.add(new Acao("ITUB4", 24.96));
				listaAcoes.add(new Acao("BBDC4", 21.97));
				listaAcoes.add(new Acao("BBAS3", 33.95));
				updateAleatorioListaAcoes();
			}
		}
	}
	
	private void updateAleatorioListaAcoes() {
		Runnable r = new Runnable() {
            @Override
            public void run() {
                while (!listaAcoes.isEmpty()) {
                	synchronized (listaAcoes) {
                		for (Acao acao : listaAcoes) {
                			acao.setCotacao(acao.getCotacao() + (Math.random()-0.5));
                		}
					}
                	try {
						Thread.currentThread();
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }
        };
        new Thread(r).start();
	}
	
	public List<Acao> getAcoes() {
		synchronized (LOCK) {
			if (listaAcoes.isEmpty()) atualizarListaBolsas();
		}
		return listaAcoes;
	}
	
	public void adicionarAcaoListaCotacao(String idCliente, String idAcao) throws DataFormatException {
		synchronized (listaCotacoes) {
			if (listaCotacoes.get(idCliente) == null) {
				listaCotacoes.put(idCliente, new ArrayList<>());
			}
		}
		synchronized (listaCotacoes.get(idCliente)) {
			Boolean jaExisteCotacao = false;
			for (ClienteCotacaoAcao cotacao : listaCotacoes.get(idCliente)) {
				if (cotacao.getAcao().getId().equals(idAcao)) {
					jaExisteCotacao = true;
					break;
				}
			}
			ClienteCotacaoAcao cotacao = null;
			for (Acao acao: getAcoes()) {
				if (acao.getId().equals(idAcao)) {
					cotacao = new ClienteCotacaoAcao();
					cotacao.setAcao(acao);
					break;
				}
			}
			if (!jaExisteCotacao && cotacao != null) {
				cotacao.setIdCliente(idCliente);
				listaCotacoes.get(idCliente).add(cotacao);
			} else {
				throw new DataFormatException("Ação não existente ou já adicionada na lista de cotações desse cliente.");
			}
		}
	}

	public List<ClienteCotacaoAcao> getListaCotacao(String idCliente) {
		synchronized (listaCotacoes) {
			if (listaCotacoes.get(idCliente) == null) {
				listaCotacoes.put(idCliente, new ArrayList<>());
			}
		}
		return listaCotacoes.get(idCliente);
	}
	
	public void removerAcaoListaCotacao(String idCliente, String idAcao) throws DataFormatException {
		if (listaCotacoes.get(idCliente) == null) {
			throw new DataFormatException("Cliente não possui ações em sua lista de cotações.");
		}
		synchronized (listaCotacoes.get(idCliente)) {
			ClienteCotacaoAcao cotacao = null;
			for (ClienteCotacaoAcao cotacaoObj : listaCotacoes.get(idCliente)) {
				if (cotacaoObj.getAcao().getId().equals(idAcao)) {
					cotacao = cotacaoObj;
					break;
				}
			}
			if (cotacao == null) {
				throw new DataFormatException("Cliente não possui a ação informada em sua lista de cotações.");
			}
			listaCotacoes.get(idCliente).remove(cotacao);
		}
	}
	
	public void alteraAcaoListaCotacao(String idCliente, String idAcao, ClienteCotacaoAcao cotacao) throws DataFormatException {
		if (listaCotacoes.get(idCliente) == null) {
			throw new DataFormatException("Cliente não possui ações em sua lista de cotações.");
		}
		synchronized (listaCotacoes.get(idCliente)) {
			ClienteCotacaoAcao cotacaoFind = null;
			for (ClienteCotacaoAcao cotacaoObj : listaCotacoes.get(idCliente)) {
				if (cotacaoObj.getAcao().getId().equals(idAcao)) {
					cotacaoFind = cotacaoObj;
					break;
				}
			}
			if (cotacaoFind == null) {
				throw new DataFormatException("Cliente não possui a ação informada em sua lista de cotações.");
			}
			cotacaoFind.setLimiteGanho(cotacao.getLimiteGanho());
			cotacaoFind.setLimitePerda(cotacao.getLimitePerda());
		}
	}

	public void notificacoesLimites(SseEventSink eventSink, Sse sse, String idCliente) {
		Runnable r = new Runnable() {
            @Override
            public void run() {
            	if (listaCotacoes.get(idCliente) == null) {
    				listaCotacoes.put(idCliente, new ArrayList<>());
    			}
            	List<ClienteCotacaoAcao> listaCotacao = listaCotacoes.get(idCliente);
                while (true) {
                	try {
						Thread.currentThread();
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	for (ClienteCotacaoAcao cotacao : listaCotacao) {
                		if (
                			(cotacao.getLimiteGanho() != null && cotacao.getAcao().getCotacao() > cotacao.getLimiteGanho())
                				|| (cotacao.getLimitePerda() != null && cotacao.getAcao().getCotacao() < cotacao.getLimitePerda())) {
	                		OutboundSseEvent event = sse.newEventBuilder()
	                				.mediaType(MediaType.APPLICATION_JSON_TYPE)
	                				.data(ClienteCotacaoAcao.class, new ClienteCotacaoAcao(cotacao.getIdCliente(), cotacao.getAcao(), cotacao.getLimiteGanho()*1+0, cotacao.getLimitePerda()*1+0))
	                				.build();
	                		eventSink.send(event);
	                		cotacao.setLimiteGanho(null);
	                		cotacao.setLimitePerda(null);
                		}
                	}
                }
            }
        };
        new Thread(r).start();
	}

	public void adicionaOrdemCompra(Ordem ordem) {
		listaOrdensCompra.add(ordem);
		synchronized (threadCompraVendaAcoesRunning) {
			if (!threadCompraVendaAcoesRunning) {
				threadCompraVendaAcoesRunning = true;
				startCompraVendaAcoes();
			}
		}
		novaOrdemCompraVenda = true;
	}

	public void adicionaOrdemVenda(Ordem ordem) {
		listaOrdensVenda.add(ordem);
		synchronized (threadCompraVendaAcoesRunning) {
			if (!threadCompraVendaAcoesRunning) {
				threadCompraVendaAcoesRunning = true;
				startCompraVendaAcoes();
			}
		}
		novaOrdemCompraVenda = true;
	}

	private void startCompraVendaAcoes() {
		Runnable r = new Runnable() {
            @Override
            public void run() {
                while (true) {
                	try {
						Thread.currentThread();
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	if (novaOrdemCompraVenda) {
	                	List<Ordem> listaOrdensCompraVencidas = new ArrayList<Ordem>();
	                	List<Ordem> listaOrdensVendaVencidas = new ArrayList<Ordem>();
	                	List<Ordem> ordensQueSeraoExecutadas = new ArrayList<Ordem>();
	                	synchronized (listaOrdensCompra) {
	                		for (Ordem ordemCompra : listaOrdensCompra) {
	                			if (Calendar.getInstance().before(ordemCompra.getPrazo())) {
	                				synchronized (listaOrdensVenda) {
	                					for (Ordem ordemVenda : listaOrdensVenda) {
	                						if (Calendar.getInstance().before(ordemVenda.getPrazo())) {
	                							if (ordemCompra.getAcao().equals(ordemVenda.getAcao())) {
	                								if (ordemCompra.getPreco().equals(ordemVenda.getPreco())) {
	                									ordensQueSeraoExecutadas.add(ordemCompra);
	                									ordensQueSeraoExecutadas.add(ordemVenda);
	                									break;
	                								}
	                							}
	                						} else {
	                							listaOrdensVendaVencidas.add(ordemVenda);
	                						}
	                					}
									}
	                				if (!ordensQueSeraoExecutadas.isEmpty()) {
	                					break;
	                				}
	                			} else {
	                				listaOrdensCompraVencidas.add(ordemCompra);
	                			}
	                		}
						}
	                	if (!ordensQueSeraoExecutadas.isEmpty()) {
	                		Ordem ordemCompra = ordensQueSeraoExecutadas.remove(0);
	                		Ordem ordemVenda = ordensQueSeraoExecutadas.remove(0);
	                		listaOrdensCompra.remove(ordemCompra);
	                		listaOrdensVenda.remove(ordemVenda);
	                		realizarCompraVendaAcao(ordemCompra, ordemVenda);
	                	} else {
	                		novaOrdemCompraVenda = false;
	                	}
	                	for (Ordem ordem : listaOrdensCompraVencidas) {
	                		listaOrdensCompra.remove(ordem);
	                	}
	                	for (Ordem ordem : listaOrdensVendaVencidas) {
	                		listaOrdensVenda.remove(ordem);
	                	}
                	}
                }
            }

			private void realizarCompraVendaAcao(Ordem ordemCompra, Ordem ordemVenda) {
				if (Coordenador.abrirTransacao(ordemCompra, ordemVenda)) {
					Integer quantidadeTransicionada = Math.min(ordemCompra.getQuantidade(), ordemVenda.getQuantidade());
					ordemCompra.setQuantidade(ordemCompra.getQuantidade() - quantidadeTransicionada);
					ordemVenda.setQuantidade(ordemVenda.getQuantidade() - quantidadeTransicionada);
					if (ordemCompra.getQuantidade() > 0) {
						listaOrdensCompra.add(ordemCompra);
					}
					if (ordemVenda.getQuantidade() > 0) {
						listaOrdensVenda.add(ordemVenda);
					}
					try {
						adicionarAcaoListaCotacao(ordemCompra.getIdCliente(), ordemCompra.getAcao().getId());
					} catch (DataFormatException e) {
					}
					OrdemTransicionada ordemTransicionada = new OrdemTransicionada(ordemCompra.getAcao().getId(), quantidadeTransicionada, ordemCompra.getPreco()*quantidadeTransicionada);
					synchronized (listaOrdensTransicionadas) {
						if (listaOrdensTransicionadas.get(ordemCompra.getIdCliente()) == null) {
							listaOrdensTransicionadas.put(ordemCompra.getIdCliente(), new ArrayList<OrdemTransicionada>());
						}
						if (listaOrdensTransicionadas.get(ordemVenda.getIdCliente()) == null) {
							listaOrdensTransicionadas.put(ordemVenda.getIdCliente(), new ArrayList<OrdemTransicionada>());
						}
					}
					listaOrdensTransicionadas.get(ordemCompra.getIdCliente()).add(ordemTransicionada);
					listaOrdensTransicionadas.get(ordemVenda.getIdCliente()).add(ordemTransicionada);
				}
			}
        };
        new Thread(r).start();
		
	}

	public void notificacoesOrdensCompraVenda(SseEventSink eventSink, Sse sse, String idCliente) {
		Runnable r = new Runnable() {
            @Override
            public void run() {
            	synchronized (listaOrdensTransicionadas) {
            		if (listaOrdensTransicionadas.get(idCliente) == null) {
            			listaOrdensTransicionadas.put(idCliente, new ArrayList<>());
            		}
				}
            	List<OrdemTransicionada> listaOrdens = listaOrdensTransicionadas.get(idCliente);
                while (true) {
                	try {
						Thread.currentThread();
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	if (!listaOrdens.isEmpty()) {
	            		OrdemTransicionada ordem = listaOrdens.remove(0);
	    		  		OutboundSseEvent event = sse.newEventBuilder()
	        				.mediaType(MediaType.APPLICATION_JSON_TYPE)
	        				.data(OrdemTransicionada.class, ordem)
	        				.build();
	                		eventSink.send(event);
            		}
            	}
            }
        };
        new Thread(r).start();
	}
}
