package com.devmaker.siftkin.webservice.retrofit;

import com.devmaker.siftkin.webservice.model.ModelEnvio.CadastroModel.CadastroModel;
import com.devmaker.siftkin.webservice.model.request.AdicionarProdutoCarrinhoRequest;
import com.devmaker.siftkin.webservice.model.request.AlterarDadosRequest;
import com.devmaker.siftkin.webservice.model.request.AlterarSenhaRequest;
import com.devmaker.siftkin.webservice.model.request.AvaliacaoComentarioRequest;
import com.devmaker.siftkin.webservice.model.request.AvaliacoesDoProdutoRequest;
import com.devmaker.siftkin.webservice.model.request.AvaliarEmpresaRequest;
import com.devmaker.siftkin.webservice.model.request.AvaliarProdutoRequest;
import com.devmaker.siftkin.webservice.model.request.BuscaGeralRequest;
import com.devmaker.siftkin.webservice.model.request.BuscaProdutosDestaqueRequest;
import com.devmaker.siftkin.webservice.model.request.BuscaSugestaoRequest;
import com.devmaker.siftkin.webservice.model.request.BuscarProdutosDetalhesRequest;
import com.devmaker.siftkin.webservice.model.request.BuscarProdutosRequest;
import com.devmaker.siftkin.webservice.model.request.CadastroRequest;
import com.devmaker.siftkin.webservice.model.request.CepResponse;
import com.devmaker.siftkin.webservice.model.request.CodigoDeIndicacaoRequest;
import com.devmaker.siftkin.webservice.model.request.ConfirmarPedidoRequest;
import com.devmaker.siftkin.webservice.model.request.ConfirmarPedidoVendedorRequest;
import com.devmaker.siftkin.webservice.model.request.DeletarMeusPedidosRequest;
import com.devmaker.siftkin.webservice.model.request.EmpresaDetalheRequest;
import com.devmaker.siftkin.webservice.model.request.EmpresasCategoriaRequest;
import com.devmaker.siftkin.webservice.model.request.EmpresasDistanciaRequest;
import com.devmaker.siftkin.webservice.model.request.FaleConoscoRequest;
import com.devmaker.siftkin.webservice.model.request.GetBannersRequest;
import com.devmaker.siftkin.webservice.model.request.GetCategoriasRequest;
import com.devmaker.siftkin.webservice.model.request.GetCidadesRequest;
import com.devmaker.siftkin.webservice.model.request.GetEstadosRequest;
import com.devmaker.siftkin.webservice.model.request.GetPerfilRequest;
import com.devmaker.siftkin.webservice.model.request.GetSubcategoriasRequest;
import com.devmaker.siftkin.webservice.model.request.HistoricoPedidoDetalheRequest;
import com.devmaker.siftkin.webservice.model.request.HistoricoPedidosRequest;
import com.devmaker.siftkin.webservice.model.request.InstrucoesDeUsoRequest;
import com.devmaker.siftkin.webservice.model.request.ListarPremiosRequest;
import com.devmaker.siftkin.webservice.model.request.LoginRequest;
import com.devmaker.siftkin.webservice.model.request.MeusJogosDetalheRequest;
import com.devmaker.siftkin.webservice.model.request.MeusJogosRequest;
import com.devmaker.siftkin.webservice.model.request.ProdutosEstabelecimentoRequest;
import com.devmaker.siftkin.webservice.model.request.ProdutosLojaRequest;
import com.devmaker.siftkin.webservice.model.request.ProximoSorteioRequest;
import com.devmaker.siftkin.webservice.model.request.RealizarApostaRequest;
import com.devmaker.siftkin.webservice.model.request.RecuperarSenhaRequest;
import com.devmaker.siftkin.webservice.model.request.UploadImagemRequest;
import com.devmaker.siftkin.webservice.model.request.VisualizarItemCarrinhoDetalheRequest;
import com.devmaker.siftkin.webservice.model.request.VisualizarItensCarrinhoRequest;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RequestInterfaceUser {

    //--------------------------------------------PUT---------------------------------------------//

    @PUT("cliente/perfil-alterar-senha")
    Call<AlterarSenhaRequest> editarSenha(@Body Map<String, String> param);

    @PUT("cliente/perfil-editar")
    Call<AlterarDadosRequest> alterarDados(@Body CadastroModel cadastroModel);

    @PUT("cliente/pedido-carrinho-concluir-compra")
    Call<ConfirmarPedidoRequest> confirmarPedido(@Body Map<String,String> codigo);

    @PUT("cliente/pedido-confirmar-codigo")
    Call<ConfirmarPedidoVendedorRequest> confirmarPedidoVendedor(@Body Map<String,String> codigo);

    //-------------------------------------------POST----------------------------------------------//

    @POST("cliente/login")
    Call<LoginRequest> login(@Body Map<String, String> param);

    @Multipart
    @POST("utils/uploadTmp")
    Call<UploadImagemRequest> uploadTemp(@Part MultipartBody.Part upload, @Part("extensao") RequestBody extensao);

    @POST("cliente/cadastro")
    Call<CadastroRequest> cadastrar(@Body CadastroModel cadastroModel);

    @POST("cliente/pedido-avaliar-empresa")
    Call<AvaliarEmpresaRequest> avaliarEmpresa(@Body Map<String, String> param);

    @POST("cliente/pedido-avaliar-produto")
    Call<AvaliarProdutoRequest> avaliarProduto(@Body Map<String, String> param);

    @POST("cliente/pedido-carrinho-adicionar")
    Call<AdicionarProdutoCarrinhoRequest> adicionarProdutoCarrinho(@Body Map<String, Integer> param);

    @POST("cliente/esqueceu-senha")
    Call<RecuperarSenhaRequest> recuperarSenha(@Body Map<String, String> param);

    @POST("cliente/produtos-buscar")
    Call<BuscarProdutosRequest> getProdutosDigitar(@Body Map<String, String> param);

    @POST("cliente/realizar-aposta")
    Call<RealizarApostaRequest> realizarAposta(@Body Map<String, String> param);

    @POST("cliente/produtos-buscar?limit=10")
    Call<BuscaGeralRequest> buscaGeral(@Body Map<String, Object> param,@Query("page") int page);

    @POST("cliente/fale-conosco")
    Call<FaleConoscoRequest> faleConosco(@Body Map<String, String> param);

    @POST("cliente/salvar-indicacao")
    Call<CodigoDeIndicacaoRequest> codigoIndicacao(@Body Map<String, String> param);

    //------------------------------------------GET-----------------------------------------------//

    @GET("cliente/categorias?")
    Call<GetCategoriasRequest> getCategorias();

    @GET("cliente/subcategorias/{id}?")
    Call<GetSubcategoriasRequest> getSubCategorias(@Path("id")String id);

    @GET("cliente/pedidos-carrinho")
    Call<VisualizarItensCarrinhoRequest> getCarrinhoProduto();

    @GET("cliente/pedido-carrinho-detalhes/{id}")
    Call<VisualizarItemCarrinhoDetalheRequest> getCarrinhoProdutoDetalhe(@Path("id") int id);

    @GET("cliente/produtos")
    Call<BuscarProdutosRequest> getProdutos();

    @GET("cliente/produto/{id}")
    Call<BuscarProdutosDetalhesRequest> getProdutosDetalhes(@Path("id") int id);

    @GET("cliente/produto/avaliacao-resumo/{id}")
    Call<AvaliacoesDoProdutoRequest> getProdutosAvaliacoes(@Path("id") int id);

    @GET("cliente/produto/avaliacao-comentarios/{id}")
    Call<AvaliacaoComentarioRequest> getAvaliacoesComentarios(@Path("id") int id);

    @GET("cliente/perfil")
    Call<GetPerfilRequest> getPerfil();

    @GET("cliente/pedidos?limit=20&desc=true")
    Call<HistoricoPedidosRequest> getHistoricoPedidos(@Query("page") int page);

    @GET("cliente/pedido-detalhes/{id}")
    Call<HistoricoPedidoDetalheRequest> getHistoricoPedidosDetalhe(@Path("id") int id);

    @GET("utils/estados")
    Call<GetEstadosRequest> getEstados();

    @GET("utils/estados/{id}")
    Call<GetCidadesRequest> getCidades(@Path("id") int id);

    @GET("cliente/empresas/categoria/{id}?limit=10")
    Call<EmpresasCategoriaRequest> getEmpresasPorCategoria(@Path("id") String id, @Query("raio") String raio, @Query("latitude") String latitude, @Query("longitude") String longitude, @Query("page") int page);

    @GET("cliente/empresa/{id}")
    Call<EmpresaDetalheRequest> getEmpresaDetalhe(@Path("id") String id);

    @GET("cliente/proximo-sorteio")
    Call<ProximoSorteioRequest> getProximoSorteio();

    @GET("cliente/meus-sorteios?desc=true")
    Call<MeusJogosRequest> getMeusJogos();

    @GET("cliente/sorteio-detalhes/{id}")
    Call<MeusJogosDetalheRequest> getMeusJogosDetalhe(@Path("id")String id);

    @GET("cliente/premios")
    Call<ListarPremiosRequest> getPremios();

    @GET("cliente/empresa-cat-e-prod")
    Call<ProdutosLojaRequest> getProdutosEmpresa(@Query("categoria_id") String idcategoria, @Query("empresa_id") String idempresa);

    @GET("cliente/instrucoes-uso")
    Call<InstrucoesDeUsoRequest> getInstrucoesDeUso();

    @GET("cliente/banner?")
    Call<GetBannersRequest> getBanners(@Query("categoria_id") String idcategoria, @Query("limit") String limit,@Query("order") String order);

    @GET("cliente/produtos-destaque")
    Call<BuscaProdutosDestaqueRequest> getProdutosDestaque(@Query("categoria_id") String idcategoria, @Query("subcategoria_id") String subcategoria_id, @Query("por") String por);

    @GET("cliente/produtos-buscar-sugestao?")
    Call<BuscaSugestaoRequest> getBuscaSugestao(@Query("por") String por);

    @GET("cliente/empresa-cat-e-prod-list?limit=10")
    Call<ProdutosEstabelecimentoRequest> getProdutosEstabelecimento(@Query("categoria_id") String categoriaId, @Query("empresa_id") String empresaId, @Query("page") int page);

    @GET("{cep}/json/ ")
    Call<CepResponse> cep(@Path("cep") String cep);

    //------------------------------------------DELETE-----------------------------------------------//

    @DELETE("cliente/pedido-carrinho-remover-pedido/{codigo}")
    Call<DeletarMeusPedidosRequest> deletarMeusPedidos(@Path("codigo") String codigo);



}