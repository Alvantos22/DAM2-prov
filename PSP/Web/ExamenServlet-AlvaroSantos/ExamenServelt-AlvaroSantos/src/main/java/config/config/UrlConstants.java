package config.config;

/**
 * Constantes relacionadas con URLs y rutas de la aplicación web
 */
public final class UrlConstants {

    // Constructor privado para prevenir instanciación
    private UrlConstants() {
        throw new UnsupportedOperationException("Esta es una clase de utilidad");
    }



    // Rutas de plantillas Thymeleaf
    public static final String TEMPLATE_PREFIX = "/WEB-INF/templates/";
    public static final String TEMPLATE_SUFFIX = ".html";

    // Nombres de plantillas
    public static final String TEMPLATE_CONFIGURAR = "configurar";
    public static final String TEMPLATE_CLASE_CODIGO = "CodigoClass";
}
