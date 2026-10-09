package com.rodasoft.recursos.humanos.controlller;

import com.rodasoft.recursos.humanos.dto.RegistroDTO;
import com.rodasoft.recursos.humanos.dto.RolDTO;
import com.rodasoft.recursos.humanos.exception.RepositoryException;
import com.rodasoft.recursos.humanos.exception.ValidacionException;
import com.rodasoft.recursos.humanos.service.AuthService;
import com.rodasoft.recursos.humanos.util.Alertas;
import com.rodasoft.recursos.humanos.util.SceneManager;
    import javafx.fxml.FXML;
    import javafx.scene.control.ComboBox;
    import javafx.scene.control.PasswordField;
    import javafx.scene.control.TextField;

    public class RegistroController {

        private final AuthService authService;
        private final SceneManager sceneManager;

        @FXML private TextField txtUsuario;
        @FXML private PasswordField txtContrasena;
        @FXML private PasswordField txtConfirmacion;
        @FXML private ComboBox<RolDTO> cmbRol;

        public RegistroController(AuthService authService, SceneManager sceneManager) {
            this.authService = authService;
            this.sceneManager = sceneManager;
        }

        @FXML
        private void initialize() {
            try {
                cmbRol.getItems().setAll(authService.listarRoles());
            } catch (RepositoryException e) {
                Alertas.error(e.getMessage());
            }
        }

        @FXML
        private void onRegistrar() {
            try {
                authService.registrar(new RegistroDTO(txtUsuario.getText(), txtContrasena.getText(),
                        txtConfirmacion.getText(), cmbRol.getValue()));
                Alertas.info("Usuario registrado correctamente.");
                sceneManager.cambiarVista("Login.fxml", "Iniciar sesión");
            } catch (ValidacionException | RepositoryException e) {
                Alertas.error(e.getMessage());
            }
        }

        @FXML
        private void onVolver() {
            sceneManager.cambiarVista("Login.fxml", "Iniciar sesión");
        }
    }