package br.edu.utfpr.alunos.amir.bolsa.service;

import java.util.List;
import java.util.zip.DataFormatException;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import br.edu.utfpr.alunos.amir.bolsa.controller.AcaoController;
import br.edu.utfpr.alunos.amir.bolsa.entity.Acao;
import br.edu.utfpr.alunos.amir.bolsa.entity.ClienteCotacaoAcao;
import br.edu.utfpr.alunos.amir.bolsa.entity.Ordem;
import br.edu.utfpr.alunos.amir.bolsa.entity.OrdemCriptografada;
import br.edu.utfpr.alunos.amir.bolsa.util.EncriptaDecriptaRSA;

@ApplicationPath("/ws")
@Path("/")
public class MercadoBolsasService extends Application {
	
	@Inject private AcaoController acaoController;
	
	@GET
	@Consumes(value = { MediaType.APPLICATION_JSON })
	@Produces(value = { MediaType.APPLICATION_JSON })
	@Path("/bolsas/cotacoes")
	public Response getAll() {
		try {
			List<Acao> listaBolsas = acaoController.getAcoes();
			return Response.ok().entity(listaBolsas).build(); 
		} catch (NoResultException nre) {
			return Response.status(Status.NOT_FOUND.getStatusCode()).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("deu erro").build(); 
		} 
	}
	
	/**
	 * adiciona a ação na lista de cotações do cliente
	 * @param idCliente id do cliente
	 * @param idAcao id da ação
	 * @return resposta http
	 */
	@POST
	@Consumes(value = { MediaType.APPLICATION_JSON })
	@Produces(value = { MediaType.APPLICATION_JSON })
	@Path("/bolsas/cotacoes/{idCliente}/{idAcao}")
	public Response adicionarAcaoListaCotacao(
			@PathParam("idCliente") String idCliente, 
			@PathParam("idAcao") String idAcao
	) {
		try {
			acaoController.adicionarAcaoListaCotacao(idCliente, idAcao);
			return Response.ok().build(); 
		} catch (NoResultException nre) {
			return Response.status(Status.NOT_FOUND.getStatusCode()).build();
		} catch (DataFormatException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build(); 
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getStackTrace()).build(); 
		} 
	}
	
	/**
	 * cadastro de limites de ganhos e perdas
	 * @param idCliente id do cliente
	 * @param idAcao id da ação
	 * @param cotacao objeto com os limites
	 * @return resposta http
	 */
	@PUT
	@Consumes(value = { MediaType.APPLICATION_JSON })
	@Produces(value = { MediaType.APPLICATION_JSON })
	@Path("/bolsas/cotacoes/{idCliente}/{idAcao}")
	public Response alteraAcaoListaCotacao(
			@PathParam("idCliente") String idCliente, 
			@PathParam("idAcao") String idAcao,
			ClienteCotacaoAcao cotacao
	) {
		try {
			acaoController.alteraAcaoListaCotacao(idCliente, idAcao, cotacao);
			return Response.ok().build(); 
		} catch (NoResultException nre) {
			return Response.status(Status.NOT_FOUND.getStatusCode()).build();
		} catch (DataFormatException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build(); 
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getStackTrace()).build(); 
		} 
	}
	
	/**
	 * retorna a lista de cotações do cliente
	 * @param idCliente id do cliente
	 * @return resposta http
	 */
	@GET
	@Consumes(value = { MediaType.APPLICATION_JSON })
	@Produces(value = { MediaType.APPLICATION_JSON })
	@Path("/bolsas/cotacoes/{idCliente}")
	public Response getListaCotacao(
			@PathParam("idCliente") String idCliente
	) {
		try {
			return Response.ok().entity(acaoController.getListaCotacao(idCliente)).build(); 
		} catch (NoResultException nre) {
			return Response.status(Status.NOT_FOUND.getStatusCode()).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getStackTrace()).build(); 
		} 
	}
	
	/**
	 * tira uma ação da lista de cotações do cliente
	 * @param idCliente id do cliente
	 * @param idAcao id da ação
	 * @return resposta http
	 */
	@DELETE
	@Consumes(value = { MediaType.APPLICATION_JSON })
	@Produces(value = { MediaType.APPLICATION_JSON })
	@Path("/bolsas/cotacoes/{idCliente}/{idAcao}")
	public Response removerAcaoListaCotacao(
			@PathParam("idCliente") String idCliente, 
			@PathParam("idAcao") String idAcao
	) {
		try {
			acaoController.removerAcaoListaCotacao(idCliente, idAcao);
			return Response.ok().build(); 
		} catch (NoResultException nre) {
			return Response.status(Status.NOT_FOUND.getStatusCode()).build();
		} catch (DataFormatException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build(); 
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getStackTrace()).build(); 
		} 
	}
	
	/**
	 * sinaliza interesse nas notificações de limite atingido
	 * @param eventSink parametro da requisição
	 * @param sse parametro da requisição
	 * @param idCliente id do cliente
	 */
	@GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
	@Path("/bolsas/cotacoes/limites/{idCliente}/")
	public void notificacoesLimites(
			@Context SseEventSink eventSink, 
			@Context Sse sse,
			@PathParam("idCliente") String idCliente
	) {
		acaoController.notificacoesLimites(eventSink, sse, idCliente);
	}
	
	/**
	 * cadastra nova ordem de compra
	 * @param idCliente id do cliente
	 * @param ordem ordem de compra
	 * @return reposta http
	 */
	@POST
	@Consumes(value = { MediaType.APPLICATION_JSON })
	@Produces(value = { MediaType.APPLICATION_JSON })
	@Path("/bolsas/ordem-compra/{idCliente}")
	public Response ordemCompra(
			@PathParam("idCliente") String idCliente, 
			OrdemCriptografada ordem
	) {
		try {
			acaoController.adicionaOrdemCompra(new Ordem(idCliente, EncriptaDecriptaRSA.decrypt(ordem.getOrdem())));
			return Response.ok().build(); 
		} catch (NoResultException nre) {
			return Response.status(Status.NOT_FOUND.getStatusCode()).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getStackTrace()).build(); 
		} 
	}
	
	/**
	 * cadastra nova ordem de venda
	 * @param idCliente id do cliente
	 * @param ordem ordem de venda
	 * @return resposta http
	 */
	@POST
	@Consumes(value = { MediaType.APPLICATION_JSON })
	@Produces(value = { MediaType.APPLICATION_JSON })
	@Path("/bolsas/ordem-venda/{idCliente}")
	public Response ordemVenda(
			@PathParam("idCliente") String idCliente, 
			OrdemCriptografada ordem
	) {
		try {
			acaoController.adicionaOrdemVenda(new Ordem(idCliente, EncriptaDecriptaRSA.decrypt(ordem.getOrdem())));
			return Response.ok().build(); 
		} catch (NoResultException nre) {
			return Response.status(Status.NOT_FOUND.getStatusCode()).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getStackTrace()).build(); 
		} 
	}
	
	/**
	 * sinaliza interesse de receber notificações sobre ordens de compra e venda realizadas
	 * @param eventSink parametro da requisição
	 * @param sse parametro da requisição
	 * @param idCliente id do cliente
	 */
	@GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
	@Path("/bolsas/ordens/{idCliente}/")
	public void notificacoesOrdensCompraVenda(
			@Context SseEventSink eventSink, 
			@Context Sse sse,
			@PathParam("idCliente") String idCliente
	) {
		acaoController.notificacoesOrdensCompraVenda(eventSink, sse, idCliente);
	}
}
