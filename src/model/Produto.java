package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Produto {

	private IntegerProperty id = new SimpleIntegerProperty(0);
	private StringProperty nome = new SimpleStringProperty("");
	private IntegerProperty quantidade = new SimpleIntegerProperty(0);
	private DoubleProperty valorCusto = new SimpleDoubleProperty(0);
	private DoubleProperty valorVenda = new SimpleDoubleProperty(0);
	private IntegerProperty qtdVendas = new SimpleIntegerProperty(0);
	private DoubleProperty prodLucro = new SimpleDoubleProperty(0);

	private IntegerProperty fornecedorId = new SimpleIntegerProperty(0);


	

	public final IntegerProperty idProperty() {
		return this.id;
	}

	public final int getId() {
		return this.idProperty().get();
	}

	public final void setId(final int id) {
		this.idProperty().set(id);
	}

	public final StringProperty nomeProperty() {
		return this.nome;
	}

	public final String getNome() {
		return this.nomeProperty().get();
	}

	public final void setNome(final String nome) {
		this.nomeProperty().set(nome);
	}

	public final DoubleProperty valorVendaProperty() {
		return this.valorVenda;
	}

	public final double getValorVenda() {
		return this.valorVendaProperty().get();
	}

	public final void setValorVenda(final double valorVenda) {
		this.valorVendaProperty().set(valorVenda);
	}

	public final IntegerProperty fornecedorIdProperty() {
		return this.fornecedorId;
	}

	public final int getFornecedorId() {
		return this.fornecedorIdProperty().get();
	}

	@Override
	public String toString() {
		return getNome() +  " (" + getFornecedorId() + ") ";
	}

	public final void setFornecedorId(final int fornecedorId) {
		this.fornecedorIdProperty().set(fornecedorId);
	}

	public final DoubleProperty valorCustoProperty() {
		return this.valorCusto;
	}

	public final double getValorCusto() {
		return this.valorCustoProperty().get();
	}

	public final void setValorCusto(final double valorCusto) {
		this.valorCustoProperty().set(valorCusto);
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

	public final IntegerProperty qtdVendasProperty() {
		return this.qtdVendas;
	}
	

	public final int getQtdVendas() {
		return this.qtdVendasProperty().get();
	}
	

	public final void setQtdVendas(final int qtdVendas) {
		this.qtdVendasProperty().set(qtdVendas);
	}

	public final DoubleProperty prodLucroProperty() {
		return this.prodLucro;
	}
	

	public final double getProdLucro() {
		return this.prodLucroProperty().get();
	}
	

	public final void setProdLucro(final double prodLucro) {
		this.prodLucroProperty().set(prodLucro);
	}
	
	
	

}
