package com.rodasoft.recursos.humanos.controlller;

import com.rodasoft.recursos.humanos.dto.UsuarioDTO;
import com.rodasoft.recursos.humanos.exception.AuthException;
import com.rodasoft.recursos.humanos.exception.RepositoryException;
import com.rodasoft.recursos.humanos.exception.ValidacionException;
import com.rodasoft.recursos.humanos.service.AuthService;
import com.rodasoft.recursos.humanos.util.Alertas;
import com.rodasoft.recursos.humanos.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    private final AuthService authService;
    private final SceneManager sceneManager;

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;

    public LoginController(AuthService authService, SceneManager sceneManager) {
        this.authService = authService;
        this.sceneManager = sceneManager;
    }

    @FXML
    private void onIngresar() {
        try {
            UsuarioDTO usuario = authService.iniciarSesion(txtUsuario.getText(), txtContrasena.getText());
            sceneManager.cambiarVista("Inventario.fxml", "Inventario de usuarios - " + usuario.nombreUsuario()
                    + " (" + usuario.rol() + ")");
        } catch (AuthException | ValidacionException | RepositoryException e) {
            Alertas.error(e.getMessage());
        }
    }

    @FXML
    private void onIrARegistro() {
        sceneManager.cambiarVista("Registro.fxml", "Registro");
    }
}
 

