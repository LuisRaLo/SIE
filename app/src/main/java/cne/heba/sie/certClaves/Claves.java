package cne.heba.sie.certClaves;

public class Claves {
    String[] EN = {"AS", "BS", "CL", "CS", "DF", "GT", "HG", "MC", "MS", "NL", "PL", "QR", "SL", "TC", "TL", "YN", "NE", "BC", "CC", "CM", "CH", "DG", "GR", "JC", "MN", "NT", "OC", "QT", "SP", "SR", "TS", "VZ", "ZS"};
    String[] ENUM = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32"};

    public String CURP(String crp) {
        if (SonLetras(crp.substring(0, 4))) {
            if (SonNumeros(crp.substring(4, 10))) {
                if (Genero(crp.substring(10, 11))) {
                    if (LugarNac(crp.substring(11, 13))) {
                        if (SonLetras(crp.substring(13, 16))) {
                            if (SonNumeros(crp.substring(16, 18))) {
                                return "1";
                            } else {
                                return "Verifique los ultimos dos digitos";
                            }
                        } else {
                            return "Verifique del digto 13 al 16";
                        }
                    } else {
                        return "verifique lugar de nacimiento";
                    }
                } else {
                    return "Verifique Sexo";
                }
            } else {
                return "hay un error en la fecha de nacimiento";
            }
        } else {
            return "Hay un error en los primeros 4 digitos";
        }
    }

    public String INE(String ine) {
        if (SonLetras(ine.substring(0, 6))) {
            if (SonNumeros(ine.substring(6, 12))) {
                if (LugarNacnUM(ine.substring(12, 14))) {
                    if (Genero(ine.substring(14, 15))) {
                        if (SonNumeros(ine.substring(15, 18))) {
                            return "1";
                        } else {
                            return "Verifique los ultimos 3 digitos";
                        }
                    } else {
                        return "Verifique Sexo";
                    }
                } else {
                    return "Verifique Lugar de Nacimiento";
                }
            } else {
                return "Verifique Fecha de nacimiento";
            }
        } else {
            return "Verifique los primeros 6 digitos";
        }
    }

    public boolean LugarNacnUM(String str) {
        for (int i = 0; i < ENUM.length; i++) {
            if (str.equals(ENUM[i])) {
                return true;
            }
        }
        return false;
    }

    public static boolean SonLetras(String cadena) {
        for (int x = 0; x < cadena.length(); x++) {
            char c = cadena.charAt(x);
            // Si no está entre a y z, ni entre A y Z, ni es un espacio
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ')) {
                return false;
            }
        }
        return true;
    }

    public boolean SonNumeros(String str) {
        boolean isNumeric = true;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                isNumeric = false;
            }
        }
        return isNumeric;
    }

    public boolean MesNac(String str) {
        if (Integer.parseInt(str) > 00 && Integer.parseInt(str) <= 12) {
            return true;
        }
        return false;
    }

    public boolean DiaNac(String str) {
        if (Integer.parseInt(str) > 00 && Integer.parseInt(str) <= 31) {
            return true;
        }
        return false;
    }

    public boolean LugarNac(String str) {
        for (int i = 0; i < EN.length; i++) {
            if (str.equals(EN[i])) {
                return true;
            }
        }
        return false;
    }

    public boolean Genero(String str) {
        if (str.equals("H") || str.equals("M")) {
            return true;
        }
        return false;
    }
}