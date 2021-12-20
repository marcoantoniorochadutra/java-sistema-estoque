package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import util.Conexao;

public class PrincipalController {
	@FXML
	private BorderPane bp;
	@FXML
	private AnchorPane ap;
	@FXML
	Label lblLucro;
	@FXML
	Label lblTotal;
	@FXML
	Label lblQtdVendas;

	@FXML
	void telaInicial(ActionEvent event) {
		bp.setCenter(ap);
		initialize();
	}

	@FXML
	private void mudarCadastroFornecedor(ActionEvent event) {
		loadPage("telaFornecedor");

	}

	@FXML
	private void mudarEstatisticas(ActionEvent event) {
		loadPage("telaEstatisticas");

	}

	@FXML
	private void mudarVenda(ActionEvent event) {
		loadPage("telaVenda");
	}

	
	@FXML
	private void mudarConfig(ActionEvent event) {
		loadPage("telaConfig");
	}

	public void loadPage(String page) {
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource(page + ".fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		bp.setCenter(root);
	}

	@FXML
	public void initialize() {
		vlTotal();
		vlLucro();
		totalVendas();
	}

	private void vlTotal() {
		Double vlTotal = 0.0;
		String sql = "select * from produto order by prod_id ";

		try {
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				vlTotal += (rs.getDouble("vlr_venda") * rs.getInt("prod_vendas"));

			}
			String vlT = vlTotal + "";
			lblTotal.setText(vlT);
			rs.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void vlLucro() {
		Double vlLucro = 0.0;
		String sql = "select * from produto order by prod_id ";

		try {
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				vlLucro += (rs.getDouble("vlr_venda") * rs.getInt("prod_vendas")
						- (rs.getDouble("vlr_custo") * rs.getInt("prod_vendas")));

			}
			String vlL = vlLucro + "";
			lblLucro.setText(vlL);
			rs.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void totalVendas() {
		int totalVendas = 0;
		String sql = "select * from produto order by prod_id ";

		try {
			Connection conn = Conexao.conectarSQL();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				totalVendas = totalVendas + rs.getInt("prod_vendas");

			}
			String totalV = totalVendas + "";

			lblQtdVendas.setText(totalV);
			rs.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
