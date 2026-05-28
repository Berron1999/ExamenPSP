package es.aemet.ui;

import es.aemet.model.Estacion;
import es.aemet.model.ValoresDiarios;
import es.aemet.service.ClienteAemet;
import es.aemet.service.DatosAemet;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * PSP - UD3 - Práctica 1
 * Aplicación Cliente AEMET OpenData
 *
 * Aplicación JavaFX con 7 pestañas para consultar la API AEMET:
 * 1. Predicción Nacional
 * 2. Predicción por Comunidades Autónomas
 * 3. Predicción por Provincias
 * 4. Predicción por Localidades
 * 5. Predicción por Macizos Montañosos
 * 6. Predicción por Playas
 * 7. Valores Climatológicos Diarios
 */
public class MainApp extends Application {

    // Área de texto compartida para mostrar el resultado (cada pestaña tendrá la suya)
    private static final String FONT_FAMILY = "Consolas";
    private static final int FONT_SIZE = 13;

    @Override
    public void start(Stage primaryStage) {
        // --- Pantalla de configuración de API Key ---
        VBox configBox = new VBox(15);
        configBox.setAlignment(Pos.CENTER);
        configBox.setPadding(new Insets(40));

        Label titleLabel = new Label("Cliente AEMET OpenData");
        titleLabel.setFont(Font.font("Arial", 24));

        Label subtitleLabel = new Label("PSP - UD3 - Práctica 1");
        subtitleLabel.setFont(Font.font("Arial", 14));

        Label instructionLabel = new Label("Introduce tu API Key de AEMET OpenData:");

        TextField apiKeyField = new TextField();
        apiKeyField.setPromptText("Tu API Key aquí...");
        apiKeyField.setMaxWidth(500);

        Button connectBtn = new Button("Conectar");
        connectBtn.setStyle("-fx-font-size: 14px; -fx-padding: 8 20;");

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: red;");

        configBox.getChildren().addAll(titleLabel, subtitleLabel, instructionLabel, apiKeyField, connectBtn, statusLabel);

        Scene configScene = new Scene(configBox, 600, 350);

        connectBtn.setOnAction(e -> {
            String key = apiKeyField.getText().trim();
            if (key.isEmpty()) {
                statusLabel.setText("Debes introducir una API Key válida.");
                return;
            }
            ClienteAemet.apiKey = key;
            statusLabel.setText("Conectando...");
            statusLabel.setStyle("-fx-text-fill: blue;");

            // Mostrar la interfaz principal
            mostrarInterfazPrincipal(primaryStage);
        });

        // También permitir pulsar Enter
        apiKeyField.setOnAction(e -> connectBtn.fire());

        primaryStage.setTitle("Cliente AEMET - PSP UD3 Práctica 1");
        primaryStage.setScene(configScene);
        primaryStage.show();
    }

    /**
     * Muestra la interfaz principal con las 7 pestañas
     */
    private void mostrarInterfazPrincipal(Stage stage) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Crear las 7 pestañas
        tabPane.getTabs().addAll(
                crearTabPrediccionNacional(),
                crearTabPrediccionCCAA(),
                crearTabPrediccionProvincias(),
                crearTabPrediccionLocalidades(),
                crearTabPrediccionMontana(),
                crearTabPrediccionPlayas(),
                crearTabValoresDiarios()
        );

        Scene mainScene = new Scene(tabPane, 1000, 700);
        stage.setScene(mainScene);
        stage.setTitle("Cliente AEMET OpenData - PSP UD3 Práctica 1");
    }

    // =====================================================================
    // 1. PREDICCIÓN NACIONAL
    // =====================================================================
    private Tab crearTabPrediccionNacional() {
        Tab tab = new Tab("1. Nacional");

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));

        Label titulo = new Label("Predicción del Tiempo para España");
        titulo.setFont(Font.font("Arial", 18));

        // Selector de período
        HBox controlBox = new HBox(10);
        controlBox.setAlignment(Pos.CENTER_LEFT);
        Label periodoLabel = new Label("Período:");
        ComboBox<String> periodoCombo = new ComboBox<>();
        for (Map.Entry<String, String> entry : DatosAemet.PERIODOS_NACIONAL.entrySet()) {
            periodoCombo.getItems().add(entry.getValue());
        }
        periodoCombo.getSelectionModel().selectFirst();

        Button consultarBtn = new Button("Consultar");
        ProgressIndicator progress = new ProgressIndicator();
        progress.setVisible(false);
        progress.setPrefSize(25, 25);

        controlBox.getChildren().addAll(periodoLabel, periodoCombo, consultarBtn, progress);

        TextArea resultado = crearTextArea();

        consultarBtn.setOnAction(e -> {
            int idx = periodoCombo.getSelectionModel().getSelectedIndex();
            String periodo = (String) DatosAemet.PERIODOS_NACIONAL.keySet().toArray()[idx];

            ejecutarConsulta(progress, consultarBtn, resultado, () -> {
                switch (periodo) {
                    case "hoy": return ClienteAemet.prediccionNacionalHoy();
                    case "manana": return ClienteAemet.prediccionNacionalManana();
                    case "pasadomanana": return ClienteAemet.prediccionNacionalPasadoManana();
                    case "medioplazo": return ClienteAemet.prediccionNacionalMedioPlazo();
                    case "tendencia": return ClienteAemet.prediccionNacionalTendencia();
                    default: return "Período no válido";
                }
            });
        });

        content.getChildren().addAll(titulo, controlBox, resultado);
        VBox.setVgrow(resultado, Priority.ALWAYS);
        tab.setContent(content);
        return tab;
    }

    // =====================================================================
    // 2. PREDICCIÓN POR COMUNIDADES AUTÓNOMAS
    // =====================================================================
    private Tab crearTabPrediccionCCAA() {
        Tab tab = new Tab("2. CC.AA.");

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));

        Label titulo = new Label("Predicción por Comunidades Autónomas");
        titulo.setFont(Font.font("Arial", 18));

        HBox controlBox = new HBox(10);
        controlBox.setAlignment(Pos.CENTER_LEFT);

        Label ccaaLabel = new Label("Comunidad:");
        ComboBox<String> ccaaCombo = new ComboBox<>();
        for (String nombre : DatosAemet.COMUNIDADES.values()) {
            ccaaCombo.getItems().add(nombre);
        }
        ccaaCombo.getSelectionModel().selectFirst();

        Label periodoLabel = new Label("Período:");
        ComboBox<String> periodoCombo = new ComboBox<>();
        for (String nombre : DatosAemet.PERIODOS_CCAA.values()) {
            periodoCombo.getItems().add(nombre);
        }
        periodoCombo.getSelectionModel().selectFirst();

        Button consultarBtn = new Button("Consultar");
        ProgressIndicator progress = new ProgressIndicator();
        progress.setVisible(false);
        progress.setPrefSize(25, 25);

        controlBox.getChildren().addAll(ccaaLabel, ccaaCombo, periodoLabel, periodoCombo, consultarBtn, progress);

        TextArea resultado = crearTextArea();

        consultarBtn.setOnAction(e -> {
            int ccaaIdx = ccaaCombo.getSelectionModel().getSelectedIndex();
            String codigoCCAA = (String) DatosAemet.COMUNIDADES.keySet().toArray()[ccaaIdx];
            int periodoIdx = periodoCombo.getSelectionModel().getSelectedIndex();
            String periodo = (String) DatosAemet.PERIODOS_CCAA.keySet().toArray()[periodoIdx];

            ejecutarConsulta(progress, consultarBtn, resultado, () -> {
                switch (periodo) {
                    case "hoy": return ClienteAemet.prediccionCCAAHoy(codigoCCAA);
                    case "manana": return ClienteAemet.prediccionCCAAManana(codigoCCAA);
                    case "pasadomanana": return ClienteAemet.prediccionCCAAPasadoManana(codigoCCAA);
                    case "medioplazo": return ClienteAemet.prediccionCCAAMedioPlazo(codigoCCAA);
                    default: return "Período no válido";
                }
            });
        });

        content.getChildren().addAll(titulo, controlBox, resultado);
        VBox.setVgrow(resultado, Priority.ALWAYS);
        tab.setContent(content);
        return tab;
    }

    // =====================================================================
    // 3. PREDICCIÓN POR PROVINCIAS
    // =====================================================================
    private Tab crearTabPrediccionProvincias() {
        Tab tab = new Tab("3. Provincias");

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));

        Label titulo = new Label("Predicción por Provincias");
        titulo.setFont(Font.font("Arial", 18));

        HBox controlBox = new HBox(10);
        controlBox.setAlignment(Pos.CENTER_LEFT);

        Label provLabel = new Label("Provincia:");
        ComboBox<String> provCombo = new ComboBox<>();
        for (Map.Entry<String, String> entry : DatosAemet.PROVINCIAS.entrySet()) {
            provCombo.getItems().add(entry.getKey() + " - " + entry.getValue());
        }
        provCombo.getSelectionModel().selectFirst();

        Label periodoLabel = new Label("Período:");
        ComboBox<String> periodoCombo = new ComboBox<>(
                FXCollections.observableArrayList("Hoy", "Mañana")
        );
        periodoCombo.getSelectionModel().selectFirst();

        Button consultarBtn = new Button("Consultar");
        ProgressIndicator progress = new ProgressIndicator();
        progress.setVisible(false);
        progress.setPrefSize(25, 25);

        controlBox.getChildren().addAll(provLabel, provCombo, periodoLabel, periodoCombo, consultarBtn, progress);

        TextArea resultado = crearTextArea();

        consultarBtn.setOnAction(e -> {
            int provIdx = provCombo.getSelectionModel().getSelectedIndex();
            String codigoProv = (String) DatosAemet.PROVINCIAS.keySet().toArray()[provIdx];
            String periodo = periodoCombo.getSelectionModel().getSelectedItem();

            ejecutarConsulta(progress, consultarBtn, resultado, () -> {
                if ("Hoy".equals(periodo)) {
                    return ClienteAemet.prediccionProvinciaHoy(codigoProv);
                } else {
                    return ClienteAemet.prediccionProvinciaManana(codigoProv);
                }
            });
        });

        content.getChildren().addAll(titulo, controlBox, resultado);
        VBox.setVgrow(resultado, Priority.ALWAYS);
        tab.setContent(content);
        return tab;
    }

    // =====================================================================
    // 4. PREDICCIÓN POR LOCALIDADES
    // =====================================================================
    private Tab crearTabPrediccionLocalidades() {
        Tab tab = new Tab("4. Localidades");

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));

        Label titulo = new Label("Predicción por Localidades (Municipios)");
        titulo.setFont(Font.font("Arial", 18));

        HBox controlBox = new HBox(10);
        controlBox.setAlignment(Pos.CENTER_LEFT);

        Label munLabel = new Label("Municipio:");
        ComboBox<String> munCombo = new ComboBox<>();
        for (Map.Entry<String, String> entry : DatosAemet.MUNICIPIOS.entrySet()) {
            munCombo.getItems().add(entry.getValue() + " (" + entry.getKey() + ")");
        }
        munCombo.getSelectionModel().selectFirst();

        // También permitir escribir código manualmente
        Label manualLabel = new Label("  o código INE:");
        TextField codigoField = new TextField();
        codigoField.setPromptText("Ej: 46250");
        codigoField.setPrefWidth(100);

        Label tipoLabel = new Label("Tipo:");
        ComboBox<String> tipoCombo = new ComboBox<>(
                FXCollections.observableArrayList("Diaria", "Horaria")
        );
        tipoCombo.getSelectionModel().selectFirst();

        Button consultarBtn = new Button("Consultar");
        ProgressIndicator progress = new ProgressIndicator();
        progress.setVisible(false);
        progress.setPrefSize(25, 25);

        controlBox.getChildren().addAll(munLabel, munCombo, manualLabel, codigoField, tipoLabel, tipoCombo, consultarBtn, progress);

        TextArea resultado = crearTextArea();

        consultarBtn.setOnAction(e -> {
            // Prioridad al código manual, si está vacío usamos el combo
            String codigo;
            if (!codigoField.getText().trim().isEmpty()) {
                codigo = codigoField.getText().trim();
            } else {
                int munIdx = munCombo.getSelectionModel().getSelectedIndex();
                codigo = (String) DatosAemet.MUNICIPIOS.keySet().toArray()[munIdx];
            }
            String tipo = tipoCombo.getSelectionModel().getSelectedItem();
            final String codigoFinal = codigo;

            ejecutarConsulta(progress, consultarBtn, resultado, () -> {
                String jsonResult;
                if ("Diaria".equals(tipo)) {
                    jsonResult = ClienteAemet.prediccionMunicipioDiaria(codigoFinal);
                } else {
                    jsonResult = ClienteAemet.prediccionMunicipioHoraria(codigoFinal);
                }
                // Formatear el JSON para que sea legible
                return formatearJson(jsonResult);
            });
        });

        content.getChildren().addAll(titulo, controlBox, resultado);
        VBox.setVgrow(resultado, Priority.ALWAYS);
        tab.setContent(content);
        return tab;
    }

    // =====================================================================
    // 5. PREDICCIÓN POR MACIZOS MONTAÑOSOS
    // =====================================================================
    private Tab crearTabPrediccionMontana() {
        Tab tab = new Tab("5. Montaña");

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));

        Label titulo = new Label("Predicción por Macizos Montañosos");
        titulo.setFont(Font.font("Arial", 18));

        HBox controlBox = new HBox(10);
        controlBox.setAlignment(Pos.CENTER_LEFT);

        Label montanaLabel = new Label("Macizo:");
        ComboBox<String> montanaCombo = new ComboBox<>();
        for (String nombre : DatosAemet.MONTANAS.values()) {
            montanaCombo.getItems().add(nombre);
        }
        montanaCombo.getSelectionModel().selectFirst();

        Label diaLabel = new Label("Día:");
        ComboBox<String> diaCombo = new ComboBox<>();
        for (String nombre : DatosAemet.DIAS_MONTANA.values()) {
            diaCombo.getItems().add(nombre);
        }
        diaCombo.getSelectionModel().selectFirst();

        Button consultarBtn = new Button("Consultar Predicción");
        Button nivoBtn = new Button("Info Nivológica");
        ProgressIndicator progress = new ProgressIndicator();
        progress.setVisible(false);
        progress.setPrefSize(25, 25);

        controlBox.getChildren().addAll(montanaLabel, montanaCombo, diaLabel, diaCombo, consultarBtn, nivoBtn, progress);

        TextArea resultado = crearTextArea();

        consultarBtn.setOnAction(e -> {
            int montIdx = montanaCombo.getSelectionModel().getSelectedIndex();
            String codigoMont = (String) DatosAemet.MONTANAS.keySet().toArray()[montIdx];
            int diaIdx = diaCombo.getSelectionModel().getSelectedIndex();

            ejecutarConsulta(progress, consultarBtn, resultado, () ->
                    ClienteAemet.prediccionMontana(codigoMont, diaIdx)
            );
        });

        nivoBtn.setOnAction(e -> {
            int montIdx = montanaCombo.getSelectionModel().getSelectedIndex();
            String codigoMont = (String) DatosAemet.MONTANAS.keySet().toArray()[montIdx];

            ejecutarConsulta(progress, nivoBtn, resultado, () ->
                    ClienteAemet.informacionNivologica(codigoMont)
            );
        });

        content.getChildren().addAll(titulo, controlBox, resultado);
        VBox.setVgrow(resultado, Priority.ALWAYS);
        tab.setContent(content);
        return tab;
    }

    // =====================================================================
    // 6. PREDICCIÓN POR PLAYAS
    // =====================================================================
    private Tab crearTabPrediccionPlayas() {
        Tab tab = new Tab("6. Playas");

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));

        Label titulo = new Label("Predicción por Playas");
        titulo.setFont(Font.font("Arial", 18));

        HBox controlBox = new HBox(10);
        controlBox.setAlignment(Pos.CENTER_LEFT);

        Label playaLabel = new Label("Playa:");
        ComboBox<String> playaCombo = new ComboBox<>();
        for (Map.Entry<String, String> entry : DatosAemet.PLAYAS.entrySet()) {
            playaCombo.getItems().add(entry.getValue());
        }
        playaCombo.getSelectionModel().selectFirst();

        // También permitir escribir código manualmente
        Label manualLabel = new Label("  o código:");
        TextField codigoField = new TextField();
        codigoField.setPromptText("Ej: 4602001");
        codigoField.setPrefWidth(100);

        Button consultarBtn = new Button("Consultar");
        ProgressIndicator progress = new ProgressIndicator();
        progress.setVisible(false);
        progress.setPrefSize(25, 25);

        controlBox.getChildren().addAll(playaLabel, playaCombo, manualLabel, codigoField, consultarBtn, progress);

        TextArea resultado = crearTextArea();

        consultarBtn.setOnAction(e -> {
            String codigo;
            if (!codigoField.getText().trim().isEmpty()) {
                codigo = codigoField.getText().trim();
            } else {
                int playaIdx = playaCombo.getSelectionModel().getSelectedIndex();
                codigo = (String) DatosAemet.PLAYAS.keySet().toArray()[playaIdx];
            }
            final String codigoFinal = codigo;

            ejecutarConsulta(progress, consultarBtn, resultado, () -> {
                String jsonResult = ClienteAemet.prediccionPlaya(codigoFinal);
                return formatearJson(jsonResult);
            });
        });

        content.getChildren().addAll(titulo, controlBox, resultado);
        VBox.setVgrow(resultado, Priority.ALWAYS);
        tab.setContent(content);
        return tab;
    }

    // =====================================================================
    // 7. VALORES CLIMATOLÓGICOS DIARIOS
    // =====================================================================
    private Tab crearTabValoresDiarios() {
        Tab tab = new Tab("7. Valores Diarios");

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));

        Label titulo = new Label("Valores Climatológicos Diarios por Estación");
        titulo.setFont(Font.font("Arial", 18));

        // Fila 1: Estación
        HBox fila1 = new HBox(10);
        fila1.setAlignment(Pos.CENTER_LEFT);
        Label estacionLabel = new Label("Estación (indicativo):");
        TextField estacionField = new TextField();
        estacionField.setPromptText("Ej: 8416Y");
        estacionField.setPrefWidth(120);

        Button cargarEstacionesBtn = new Button("Ver estaciones");
        ProgressIndicator progress = new ProgressIndicator();
        progress.setVisible(false);
        progress.setPrefSize(25, 25);

        fila1.getChildren().addAll(estacionLabel, estacionField, cargarEstacionesBtn, progress);

        // Fila 2: Fechas
        HBox fila2 = new HBox(10);
        fila2.setAlignment(Pos.CENTER_LEFT);

        Label fechaIniLabel = new Label("Fecha inicio:");
        DatePicker fechaIniPicker = new DatePicker(LocalDate.now().minusDays(7));

        Label fechaFinLabel = new Label("Fecha fin:");
        DatePicker fechaFinPicker = new DatePicker(LocalDate.now().minusDays(1));

        Button consultarBtn = new Button("Consultar Valores");

        fila2.getChildren().addAll(fechaIniLabel, fechaIniPicker, fechaFinLabel, fechaFinPicker, consultarBtn);

        TextArea resultado = crearTextArea();

        // Cargar inventario de estaciones
        cargarEstacionesBtn.setOnAction(e -> {
            ejecutarConsulta(progress, cargarEstacionesBtn, resultado, () -> {
                List<Estacion> estaciones = ClienteAemet.inventarioEstacionesTodas();
                StringBuilder sb = new StringBuilder();
                sb.append("=== INVENTARIO DE ESTACIONES METEOROLÓGICAS ===\n");
                sb.append("Total: ").append(estaciones.size()).append(" estaciones\n\n");
                sb.append(String.format("%-10s %-40s %-20s %-10s\n", "CÓDIGO", "NOMBRE", "PROVINCIA", "ALTITUD"));
                sb.append("-".repeat(85)).append("\n");
                for (Estacion est : estaciones) {
                    sb.append(String.format("%-10s %-40s %-20s %-10s\n",
                            est.getIndicativo(), est.getNombre(), est.getProvincia(), est.getAltitud()));
                }
                return sb.toString();
            });
        });

        // Consultar valores diarios
        consultarBtn.setOnAction(e -> {
            String idema = estacionField.getText().trim();
            if (idema.isEmpty()) {
                resultado.setText("Introduce el indicativo de la estación (ej: 8416Y).\n"
                        + "Puedes consultar los indicativos pulsando 'Ver estaciones'.");
                return;
            }

            LocalDate fechaIni = fechaIniPicker.getValue();
            LocalDate fechaFin = fechaFinPicker.getValue();

            // Formato requerido por AEMET: AAAA-MM-DDTHH:MM:SSUTC
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'UTC'");
            String fechaIniStr = fechaIni.atStartOfDay().format(fmt);
            String fechaFinStr = fechaFin.atTime(23, 59, 59).format(fmt);

            ejecutarConsulta(progress, consultarBtn, resultado, () -> {
                ValoresDiarios[] valores = ClienteAemet.valoresClimaDiarios(fechaIniStr, fechaFinStr, idema);
                StringBuilder sb = new StringBuilder();
                sb.append("=== VALORES CLIMATOLÓGICOS DIARIOS ===\n");
                sb.append("Estación: ").append(idema).append("\n");
                sb.append("Período: ").append(fechaIni).append(" a ").append(fechaFin).append("\n");
                sb.append("Registros: ").append(valores.length).append("\n\n");

                for (ValoresDiarios v : valores) {
                    sb.append("━".repeat(60)).append("\n");
                    sb.append(v.toString()).append("\n");
                    sb.append("Viento: dir=").append(v.getDir())
                            .append(", vel.media=").append(v.getVelocidadMedia())
                            .append(", racha=").append(v.getRacha()).append("\n");
                    sb.append("Presión: max=").append(v.getPresMax())
                            .append(", min=").append(v.getPresMin()).append("\n");
                    sb.append("Horas de sol: ").append(v.getSol()).append("\n");
                }
                return sb.toString();
            });
        });

        content.getChildren().addAll(titulo, fila1, fila2, resultado);
        VBox.setVgrow(resultado, Priority.ALWAYS);
        tab.setContent(content);
        return tab;
    }

    // =====================================================================
    // UTILIDADES
    // =====================================================================

    /**
     * Crea un TextArea con formato monoespaciado
     */
    private TextArea crearTextArea() {
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setFont(Font.font(FONT_FAMILY, FONT_SIZE));
        textArea.setText("Pulsa 'Consultar' para obtener la predicción...");
        return textArea;
    }

    /**
     * Ejecuta una consulta en un hilo secundario para no bloquear la UI
     */
    private void ejecutarConsulta(ProgressIndicator progress, Button btn, TextArea resultado,
                                  ConsultaAemet consulta) {
        progress.setVisible(true);
        btn.setDisable(true);
        resultado.setText("Consultando AEMET OpenData...");

        new Thread(() -> {
            try {
                String datos = consulta.ejecutar();
                Platform.runLater(() -> {
                    resultado.setText(datos);
                    progress.setVisible(false);
                    btn.setDisable(false);
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    resultado.setText("Error en la consulta:\n" + ex.getMessage()
                            + "\n\nVerifica:\n- Que la API Key sea correcta\n"
                            + "- Que los parámetros sean válidos\n"
                            + "- Que tengas conexión a Internet");
                    progress.setVisible(false);
                    btn.setDisable(false);
                });
            }
        }).start();
    }

    /**
     * Formatea un JSON para hacerlo legible
     */
    private String formatearJson(String json) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Object obj = mapper.readValue(json, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            // Si no es JSON válido, devolver tal cual (puede ser texto plano)
            return json;
        }
    }

    /**
     * Interfaz funcional para las consultas a AEMET
     */
    @FunctionalInterface
    interface ConsultaAemet {
        String ejecutar() throws Exception;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
