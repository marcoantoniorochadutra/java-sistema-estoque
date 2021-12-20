package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.beans.property.StringProperty;

public class Fornecedor {

	private IntegerProperty id = new SimpleIntegerProperty(0);
	private StringProperty nome = new SimpleStringProperty("");
	private StringProperty email = new SimpleStringProperty("");
	private StringProperty cnpj = new SimpleStringProperty("");

	public String naString() {
		return "Fornecedor [id=" + id + ", nome=" + nome + ", email=" + email + ", cnpj=" + cnpj + "]";
	}

	@Override
	public String toString() {
		String nomeStr = nome.get();
		return nomeStr;
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

	public final StringProperty nomeProperty() {
		return this.nome;
	}

	public final String getNome() {
		return this.nomeProperty().get();
	}

	public final void setNome(final String nome) {
		this.nomeProperty().set(nome);
	}

	public final StringProperty emailProperty() {
		return this.email;
	}

	public final String getEmail() {
		return this.emailProperty().get();
	}

	public final void setEmail(final String email) {
		this.emailProperty().set(email);
	}

	public final StringProperty cnpjProperty() {
		return this.cnpj;
	}

	public final String getCnpj() {
		return this.cnpjProperty().get();
	}

	public final void setCnpj(final String cnpj) {
		this.cnpjProperty().set(cnpj);
	}

}
