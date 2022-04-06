package com.kreitek.refactor.mal;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentoIdentificativo {

    private TipoDocumentoIdentificativo enumTipo;    // tipo de documento
    private String numDI;       // identificador del documento
    private Date fchValidez;     // fecha de validez del documento

    public DocumentoIdentificativo() {
    }

    // construye un DNI
    public DocumentoIdentificativo(TipoDocumentoIdentificativo tipo, String numDNI, Date fchValidez) {
        this.enumTipo = tipo;           // tipo de documento
        this.numDI = numDNI;           // asignamos el DNI
        this.fchValidez = fchValidez;   // asignamos la fecha de validez
    }

    public TipoDocumentoIdentificativo getEnumTipo() {
        return enumTipo;
    }

    public void setEnumTipo(TipoDocumentoIdentificativo enumTipo) {
        this.enumTipo = enumTipo;
    }

    public String getNumDI() {
        return numDI;
    }

    public void setNumDI(String numDI) {
        this.numDI = numDI;
    }

    public Date getFchValidez() {
        return fchValidez;
    }

    public void setFchValidez(Date fchValidez) {
        this.fchValidez = fchValidez;
    }

    // valida el  documento según su tipo
    // si es ok devuelve 1
    // si es nok devuelve 0
    // si pasa algo devuelve 99


    public int validarDNI(){
        // posibles letras en un DNI
        String dniChars="TRWAGMYFPDXBNJZSQVHLCKE";
        // los primeros 8 caracteres son números
        String intPartDNI = this.numDI.trim().replaceAll(" ", "").substring(0, 8);
        // el último es un dígito de control
        char ltrDNI = this.numDI.charAt(8);
        // calculamos el módulo de 23 de la parte numérica que debería ser el caracter en esa
        // posición en la lista de dniChar --> my code Rocks!!!
        int valNumDni = Integer.parseInt(intPartDNI) % 23;

        // comprobamos que  esté bien
        return extracted(dniChars, intPartDNI, ltrDNI, valNumDni);
    }


    private int extracted(String dniChars, String intPartDNI, char ltrDNI, int valNumDni) {
        if (this.numDI.length()!= 9 || isNumeric(intPartDNI) == false || dniChars.charAt(valNumDni)!= ltrDNI) {
            return 0;
        } else {
            return 1;
        }
    }


    public int validarCIF(){
        if (this.numDI != null) {
            final String cifUP = this.numDI.toUpperCase();

            // si el primer caracter no es uno de los válidos entonces ya fallamos
            if ("ABCDEFGHJKLMNPQRSUVW".indexOf(cifUP.charAt(0)) == -1) {
                return 0; // no cumple el primer char
            }

            Integer x1 = validarPatronCif(cifUP);
            if (x1 != null) return x1;

            final char primerCar = cifUP.charAt(0);
            final char ultimoCar = cifUP.charAt(cifUP.length() - 1);


            UltimoCaracter tipUltCar = getUltimoCaracter(primerCar, ultimoCar);
            if (tipUltCar == null) return 0; // no es una letra



            final String digitos = cifUP.substring(1, cifUP.length() - 1);

            final Integer total = calcularNUMEROS(digitos);

            Integer x = validarNumeroControl(ultimoCar, tipUltCar, total);
            if (x != null) return x;
            return 1; // OK
        }
        return 0; //NOK
    }

    private Integer validarPatronCif(String cifUP) {
        // si no cumple el patrón de CIF fallamos
        final Pattern mask = Pattern.compile("[ABCDEFGHJKLMNPQRSUVW][0-9]{7}[A-Z[0-9]]{1}");
        final Matcher matcher = mask.matcher(cifUP);
        if (!matcher.matches()) {
            return 0;
        }
        return null;
    }

    private Integer validarNumeroControl(char ultimoCar, UltimoCaracter tipUltCar, Integer total) {
        // calculo el número de control
        Integer numControl = 10 - (total % 10);


        int pos = numControl == 10? 0:numControl;

        final char carControl = "JABCDEFGHI".charAt(pos);

        // con el número de control calculado validamos
        if (tipUltCar == UltimoCaracter.NUMERO) {

            final Integer ultCar = Integer.parseInt(Character
                    .toString(ultimoCar));
            if (pos != ultCar.intValue()) {

                return 0;
            }

        } else if (tipUltCar == UltimoCaracter.LETRA) {
            if (carControl != ultimoCar) {
                return 0;
            }

        } else {
            // find all occurrences forward
            Integer ultCar = -1;

            ultCar = "JABCDEFGHI".indexOf(ultimoCar);

            if (ultCar < 0){
                ultCar = Integer.parseInt(Character.toString(ultimoCar));
                if (pos != ultCar.intValue()) {
                    return 0;
                }
            }
            if ((pos != ultCar.intValue()) && (carControl != ultimoCar)) {
                return 0;
            }
        }
        return null;
    }

    private Integer calcularNUMEROS(String digitos) {
        // sumo los pares
        Integer sumaPares = 0;
        for (int i = 1; i <= digitos.length() - 1; i = i + 2) {
            sumaPares += Integer.parseInt(digitos.substring(i, i + 1));
        }

        // sumo los impares
        Integer sumaImpares = 0;
        for (int i = 0; i <= digitos.length() - 1; i = i + 2) {
            Integer cal = Integer.parseInt(digitos.substring(i, i + 1)) * 2;
            if (cal.toString().length() > 1) {
                cal = Integer.parseInt(cal.toString().substring(0, 1))
                        + Integer.parseInt(cal.toString().substring(1, 2));
            }
            sumaImpares += cal;
        }

        // los sumo todos
        final Integer total = sumaPares + sumaImpares;
        return total;
    }

    private UltimoCaracter getUltimoCaracter(char primerCar, char ultimoCar) {
        UltimoCaracter tipUltCar;

        // si empiezo por P,Q, S, K o W la última letra tiene que ser una LETRA
        if (primerCar == 'P' || primerCar == 'Q' || primerCar == 'S' || primerCar == 'K' || primerCar == 'W') {
            tipUltCar = UltimoCaracter.LETRA;
            if (!(ultimoCar >= 'A' && ultimoCar <= 'Z')) {
                return null;
            }
            // si empiezo por A, B, E o H la última letra tiene que ser un número
        } else if (primerCar == 'A' || primerCar == 'B' || primerCar == 'E'
                || primerCar == 'H') {
            tipUltCar = UltimoCaracter.NUMERO;
            if (!(ultimoCar >= '0' && ultimoCar <= '9')) {
                return null;
            }
            // en otro caso la última letra puede ser cualquier cosa
        } else {
            tipUltCar = UltimoCaracter.AMBOS;
        }
        return tipUltCar;
    }

    public int validarNIE(){
        boolean esValido = comprobarValidacion();

        return esValido(esValido);
    }

    private boolean comprobarValidacion() {
        boolean esValido = false;
        int i = 1;
        int caracterASCII = 0;
        char letra = ' ';
        int miNIE = 0;
        int resto = 0;
        char[] asignacionLetra = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X','B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};

        String nie = this.numDI;

        if(nie.length() == 9 && Character.isLetter(nie.charAt(8))
                && nie.substring(0,1).equalsIgnoreCase("X")
                || nie.substring(0,1).equalsIgnoreCase("Y")
                || nie.substring(0,1).equalsIgnoreCase("Z")) {

            do {
                caracterASCII = nie.codePointAt(i);
                esValido = (caracterASCII > 47 && caracterASCII < 58);
                i++;
            } while(i < nie.length() - 1 && esValido);
        }

        if(esValido && nie.substring(0,1).equalsIgnoreCase("X")) {
            nie = "0" + nie.substring(1,9);
        } else if(esValido && nie.substring(0,1).equalsIgnoreCase("Y")) {
            nie = "1" + nie.substring(1,9);
        } else if(esValido && nie.substring(0,1).equalsIgnoreCase("Z")) {
            nie = "2" + nie.substring(1,9);
        }

        if(esValido) {
            letra = Character.toUpperCase(nie.charAt(8));
            miNIE = Integer.parseInt(nie.substring(1,8));
            resto = miNIE % 23;
            esValido = (letra == asignacionLetra[resto]);
        }
        return esValido;
    }

    private int esValido(boolean esValido) {
        if (esValido) {
            return 1;
        } else {
            return 0;
        }
    }


    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
