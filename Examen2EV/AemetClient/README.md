# Cliente AEMET OpenData - PSP UD3 Práctica 1

## Descripción

Aplicación cliente JavaFX que consume la API REST de AEMET OpenData con **7 pestañas** de consulta:

1. **Predicción Nacional** - Predicción general para España (hoy, mañana, pasado, medio plazo, tendencia)
2. **Predicción por CC.AA.** - Predicción por comunidades autónomas (19 CCAA, varios períodos)
3. **Predicción por Provincias** - Predicción por provincias (52 provincias, hoy/mañana)
4. **Predicción por Localidades** - Predicción diaria/horaria por municipio (50+ municipios + código manual)
5. **Predicción por Montaña** - Predicción e info nivológica por macizo montañoso (9 macizos, 4 días)
6. **Predicción por Playas** - Predicción para playas (25+ playas + código manual)
7. **Valores Climatológicos Diarios** - Datos diarios por estación meteorológica (ya desarrollado en clase)

## Requisitos

- **Java 17** o superior
- **Maven 3.6+**
- **API Key de AEMET OpenData** (solicitarla en: https://opendata.aemet.es/centrodedescargas/altaUsuario)

## Estructura del Proyecto

```
AemetClient/
├── pom.xml
├── README.md
└── src/main/java/
    ├── module-info.java
    └── es/aemet/
        ├── model/
        │   ├── Respuesta.java        # Respuesta estándar de la API
        │   ├── Estacion.java          # Estación meteorológica
        │   └── ValoresDiarios.java    # Valores climatológicos diarios
        ├── service/
        │   ├── ClienteDatos.java      # Cliente HTTP para obtener datos
        │   ├── ClienteAemet.java      # Cliente principal con todos los endpoints
        │   └── DatosAemet.java        # Constantes (CCAA, provincias, playas, etc.)
        └── ui/
            └── MainApp.java           # Aplicación JavaFX con las 7 pestañas
```

## Compilar y Ejecutar

```bash
# Compilar
mvn clean compile

# Ejecutar
mvn javafx:run
```

## Dependencias Maven

- **JavaFX 17** - Interfaz gráfica con pestañas (TabPane)
- **Jackson 2.15** - Deserialización JSON de las respuestas de la API

## Endpoints de la API AEMET utilizados

| Pestaña | Endpoint |
|---------|----------|
| Nacional | `GET /api/prediccion/nacional/{periodo}` |
| CC.AA. | `GET /api/prediccion/ccaa/{periodo}/{ccaa}` |
| Provincias | `GET /api/prediccion/provincia/{periodo}/{provincia}` |
| Localidades | `GET /api/prediccion/especifica/municipio/{tipo}/{municipio}` |
| Montaña | `GET /api/prediccion/especifica/montaña/pasada/area/{area}/dia/{dia}` |
| Playas | `GET /api/prediccion/especifica/playa/{playa}` |
| Valores Diarios | `GET /api/valores/climatologicos/diarios/datos/fechaini/{ini}/fechafin/{fin}/estacion/{id}` |

## Uso

1. Ejecutar la aplicación
2. Introducir la API Key de AEMET OpenData
3. Pulsar "Conectar"
4. Navegar por las 7 pestañas y realizar consultas

## Notas

- Las consultas se ejecutan en hilos secundarios para no bloquear la interfaz
- Las predicciones nacionales, de CCAA y de provincias devuelven texto plano
- Las predicciones de localidades y playas devuelven JSON formateado
- La pestaña de valores diarios permite consultar el inventario completo de estaciones
- Se incluyen datos de ejemplo para municipios y playas; se pueden añadir más fácilmente en `DatosAemet.java`
