package application;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javafx.fxml.FXML;
import util.Conexao;

public class telaConfigController {
	
	@FXML
	private void apagarFornecedor() {

		String sql = "delete from fornecedor";
		
		try {
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);;
			ps.executeUpdate();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	@FXML
	private void apagarProdutos() {

		String sql = "delete from produto";

		try {
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);;
			ps.executeUpdate();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	@FXML
	private void apagarVendas() {

		String sql = "delete from venda";

		try {
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);;
			ps.executeUpdate();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
