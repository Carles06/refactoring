package com.kreitek.refactor.mal;

class  Main
{
    public static void main(String args[]) {
        menu();
        dniCorrecto();
        dniIncorrecto();
        nieCorrecto();
        nieIncorrecto();
        cifCorrecto();
        cifIncorrecto();

    }

    private static void menu() {

        System.out.println("=====================");
        System.out.println("Vamos a refactorizar!");
        System.out.println("=====================");
    }

    private static void dniCorrecto() {
        DocumentoIdentificativo dniCorrecto = new DocumentoIdentificativo(TipoDocumentoIdentificativo.DNI, "11111111H", null);
        Boolean esValido = (dniCorrecto.validarDNI() == 1);
        System.out.println("DNI " + dniCorrecto.getNumDI() + " es: " + esValido);
    }
    private static void dniIncorrecto() {
        DocumentoIdentificativo dniIncorrecto01 = new DocumentoIdentificativo(TipoDocumentoIdentificativo.DNI, "24324356A", null);
        Boolean esValido01 = (dniIncorrecto01.validarDNI() == 1);
        System.out.println("DNI " + dniIncorrecto01.getNumDI() + " es: " + esValido01);

    }
    private static void nieCorrecto() {
        DocumentoIdentificativo nieCorrecto = new DocumentoIdentificativo(TipoDocumentoIdentificativo.NIE, "X0932707B", null);
        Boolean esValidoNie = (nieCorrecto.validarNIE() == 1);
        System.out.println("NIE " + nieCorrecto.getNumDI() + " es: " + esValidoNie);
    }
    private static void nieIncorrecto() {
        DocumentoIdentificativo nieIncorrecto = new DocumentoIdentificativo(TipoDocumentoIdentificativo.NIE, "Z2691139Z", null);
        Boolean esValidoNieIncorrecto = (nieIncorrecto.validarNIE() == 1);
        System.out.println("NIE " + nieIncorrecto.getNumDI() + " es: " + esValidoNieIncorrecto);
    }
    private static void cifCorrecto() {
        DocumentoIdentificativo cifCorrecto = new DocumentoIdentificativo(TipoDocumentoIdentificativo.CIF, "W9696294I", null);
        Boolean esValidoCIF = (cifCorrecto.validarCIF() == 1);
        System.out.println("CIF " + cifCorrecto.getNumDI() + " es: " + esValidoCIF);

    }
    private static void cifIncorrecto() {
        DocumentoIdentificativo cifIncorrecto = new DocumentoIdentificativo(TipoDocumentoIdentificativo.CIF, "W9696294A", null);
        Boolean esValidoCifIncorrecto = (cifIncorrecto.validarCIF() == 1);
        System.out.println("CIF " + cifIncorrecto.getNumDI() + " es: " + esValidoCifIncorrecto);
    }
}