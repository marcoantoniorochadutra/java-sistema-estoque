package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Fornecedor;
import util.Conexao;
import util.Mensagens;

public class telaFornecedorController {
	@FXML
	TextField txtPesquisa;
	@FXML
	TableView<Fornecedor> tblFornecedores;
	@FXML
	TableColumn<Fornecedor, Number> colId;
	@FXML
	TableColumn<Fornecedor, String> colNome;
	@FXML
	TableColumn<Fornecedor, String> colEmail;
	@FXML
	TableColumn<Fornecedor, String> colCnpj;

	public ArrayList<Fornecedor> fornecedores = new ArrayList<Fornecedor>();

	private Fornecedor fornecedorSelecionado = null;
	private static String status;

	@FXML
	private void apagarFornecedor() {
		String sql = "delete from fornecedor where forn_id=?";

		if(ButtonBar.ButtonData.CANCEL_CLOSE == Mensagens.msgOkCancel("Apagar Fornecedor", "Você tem certeza que deseja apagar este fornecedor?").getButtonData()) {
			return;
		}
		
		try {
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, fornecedorSelecionado.getId());
			ps.executeUpdate();
			conn.close();
			buscaFornecedores();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void selecionaFornecedor() {
		if (tblFornecedores.getSelectionModel().getSelectedItem() != null) {
			fornecedorSelecionado = tblFornecedores.getSelectionModel().getSelectedItem();

		}
	}

	@FXML
	private void abrirCadastro(ActionEvent event) {

		try {
			if (status != "aberto") {
				FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("telaCadForn.fxml"));
				Parent root = (Parent) fxmlloader.load();
				Stage stage = new Stage();
				stage.initStyle(StageStyle.UNDECORATED);
				stage.setScene(new Scene(root));
				stage.show();
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
	private void voltar(ActionEvent actionevent) {

		Node n = (Node) actionevent.getSource();
		Stage stage = (Stage) n.getScene().getWindow();
		stage.close();

		verifAberto("fechado");

	}

	@FXML
	public void initialize() {
		colId.setCellValueFactory(cellData -> cellData.getValue().idProperty());
		colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
		colEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
		colCnpj.setCellValueFactory(cellData -> cellData.getValue().cnpjProperty());
		buscaFornecedores();

	}

	public void buscaFornecedores() {

		fornecedores = new ArrayList<Fornecedor>();
		String sql = "select * from fornecedor order by forn_id ";

		try {
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Fornecedor f = new Fornecedor();
				f.setId(rs.getInt("forn_id"));
				f.setNome(rs.getString("forn_nome"));
				f.setEmail(rs.getString("forn_email"));
				f.setCnpj(rs.getString("forn_cnpj"));

				fornecedores.add(f);

			}
			rs.close();
			conn.close();
			tblFornecedores.setItems(FXCollections.observableArrayList(fornecedores));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void pesquisarFornecedor() {
		fornecedores = new ArrayList<Fornecedor>();
		String sql = "select * from fornecedor where forn_nome like ? order by forn_id ";
		try {
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, txtPesquisa.getText());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Fornecedor f = new Fornecedor();
				f.setId(rs.getInt("prod_id"));
				f.setNome(rs.getString("prod_nome"));
				f.setCnpj(rs.getString("prod_cnpj"));
				f.setEmail(rs.getString("prod_email"));

				fornecedores.add(f);
			}
			rs.close();
			conn.close();
			tblFornecedores.setItems(FXCollections.observableArrayList(fornecedores));

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
