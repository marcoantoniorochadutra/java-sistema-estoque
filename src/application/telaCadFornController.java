package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.text.MaskFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Fornecedor;
import util.Conexao;
import util.Mensagens;

public class telaCadFornController {

	@FXML
	TextField txtNome;
	@FXML
	TextField txtEmail;
	@FXML
	TextField txtCNPJ;
	 

	public ArrayList<Fornecedor> fornecedores = new ArrayList<Fornecedor>();

	private Fornecedor fornecedorSelecionado = null;

	@FXML
	private void cadastraFornecedor() {
		if (txtCNPJ.getText().length() != 14) {
			Mensagens.msgErro("CNPJ Inválido",
					"O CNPJ cadastrado está incorreto.\nEle deve ter 14 caracteres numéricos e não conter pontos.");
			return;
		}

		Fornecedor u = lerTela();
		String sql = "insert into fornecedor(forn_nome, forn_email, forn_cnpj) values (?,?,?)";
		if (txtNome.getText() == "" || txtEmail.getText() == "" || txtCNPJ.getText() == "") {
			Mensagens.msgInformacao("Campo em branco.", "Preencha todos os campos para realizar o cadastro.");

			return;
		}

		try {

			verifIgual();
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, u.getNome());
			ps.setString(2, u.getEmail());
			ps.setString(3, u.getCnpj());
			ps.executeUpdate();
			conn.close();
			Mensagens.msgInformacao("Fornecedor Cadastrado.", "Você cadastrou um fornecedor");

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	private Fornecedor lerTela() {
		Fornecedor u = new Fornecedor();
		if (fornecedorSelecionado != null) {
			u.setId(fornecedorSelecionado.getId());
		}

		u.setNome(txtNome.getText());
		u.setEmail(txtEmail.getText());
		try {
			String texto = txtCNPJ.getText();
			MaskFormatter mf = new MaskFormatter("##.###.###/####-##");
			mf.setValueContainsLiteralCharacters(false);
			u.setCnpj(mf.valueToString(texto));

		}

		catch (Exception e) {
			e.printStackTrace();

		}

		return u;

	}

	private void verifIgual() {

		Fornecedor u = lerTela();
		String sql = "select * from fornecedor order by forn_id ";

		try {
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if (u.getNome().equals(rs.getString("forn_nome"))) {
					Mensagens.msgErro("Nome já cadastrado.", "O nome cadastrado já está cadastrado.");
					conn.close();
					return;
				} else if (u.getEmail().equals(rs.getString("forn_email"))) {
					Mensagens.msgErro("E-mail já cadastrado.", "O e-mail cadastrado já está cadastrado.");
					conn.close();
					return;

				} else if (u.getCnpj().equals(rs.getString("forn_cnpj"))) {
					Mensagens.msgErro("CNPJ já cadastrado.", "O CNPJ cadastrado já está cadastrado.");
					conn.close();
					return;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}


	@FXML
	private void voltar(ActionEvent actionevent) {

		Node n = (Node) actionevent.getSource();
		Stage stage = (Stage) n.getScene().getWindow();
		stage.close();

		telaFornecedorController t = new telaFornecedorController();
		t.verifAberto("fechado");

	}


	


}
