package com.rodasoft.recursos.humanos.controlller;

import com.rodasoft.recursos.humanos.dto.AspiranteDTO;
import com.rodasoft.recursos.humanos.exception.RepositoryException;
import com.rodasoft.recursos.humanos.exception.ValidacionException;
import com.rodasoft.recursos.humanos.service.AspiranteService;
import com.rodasoft.recursos.humanos.util.Alertas;
import com.rodasoft.recursos.humanos.util.SceneManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class DashboardController {

    private final AspiranteService aspiranteService;
    private final SceneManager sceneManager;

    @FXML private TableView<AspiranteDTO> tabla;
    @FXML private TableColumn<AspiranteDTO, Integer> colId;
    @FXML private TableColumn<AspiranteDTO, String> colNombre;
    @FXML private TableColumn<AspiranteDTO, String> colApellido;
    @FXML private TableColumn<AspiranteDTO, String> colCorreo;
    @FXML private TableColumn<AspiranteDTO, String> colTelefono;
    @FXML private TableColumn<AspiranteDTO, String> colDireccion;
    @FXML private TableColumn<AspiranteDTO, Integer> colEdad;

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtEdad;

    public DashboardController(AspiranteService aspiranteService, SceneManager sceneManager) {
        this.aspiranteService = aspiranteService;
        this.sceneManager = sceneManager;
    }

    @FXML
    private void initialize() {
        colId.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().id()));
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().nombre()));
        colApellido.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().apellido()));
        colCorreo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().correo()));
        colTelefono.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().telefono()));
        colDireccion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().direccion()));
        colEdad.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().edad()));

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, anterior, nuevo) -> {
            if (nuevo != null) {
                llenarFormulario(nuevo);
            }
        });
        cargarTabla();
    }

    @FXML
    private void onAgregar() {
        try {
            aspiranteService.agregar(leerFormulario(0));
            cargarTabla();
            limpiarFormulario();
            Alertas.info("Usuario agregado.");
        } catch (ValidacionException | RepositoryException e) {
            Alertas.error(e.getMessage());
        }
    }

    @FXML
    private void onActualizar() {
        AspiranteDTO seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alertas.error("Seleccione un usuario de la tabla.");
            return;
        }
        try {
            aspiranteService.actualizar(leerFormulario(seleccionado.id()));
            cargarTabla();
            limpiarFormulario();
            Alertas.info("Usuario actualizado.");
        } catch (ValidacionException | RepositoryException e) {
            Alertas.error(e.getMessage());
        }
    }

    @FXML
    private void onEliminar() {
        AspiranteDTO seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alertas.error("Seleccione a un usuario.");
            return;
        }
        if (!Alertas.confirmar("¿Eliminar a " + seleccionado.nombre() + " " + seleccionado.apellido() + "?")) {
            return;
        }
        try {
            aspiranteService.eliminar(seleccionado.id());
            cargarTabla();
            limpiarFormulario();
        } catch (RepositoryException e) {
            Alertas.error(e.getMessage());
        }
    }

    @FXML
    private void onLimpiar() {
        limpiarFormulario();
    }

    @FXML
    private void onCerrarSesion() {
        sceneManager.cambiarVista("Login.fxml", "Iniciar sesión");
    }

    private void cargarTabla() {
        try {
            tabla.getItems().setAll(aspiranteService.listar());
        } catch (RepositoryException e) {
            Alertas.error(e.getMessage());
        }
    }

    private AspiranteDTO leerFormulario(int id) {
        int edad;
        try {
            edad = Integer.parseInt(txtEdad.getText().trim());
        } catch (NumberFormatException e) {
            throw new ValidacionException("La edad debe ser solo numeros.");
        }
        return new AspiranteDTO(id, txtNombre.getText(), txtApellido.getText(), txtCorreo.getText(),
                txtTelefono.getText(), txtDireccion.getText(), edad);
    }

    private void llenarFormulario(AspiranteDTO a) {
        txtNombre.setText(a.nombre());
        txtApellido.setText(a.apellido());
        txtCorreo.setText(a.correo());
        txtTelefono.setText(a.telefono());
        txtDireccion.setText(a.direccion());
        txtEdad.setText(String.valueOf(a.edad()));
    }

    private void limpiarFormulario() {
        tabla.getSelectionModel().clearSelection();
        txtNombre.clear();
        txtApellido.clear();
        txtCorreo.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtEdad.clear();
    }
}
