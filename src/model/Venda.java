package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Venda {

	private IntegerProperty id = new SimpleIntegerProperty(0);
	private IntegerProperty quantidade = new SimpleIntegerProperty(0);
	private StringProperty data = new SimpleStringProperty("");
	private IntegerProperty idProduto = new SimpleIntegerProperty(0);
	private StringProperty produtoNome = new SimpleStringProperty("");

	@Override
	public String toString() {
		return "Venda [id=" + id + ", quantidade=" + quantidade + ", data=" + data + "]";
	}

	public final IntegerProperty idProperty() {
		return this.id;
	}

	public final int getId() {
		return this.idProperty().get();
	}

	public final void setId(final int id) {
		this.idProperty().set(id);
	}

	public final IntegerProperty quantidadeProperty() {
		return this.quantidade;
	}

	public final int getQuantidade() {
		return this.quantidadeProperty().get();
	}

	public final void setQuantidade(final int quantidade) {
		this.quantidadeProperty().set(quantidade);
	}

	public final StringProperty dataProperty() {
		return this.data;
	}

	public final String getData() {
		return this.dataProperty().get();
	}

	public final void setData(final String data) {
		this.dataProperty().set(data);
	}

	public final IntegerProperty idProdutoProperty() {
		return this.idProduto;
	}

	public final int getIdProduto() {
		return this.idProdutoProperty().get();
	}

	public final void setIdProduto(final int idProduto) {
		this.idProdutoProperty().set(idProduto);
	}

	public final StringProperty produtoNomeProperty() {
		return this.produtoNome;
	}
	

	public final String getProdutoNome() {
		return this.produtoNomeProperty().get();
	}
	

	public final void setProdutoNome(final String produtoNome) {
		this.produtoNomeProperty().set(produtoNome);
	}
	



}
