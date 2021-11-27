package cne.heba.sie.util;

import android.net.Uri;

public class Constantes {

    public static int PUSEREDES = 0;
    //CONFIG DE REDES
    public static int online=0;
    public static final int MY_DEFAULT_TIMEOUT = 30000;
    public static String TIPORED;
    public static String SERVER="https://189.240.232.89/appirets/";
    public static String VERSION="1.4.0.3";
    public static String SERVERMEDIA="https://189.240.232.89/";

    //DATOS DEL USUARIO DE LA APP
    public static  String ID_Regional,ID_Estatal,ID_Seccional,ID_Municipio,ID_Equipo_Detalle,ID_Subequipo,ID_INE_ListaNominal,ID_Organismo,ID_Calle,EsAntorchista,ID_Antorchista,Status,ID_Elecciones_Contactos_Cy,ID_Usuario_Crypt,ID_Sistema_Nivel_Crypt;
    public static String token,ap,am,name,id_act,user,pass,Usuario_Crypt,UltimoIniciodeSesion2_Crypt,SIE_NewPass,STATUS,foto;

    public static int ID_USER_NIVEL;

    //CONTROL DE BETA TESTER

    public static String BETA_TESTER;

    //VARIABLES PARA CAPTURA
    public static String CLAVEL,CURP,AP,AM,NOMBRE,TIPOVIAL,NOMBREVIAL,MZ,LTE,COLONIA,CP,MUNICDOM,ENTIDOM,SECCION,EMIS,CIC,OCR,VIGENCIA, FACE, TWTT, TEL, PFACE, PTWIT, IF_ID_ACT, JSONDem,ID_ZONA,ID_RUTA,ID_SECCION,D1,D2,D3,NI,NE;

    //CONTROL DE ESTRUCTURA O PROMOVIDO POR IDS

    /*
    SECCION = 11
    CALLE = 12
    ACERA = 13
    PROMOVIDO = 14
    PROMOTOR = 20
    ZONAS Y RUTAS DEPENDIENDO NIVEL SON CONSTANTES EN BACKGROUND
     */

    public static  String Control_Nivel_ID;

    //END

    //VARIABLES DE COMRPOBACIÓN DE DATOS
    public static  int INCUR = 0, NOAPMA = 0, ELECTOR = 0, CONTAC = 0;

    //URI PARA PASAR LA RUTA URI A RUTA ABSOLUTA DE LAS IMAGENES USADAS EN ESTA APP
    public static Uri URIMG,pathF,pathA;

    //Subir Parte de Enfrente y parte de Atrás
    public static String RutaAbsolutaFrente,RutaAbsolutaAtras;

    //CONSTANTES UBICACIÓN
    public static String Clatitude,Clongitude, firmaPath;

    //VARIABLES DE UIX CRP E INE
    public static String TitleE="";

    //VARIABLES PARA LA MILITANCIA POLITICA
    public static String mensajeMilit,codigoMilit,nombreMilit,apMilit,amMilit,estadoMilit,partidoMilit,fechMilit;

    //CONSTANTES DE CONTROL ENTRE EVENTOS DE LLAMADO A controlCap
    //10 es captura normal, 30 es de OCR, 40 es de edición repetido

    public static int eventoSalida;

    public static int encontrado=0;

    public static  String Pdia,Pmes,Panio,Phora;
    public static String DNombre,DRegio,Desta,Dsec,Dequipo,DsubE,DseccioElec,Dcurp,Dtel,Dnivel,DCodecito,Drespo,Observa,id_reu;


    //CONTROLES DE ESTRUCTURA NUMERICOS
   // public static  String Estructura="0",Promovido="0",representante_zona="0",representante_zona_select="0",representante_ruta="0",representante_ruta_select2="0",representante_seccion="0",representante_seccion_select3="0",representante_calle="0",representante_calle4="0",representante_acera="0";

}
