package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Fornecedor;
import model.Produto;
import util.Conexao;
import util.Mensagens;

public class telaCadProdController {

	@FXML
	TextField txtNome;
	@FXML
	TextField txtValorCusto;
	@FXML
	TextField txtValorVenda;
	@FXML
	TextField txtNomeImagem;
	@FXML
	ColorPicker cpEscolheCor;
	@FXML
	ComboBox<String> cbFornecedor;
	private boolean verif = false;
	private Produto produtoSelecionado = null;

	@FXML
	private void cadastraProduto() {


		verifBranco();
		if (verif == true) {
			verif = false;
			return;
		}

		verifNumerico();

		if (verif == true) {
			verif = false;
			return;
		}

		verifIgual();
		if (verif == true) {
			verif = false;
			return;
		}
		verifValores();
		if (verif == true) {
			verif = false;
			return;
		}

		realizarCadastro();

	}

	private void realizarCadastro() {

		Produto p = lerTela();
		Fornecedor n = verifFornecedorCodigo();
		String sql = "insert into produto(prod_nome, vlr_custo, vlr_venda, fk_forn_id, prod_qtd, prod_vendas, prod_lucro) values (?,?,?,?,?,?,?)";

		try {

			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, p.getNome());
			ps.setDouble(2, p.getValorCusto());
			ps.setDouble(3, p.getValorVenda());
			ps.setInt(4, n.getId());			
			ps.setInt(5, 0);
			ps.setInt(6, 0);
			Double lucro = Math.floor((((p.getValorVenda() - p.getValorCusto()) / p.getValorVenda()) * 100));
			ps.setDouble(7, lucro);
			ps.executeUpdate();
			conn.close();

			Mensagens.msgInformacao("Produto Cadastrado.", "Você cadastrou um produto");
			telaEstatisticasController t = new telaEstatisticasController();
			t.verifAberto("fechado");
		} catch (SQLException e) {
			e.printStackTrace();

		}

	}

	@FXML
	private void escolherFornecedor() {
		String sql = "SELECT forn_nome, forn_id FROM fornecedor";
		ObservableList<String> list = FXCollections.observableArrayList();

		try {
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				list.add(rs.getString("forn_nome") + " (" + rs.getInt("forn_id") + ") ");

			}
			rs.close();
			conn.close();
			cbFornecedor.setItems(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void initialize() {
		escolherFornecedor();
	}

	@FXML
	private void voltar(ActionEvent actionevent) {

		Node n = (Node) actionevent.getSource();
		Stage stage = (Stage) n.getScene().getWindow();
		stage.close();
	
		
		telaEstatisticasController t = new telaEstatisticasController();
		t.verifAberto("fechado");
		
	}

	private Produto lerTela() {
		Fornecedor n = verifFornecedorCodigo();
		Produto p = new Produto();
		if (produtoSelecionado != null) {
			p.setId(produtoSelecionado.getId());
		}

		p.setNome(txtNome.getText());
		p.setValorCusto(Double.parseDouble(txtValorCusto.getText()));
		p.setValorVenda(Double.parseDouble(txtValorVenda.getText()));
		p.setFornecedorId(n.getId());

		return p;

	}

	private Fornecedor verifFornecedorCodigo() {

		Fornecedor n = new Fornecedor();
		String sql = "SELECT * FROM fornecedor where forn_nome like ? order by forn_id";
		
		String pegarNome = cbFornecedor.getValue();
		int localizar = pegarNome.indexOf("(");
		String substituir = "";
		String local = pegarNome.substring(localizar - 1);
		String nome = pegarNome.replace(local, substituir);

		try {

			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, nome);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				n.setId(rs.getInt("forn_id"));

			}
			rs.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return n;

	}

	private void verifNumerico() {

		try {
			@SuppressWarnings("unused")
			Double txtVlCusto = Double.parseDouble(txtValorCusto.getText());

		} catch (NumberFormatException e) {
			Mensagens.msgErro("Caractere Inválido ", "Valor de custo contém um caractere inválido");
			verif = true;
			return;
		}
		try {

			@SuppressWarnings("unused")
			Double txtVlVenda = Double.parseDouble(txtValorVenda.getText());

		} catch (NumberFormatException e) {
			Mensagens.msgErro("Caractere Inválido ", "Valor de venda contém um caractere inválido");
			verif = true;
			return;
		}
	}

	private void verifBranco() {
		if (txtNome.getText().isEmpty() || txtValorCusto.getText().isEmpty() || txtValorVenda.getText().isEmpty()
				|| cbFornecedor.getValue() == null) {
			Mensagens.msgInformacao("Campo em branco.", "Preencha todos os campos para realizar o cadastro.");
			verif = true;
			return;

		}
	}

	private void verifIgual() {

		Produto p = lerTela();
		String sql = "select * from produto order by prod_id ";

		try {
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				if (p.getNome().equalsIgnoreCase(rs.getString("prod_nome")) && rs.getInt("fk_forn_id") == p.getFornecedorId()) {
					Mensagens.msgErro("Produto já cadastrado", "Este produto já está cadastrado em nosso sistema com este fornecedor");
					conn.close();
					verif = true;
					return;

				}

			}
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void verifValores() {
		Double vlCusto, vlVenda;
		vlCusto = Double.parseDouble(txtValorCusto.getText());
		vlVenda = Double.parseDouble(txtValorVenda.getText());
		if (vlCusto > vlVenda) {
			Mensagens.msgErro("Valor Incorreto.", "O valor de custo é maior que o valor de venda.");
			verif = true;
			return;
		}

	}


}
