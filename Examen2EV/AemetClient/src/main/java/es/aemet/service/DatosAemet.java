package es.aemet.service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Constantes con los códigos necesarios para las peticiones a la API AEMET.
 * Incluye códigos de CCAA, provincias, macizos montañosos, playas y municipios de ejemplo.
 */
public class DatosAemet {

    // =====================================================================
    // COMUNIDADES AUTÓNOMAS (código AEMET -> nombre)
    // =====================================================================
    public static final Map<String, String> COMUNIDADES = new LinkedHashMap<>();
    static {
        COMUNIDADES.put("and", "Andalucía");
        COMUNIDADES.put("arn", "Aragón");
        COMUNIDADES.put("ast", "Asturias");
        COMUNIDADES.put("bal", "Islas Baleares");
        COMUNIDADES.put("coo", "Canarias");
        COMUNIDADES.put("can", "Cantabria");
        COMUNIDADES.put("cle", "Castilla y León");
        COMUNIDADES.put("clm", "Castilla-La Mancha");
        COMUNIDADES.put("cat", "Cataluña");
        COMUNIDADES.put("val", "Comunitat Valenciana");
        COMUNIDADES.put("ext", "Extremadura");
        COMUNIDADES.put("gal", "Galicia");
        COMUNIDADES.put("mad", "Madrid");
        COMUNIDADES.put("mur", "Murcia");
        COMUNIDADES.put("nav", "Navarra");
        COMUNIDADES.put("pva", "País Vasco");
        COMUNIDADES.put("rio", "La Rioja");
        COMUNIDADES.put("ceu", "Ceuta");
        COMUNIDADES.put("mel", "Melilla");
    }

    // =====================================================================
    // PROVINCIAS (código numérico -> nombre)
    // =====================================================================
    public static final Map<String, String> PROVINCIAS = new LinkedHashMap<>();
    static {
        PROVINCIAS.put("01", "Álava");
        PROVINCIAS.put("02", "Albacete");
        PROVINCIAS.put("03", "Alicante");
        PROVINCIAS.put("04", "Almería");
        PROVINCIAS.put("05", "Ávila");
        PROVINCIAS.put("06", "Badajoz");
        PROVINCIAS.put("07", "Islas Baleares");
        PROVINCIAS.put("08", "Barcelona");
        PROVINCIAS.put("09", "Burgos");
        PROVINCIAS.put("10", "Cáceres");
        PROVINCIAS.put("11", "Cádiz");
        PROVINCIAS.put("12", "Castellón");
        PROVINCIAS.put("13", "Ciudad Real");
        PROVINCIAS.put("14", "Córdoba");
        PROVINCIAS.put("15", "A Coruña");
        PROVINCIAS.put("16", "Cuenca");
        PROVINCIAS.put("17", "Girona");
        PROVINCIAS.put("18", "Granada");
        PROVINCIAS.put("19", "Guadalajara");
        PROVINCIAS.put("20", "Gipuzkoa");
        PROVINCIAS.put("21", "Huelva");
        PROVINCIAS.put("22", "Huesca");
        PROVINCIAS.put("23", "Jaén");
        PROVINCIAS.put("24", "León");
        PROVINCIAS.put("25", "Lleida");
        PROVINCIAS.put("26", "La Rioja");
        PROVINCIAS.put("27", "Lugo");
        PROVINCIAS.put("28", "Madrid");
        PROVINCIAS.put("29", "Málaga");
        PROVINCIAS.put("30", "Murcia");
        PROVINCIAS.put("31", "Navarra");
        PROVINCIAS.put("32", "Ourense");
        PROVINCIAS.put("33", "Asturias");
        PROVINCIAS.put("34", "Palencia");
        PROVINCIAS.put("35", "Las Palmas");
        PROVINCIAS.put("36", "Pontevedra");
        PROVINCIAS.put("37", "Salamanca");
        PROVINCIAS.put("38", "Santa Cruz de Tenerife");
        PROVINCIAS.put("39", "Cantabria");
        PROVINCIAS.put("40", "Segovia");
        PROVINCIAS.put("41", "Sevilla");
        PROVINCIAS.put("42", "Soria");
        PROVINCIAS.put("43", "Tarragona");
        PROVINCIAS.put("44", "Teruel");
        PROVINCIAS.put("45", "Toledo");
        PROVINCIAS.put("46", "Valencia");
        PROVINCIAS.put("47", "Valladolid");
        PROVINCIAS.put("48", "Bizkaia");
        PROVINCIAS.put("49", "Zamora");
        PROVINCIAS.put("50", "Zaragoza");
        PROVINCIAS.put("51", "Ceuta");
        PROVINCIAS.put("52", "Melilla");
    }

    // =====================================================================
    // MACIZOS MONTAÑOSOS (código AEMET -> nombre)
    // =====================================================================
    public static final Map<String, String> MONTANAS = new LinkedHashMap<>();
    static {
        MONTANAS.put("peu1", "Picos de Europa");
        MONTANAS.put("nav1", "Pirineo Navarro");
        MONTANAS.put("arn1", "Pirineo Aragonés");
        MONTANAS.put("cat1", "Pirineo Catalán");
        MONTANAS.put("rio1", "Ibérica Riojana");
        MONTANAS.put("arn2", "Ibérica Aragonesa");
        MONTANAS.put("mad2", "Sierras de Guadarrama y Somosierra");
        MONTANAS.put("gre1", "Sierra de Gredos");
        MONTANAS.put("nev1", "Sierra Nevada");
    }

    // =====================================================================
    // PLAYAS DE EJEMPLO (código AEMET -> nombre)
    // Se pueden añadir más desde:
    // http://www.aemet.es/documentos/es/eltiempo/prediccion/playas/Playas_codigos.csv
    // =====================================================================
    public static final Map<String, String> PLAYAS = new LinkedHashMap<>();
    static {
        // Valencia
        PLAYAS.put("4602001", "Playa de la Malvarrosa (Valencia)");
        PLAYAS.put("4602002", "Playa de las Arenas (Valencia)");
        PLAYAS.put("4602003", "Playa del Saler (Valencia)");
        PLAYAS.put("4602101", "Playa de Gandia");
        // Alicante
        PLAYAS.put("0301601", "Playa del Postiguet (Alicante)");
        PLAYAS.put("0301602", "Playa de San Juan (Alicante)");
        PLAYAS.put("0302101", "Playa de Levante (Benidorm)");
        PLAYAS.put("0302102", "Playa de Poniente (Benidorm)");
        // Castellón
        PLAYAS.put("1200701", "Playa Norte (Peñíscola)");
        PLAYAS.put("1200101", "Playa del Pinar (Castellón)");
        // Barcelona
        PLAYAS.put("0801901", "Playa de la Barceloneta (Barcelona)");
        PLAYAS.put("0801902", "Playa de la Mar Bella (Barcelona)");
        // Málaga
        PLAYAS.put("2906601", "Playa de la Malagueta (Málaga)");
        PLAYAS.put("2906801", "Playa de Burriana (Nerja)");
        // Cádiz
        PLAYAS.put("1103501", "Playa de la Victoria (Cádiz)");
        PLAYAS.put("1103502", "Playa de la Caleta (Cádiz)");
        // Islas Baleares
        PLAYAS.put("0704001", "Playa de Palma (Mallorca)");
        PLAYAS.put("0704002", "Cala Major (Mallorca)");
        // Canarias
        PLAYAS.put("3502301", "Playa de las Canteras (Las Palmas)");
        PLAYAS.put("3802201", "Playa de las Teresitas (Tenerife)");
        // Murcia
        PLAYAS.put("3001601", "Playa del Hornillo (Águilas)");
        PLAYAS.put("3003001", "Playa de la Manga (San Javier)");
        // Asturias
        PLAYAS.put("3302401", "Playa de San Lorenzo (Gijón)");
        // Cantabria
        PLAYAS.put("3907501", "Playa del Sardinero (Santander)");
        // País Vasco
        PLAYAS.put("2002001", "Playa de la Concha (San Sebastián)");
        // Galicia
        PLAYAS.put("1500801", "Playa de Riazor (A Coruña)");
        PLAYAS.put("3605501", "Playa de Samil (Vigo)");
    }

    // =====================================================================
    // MUNICIPIOS DE EJEMPLO (código INE 5 dígitos -> nombre)
    // =====================================================================
    public static final Map<String, String> MUNICIPIOS = new LinkedHashMap<>();
    static {
        // Capitales de provincia principales
        MUNICIPIOS.put("28079", "Madrid");
        MUNICIPIOS.put("08019", "Barcelona");
        MUNICIPIOS.put("46250", "Valencia");
        MUNICIPIOS.put("41091", "Sevilla");
        MUNICIPIOS.put("50297", "Zaragoza");
        MUNICIPIOS.put("29067", "Málaga");
        MUNICIPIOS.put("30030", "Murcia");
        MUNICIPIOS.put("07040", "Palma de Mallorca");
        MUNICIPIOS.put("35016", "Las Palmas de Gran Canaria");
        MUNICIPIOS.put("48020", "Bilbao");
        MUNICIPIOS.put("03014", "Alicante");
        MUNICIPIOS.put("14021", "Córdoba");
        MUNICIPIOS.put("47186", "Valladolid");
        MUNICIPIOS.put("15030", "A Coruña");
        MUNICIPIOS.put("36057", "Vigo");
        MUNICIPIOS.put("33044", "Gijón");
        MUNICIPIOS.put("18087", "Granada");
        MUNICIPIOS.put("38038", "Santa Cruz de Tenerife");
        MUNICIPIOS.put("20069", "San Sebastián");
        MUNICIPIOS.put("39075", "Santander");
        MUNICIPIOS.put("01059", "Vitoria-Gasteiz");
        MUNICIPIOS.put("33024", "Oviedo");
        MUNICIPIOS.put("31201", "Pamplona");
        MUNICIPIOS.put("04013", "Almería");
        MUNICIPIOS.put("09059", "Burgos");
        MUNICIPIOS.put("06015", "Badajoz");
        MUNICIPIOS.put("02003", "Albacete");
        MUNICIPIOS.put("12040", "Castellón de la Plana");
        MUNICIPIOS.put("24089", "León");
        MUNICIPIOS.put("26089", "Logroño");
        MUNICIPIOS.put("11012", "Cádiz");
        MUNICIPIOS.put("23050", "Jaén");
        MUNICIPIOS.put("21041", "Huelva");
        MUNICIPIOS.put("27028", "Lugo");
        MUNICIPIOS.put("10037", "Cáceres");
        MUNICIPIOS.put("13034", "Ciudad Real");
        MUNICIPIOS.put("16078", "Cuenca");
        MUNICIPIOS.put("17079", "Girona");
        MUNICIPIOS.put("22125", "Huesca");
        MUNICIPIOS.put("25120", "Lleida");
        MUNICIPIOS.put("32054", "Ourense");
        MUNICIPIOS.put("34120", "Palencia");
        MUNICIPIOS.put("36038", "Pontevedra");
        MUNICIPIOS.put("37274", "Salamanca");
        MUNICIPIOS.put("40194", "Segovia");
        MUNICIPIOS.put("42173", "Soria");
        MUNICIPIOS.put("43148", "Tarragona");
        MUNICIPIOS.put("44216", "Teruel");
        MUNICIPIOS.put("45168", "Toledo");
        MUNICIPIOS.put("49275", "Zamora");
        MUNICIPIOS.put("05019", "Ávila");
        MUNICIPIOS.put("19130", "Guadalajara");
    }

    // =====================================================================
    // DÍAS PARA PREDICCIÓN DE MONTAÑA
    // =====================================================================
    public static final Map<Integer, String> DIAS_MONTANA = new LinkedHashMap<>();
    static {
        DIAS_MONTANA.put(0, "Hoy");
        DIAS_MONTANA.put(1, "Mañana");
        DIAS_MONTANA.put(2, "Pasado mañana");
        DIAS_MONTANA.put(3, "En 3 días");
    }

    // =====================================================================
    // PERÍODOS PREDICCIÓN NACIONAL
    // =====================================================================
    public static final Map<String, String> PERIODOS_NACIONAL = new LinkedHashMap<>();
    static {
        PERIODOS_NACIONAL.put("hoy", "Hoy");
        PERIODOS_NACIONAL.put("manana", "Mañana");
        PERIODOS_NACIONAL.put("pasadomanana", "Pasado mañana");
        PERIODOS_NACIONAL.put("medioplazo", "Medio plazo");
        PERIODOS_NACIONAL.put("tendencia", "Tendencia");
    }

    // =====================================================================
    // PERÍODOS PREDICCIÓN CCAA
    // =====================================================================
    public static final Map<String, String> PERIODOS_CCAA = new LinkedHashMap<>();
    static {
        PERIODOS_CCAA.put("hoy", "Hoy");
        PERIODOS_CCAA.put("manana", "Mañana");
        PERIODOS_CCAA.put("pasadomanana", "Pasado mañana");
        PERIODOS_CCAA.put("medioplazo", "Medio plazo");
    }
}
