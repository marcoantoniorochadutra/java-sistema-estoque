package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Produto;
import model.Venda;
import util.Conexao;
import util.Mensagens;

public class telaVendaController {

	@FXML
	Label lblID;
	@FXML
	TableView<Venda> tblVenda;
	@FXML
	TableColumn<Venda, Number> colId;
	@FXML
	TableColumn<Venda, Number> colQtd;
	@FXML
	TableColumn<Venda, String> colData;
	@FXML
	TableColumn<Venda, Number> colIdProduto;
	@FXML
	TableColumn<Venda, String> colNomeProduto;
	@FXML
	ComboBox<Produto> cbProdutos;
	@FXML
	TextField txtQtd;
	@FXML
	DatePicker dpData;

	private Venda vendaSelecionado = null;
	private int estoque;
	private char verif = 'N';

	@FXML
	public void cadastrar() {
		verifBranco();
		if (verif == 'S') {
			verif = 'N';
			return;
		}
		verifNumerico();
		if (verif == 'S') {
			verif = 'N';
			return;
		}

		verifEstoque();
		int qtd = Integer.parseInt(txtQtd.getText());
		if (estoque < qtd) {
			Mensagens.msgErro("Estoque Inválido", "O valor informado é maior que o disponível");
			return;
		}

		realizarCadastro();

	}

	@FXML

	private void verifBranco() {

		if (txtQtd.getText().isEmpty() || dpData.getValue() == null || cbProdutos.getValue() == null) {
			Mensagens.msgInformacao("Campo em branco.", "Preencha todos os campos para realizar o cadastro.");
			verif = 'S';
			return;

		}

	}

	private void verifEstoque() {
		String sql = "SELECT prod_qtd FROM produto where prod_nome = ? and prod_id = ?; ";

		try {
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, cbProdutos.getValue().getNome());
			ps.setInt(2, cbProdutos.getValue().getId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				estoque = rs.getInt("prod_qtd");

			}
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	private void verifNumerico() {

		try {
			@SuppressWarnings("unused")
			int qtd = Integer.parseInt(txtQtd.getText());

		} catch (NumberFormatException e) {
			Mensagens.msgErro("Caractere Inválido ", "A quantidade contém um caractere inválido");
			verif = 'S';
			return;
		}

	}

	private void realizarCadastro() {

		Venda v = lerTela();
		String sql = "insert into venda(vend_data, vend_qtd,vend_nome,fk_prod_id) values (?,?,?,?)";

		try {

			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, v.getData());
			ps.setInt(2, v.getQuantidade());
			ps.setString(3, cbProdutos.getValue().getNome());
			ps.setInt(4, cbProdutos.getValue().getId());

			ps.executeUpdate();
			conn.close();

			controleEstoque();
			preencheVendas();
			Mensagens.msgInformacao("Produto Cadastrado.", "Você cadastrou um produto");

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	private void controleEstoque() {

		try {

			String sql = "UPDATE produto SET prod_qtd = prod_qtd - ?, prod_vendas = prod_vendas + ? WHERE prod_id = ?";
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(txtQtd.getText()));
			ps.setInt(2, Integer.parseInt(txtQtd.getText()));
			ps.setInt(3, cbProdutos.getValue().getId());

			ps.executeUpdate();
			conn.close();

		} catch (NumberFormatException e) {
			e.printStackTrace();
			Mensagens.msgErro("Caractere Inválido", "Para cadastrar preencha o campo apenas com números");
		} catch (SQLException e) {
			return;
		}

	}

	@FXML
	private void apagarVenda() {
		
		if(ButtonBar.ButtonData.CANCEL_CLOSE == Mensagens.msgOkCancel("Apagar Venda", "Você tem certeza que deseja apagar esta venda?").getButtonData()) {
			return;
		}
		addEstoque();
		String sql = "delete from venda where vend_id=?";
		try {
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, vendaSelecionado.getId());
			ps.executeUpdate();
			conn.close();
			preencheVendas();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addEstoque() {

		try {
			String sql = "update produto set prod_qtd = prod_qtd + ?, prod_vendas = prod_vendas - ? where prod_id = ?";
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, vendaSelecionado.getQuantidade());
			ps.setInt(2, vendaSelecionado.getQuantidade());
			ps.setInt(3, vendaSelecionado.getIdProduto());

			ps.executeUpdate();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void initialize() {

		preencheVendas();
		preencherCbProdutos();
		colId.setCellValueFactory(cellData -> cellData.getValue().idProperty());
		colData.setCellValueFactory(cellData -> cellData.getValue().dataProperty());
		colQtd.setCellValueFactory(cellData -> cellData.getValue().quantidadeProperty());
		colIdProduto.setCellValueFactory(cellData -> cellData.getValue().idProdutoProperty());
		colNomeProduto.setCellValueFactory(cellData -> cellData.getValue().produtoNomeProperty());

	}

	@FXML
	private void preencherCbProdutos() {
		String sql = "SELECT * FROM produto order by prod_id";
		ArrayList<Produto> produtos = new ArrayList<Produto>();

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
				p.setQtdVendas(rs.getInt("prod_vendas"));
				p.setFornecedorId(rs.getInt("fK_forn_id"));
				p.setProdLucro(rs.getInt("prod_lucro"));
				produtos.add(p);

			}
			conn.close();
			cbProdutos.setItems(FXCollections.observableArrayList(produtos));

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void preencheVendas() {

		ArrayList<Venda> vendas = new ArrayList<Venda>();
		String sql = "select * from venda order by vend_id ";

		try {
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Venda v = new Venda();
				v.setId(rs.getInt("vend_id"));
				v.setData(rs.getString("vend_data"));
				v.setQuantidade(rs.getInt("vend_qtd"));
				v.setIdProduto(rs.getInt("fk_prod_id"));
				v.setProdutoNome(rs.getString("vend_nome"));

				vendas.add(v);

			}
			rs.close();
			conn.close();
			tblVenda.setItems(FXCollections.observableArrayList(vendas));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Venda lerTela() {
		Venda v = new Venda();
		if (vendaSelecionado != null) {
			v.setId(vendaSelecionado.getId());
		}
		LocalDate data = dpData.getValue();
		String dataFormatar = data.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		v.setData(dataFormatar);
		v.setQuantidade(Integer.parseInt(txtQtd.getText()));

		return v;

	}

	@FXML
	private void selecionaProduto() {
		if (tblVenda.getSelectionModel().getSelectedItem() != null) {
			vendaSelecionado = tblVenda.getSelectionModel().getSelectedItem();


		}
	}
}
