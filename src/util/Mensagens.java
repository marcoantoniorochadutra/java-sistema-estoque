package util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

public class Mensagens {
	
	private static Alert msg(String cabecalho, String msg, AlertType tp){
		  Alert alert = new Alert(tp);
		  alert.setHeaderText(cabecalho);
		  alert.setContentText(msg);
		  alert.initStyle(StageStyle.UNDECORATED);
		  alert.getDialogPane().setStyle("-fx-border-color: #3a3d49; -fx-border-width: 1;");
		  return alert;
	}
	public static void msgErro(String cabecalho, String msg){
		msg(cabecalho, msg, AlertType.ERROR).showAndWait();
	}
	public static void msgAviso(String cabecalho, String msg){
		msg(cabecalho, msg, AlertType.WARNING).showAndWait();
	}
	public static void msgInformacao(String cabecalho, String msg){
		msg(cabecalho, msg, AlertType.INFORMATION).showAndWait();
	}
	public static void msgConfirmacao(String cabecalho, String msg){
		msg(cabecalho, msg, AlertType.CONFIRMATION).showAndWait();
	}
	public static ButtonType msgOkCancel(String cabecalho, String msg){
		Optional<ButtonType> r = msg(cabecalho, msg, AlertType.CONFIRMATION).showAndWait();
		return r.get();
	}

}