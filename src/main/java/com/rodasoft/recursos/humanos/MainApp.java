package com.rodasoft.recursos.humanos;

import com.rodasoft.recursos.humanos.config.DataBaseConeccion;
import com.rodasoft.recursos.humanos.controlller.DashboardController;
import com.rodasoft.recursos.humanos.controlller.LoginController;
import com.rodasoft.recursos.humanos.controlller.RegistroController;
import com.rodasoft.recursos.humanos.repository.AspiranteRepository;
import com.rodasoft.recursos.humanos.repository.RolRepository;
import com.rodasoft.recursos.humanos.repository.UsuarioRepository;
import com.rodasoft.recursos.humanos.service.AspiranteService;
import com.rodasoft.recursos.humanos.service.AuthService;
import com.rodasoft.recursos.humanos.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        DataBaseConeccion conexion = new DataBaseConeccion ();
        AuthService authService = new AuthService(new UsuarioRepository(conexion), new RolRepository(conexion));
        AspiranteService aspiranteService = new AspiranteService(new AspiranteRepository(conexion));

        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.setFabricaControladores(tipo -> {
            if (tipo == LoginController.class) {
                return new LoginController(authService, sceneManager);
            }
            if (tipo == RegistroController.class) {
                return new RegistroController(authService, sceneManager);
            }
            if (tipo == DashboardController.class) {
                return new DashboardController(aspiranteService, sceneManager);
            }
            throw new IllegalArgumentException("Controlador no encontrado: " + tipo);
        });
        sceneManager.cambiarVista("Login.fxml", "Iniciar sesión");
    }
    public static void main(String[] args) {
        launch(args);
    }
}
