package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Produto;
import util.Conexao;
import util.Mensagens;

public class telaEstatisticasController implements Initializable {
	@FXML
	TextField txtAdicao;
	@FXML
	TextField txtPesquisa;
	@FXML
	TableView<Produto> tblEstoque;
	@FXML
	TableColumn<Produto, Number> colId;
	@FXML
	TableColumn<Produto, String> colNome;
	@FXML
	TableColumn<Produto, Number> colVlrCusto;
	@FXML
	TableColumn<Produto, Number> colVlrVenda;
	@FXML
	TableColumn<Produto, Number> colQtdVenda;
	@FXML
	TableColumn<Produto, Number> colQuantidade;
	@FXML
	TableColumn<Produto, Number> colLucro;
	@FXML
	TableColumn<Produto, Number> colCodFornecedor;

	public ArrayList<Produto> produtos = new ArrayList<Produto>();
	private Produto produtoSelecionado = null;
	private static String status;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		colId.setCellValueFactory(cellData -> cellData.getValue().idProperty());
		colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
		colVlrCusto.setCellValueFactory(cellData -> cellData.getValue().valorCustoProperty());
		colVlrVenda.setCellValueFactory(cellData -> cellData.getValue().valorVendaProperty());
		colQuantidade.setCellValueFactory(cellData -> cellData.getValue().quantidadeProperty());
		colCodFornecedor.setCellValueFactory(cellData -> cellData.getValue().fornecedorIdProperty());
		colQtdVenda.setCellValueFactory(cellData -> cellData.getValue().qtdVendasProperty());
		colLucro.setCellValueFactory(cellData -> cellData.getValue().prodLucroProperty());

		buscaProdutos();

	}

	public void buscaProdutos() {

		produtos = new ArrayList<Produto>();
		String sql = "select * from produto order by prod_id ";

		try {
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Produto p = new Produto();
				p.setId(rs.getInt("prod_id"));
				p.setNome(rs.getString("prod_nome"));
				p.setValorCusto(rs.getDouble("vlr_custo"));
				p.setValorVenda(rs.getDouble("vlr_venda"));
				p.setQuantidade(rs.getInt("prod_qtd"));
				p.setFornecedorId(rs.getInt("fk_forn_id"));
				p.setQtdVendas(rs.getInt("prod_vendas"));
				p.setProdLucro(rs.getDouble("prod_lucro"));
				p.setQtdVendas(rs.getInt("prod_vendas"));

				produtos.add(p);

			}
			rs.close();
			conn.close();
			tblEstoque.setItems(FXCollections.observableArrayList(produtos));
			colQtdVenda.setVisible(false);
			colCodFornecedor.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@FXML
	private void abrirCadastro(ActionEvent event) {
	

		try {

			if (status != "aberto") {
				FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("telaCadProd.fxml"));
				Parent root = (Parent) fxmlloader.load();
				Stage stage = new Stage();
				stage.initStyle(StageStyle.UNDECORATED);
				stage.setScene(new Scene(root));
				stage.show();
				buscaProdutos();
				verifAberto("aberto");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void verifAberto(String aberto) {
		String on = "aberto", off = "fechado";

		if (aberto == on) {
			status = "aberto";

		} else if (aberto == off) {

			status = "fechado";

		}

	}

	@FXML
	private void selecionaProduto() {
		if (tblEstoque.getSelectionModel().getSelectedItem() != null) {
			produtoSelecionado = tblEstoque.getSelectionModel().getSelectedItem();

		}
	}

	@FXML
	private void adicionarEstoque() {
		if(ButtonBar.ButtonData.CANCEL_CLOSE == Mensagens.msgOkCancel("Adicionar Estoque", "Você tem certeza que deseja adicionar estoque a este produto?").getButtonData()) {
			return;
		}
		
		try {

			String sql = "update produto set prod_qtd = prod_qtd + ? where prod_id = ?";
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(txtAdicao.getText()));
			ps.setInt(2, produtoSelecionado.getId());

			ps.executeUpdate();
			conn.close();
			buscaProdutos();

		} catch (NumberFormatException e) {
			e.printStackTrace();
			Mensagens.msgErro("Caractere Inválido", "Para cadastrar preencha o campo apenas com números");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void pesquisarProdutos() {
		produtos = new ArrayList<Produto>();
		String sql = "select * from produto where prod_nome like ? order by prod_id ";
		try {
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, txtPesquisa.getText() + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Produto p = new Produto();
				p.setId(rs.getInt("prod_id"));
				p.setNome(rs.getString("prod_nome"));
				p.setValorCusto(rs.getDouble("vlr_custo"));
				p.setValorVenda(rs.getDouble("vlr_venda"));
				p.setQuantidade(rs.getInt("prod_qtd"));
				p.setFornecedorId(rs.getInt("fk_forn_id"));
				p.setQtdVendas(rs.getInt("prod_vendas"));
				p.setProdLucro(rs.getDouble("prod_lucro"));
				p.setQtdVendas(rs.getInt("prod_vendas"));
				produtos.add(p);
			}
			rs.close();
			conn.close();
			tblEstoque.setItems(FXCollections.observableArrayList(produtos));

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@FXML
	private void apagarProduto() {
		if(ButtonBar.ButtonData.CANCEL_CLOSE == Mensagens.msgOkCancel("Apagar Produto", "Você tem certeza que deseja apagar este produto?").getButtonData()) {
			return;
		}
		String sql = "delete from produto where prod_id=?";
		try {
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, produtoSelecionado.getId());
			ps.executeUpdate();
			conn.close();
			buscaProdutos();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void maisVendido() {
		buscaProdutos();

		colQtdVenda.setVisible(true);
		colLucro.setVisible(false);
		colCodFornecedor.setVisible(false);

		colQtdVenda.setSortType(TableColumn.SortType.DESCENDING);
		tblEstoque.getSortOrder().add(colQtdVenda);
		tblEstoque.sort();

	}

	public void maisLucro() {
		buscaProdutos();

		colLucro.setVisible(true);
		colQtdVenda.setVisible(false);
		colCodFornecedor.setVisible(false);

		colLucro.setSortType(TableColumn.SortType.DESCENDING);
		tblEstoque.getSortOrder().add(colLucro);
		tblEstoque.sort();

	}

}
