package com.main.racerstore;

public class Product {
    private String codigo;
    private String categoria;
    private String nombre;
    private String descripcion;
    private String precio;
    private String imgrt;
    private String videoURL;
    private String ipserser = "http://192.168.1.41/RacerStore/img";
    private static String ip= "http://192.168.1.41";
    private String ubicacion;
    public Product(String codigo, String categoria, String nombre, String descripcion, String precio, String imgrt, String videoURL,String ubicacion) {
        this.codigo = codigo;
        this.categoria = categoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imgrt = imgrt;
        this.videoURL=videoURL;
        this.ubicacion=ubicacion;
    }
    public Product(String codigo, String categoria, String nombre, String descripcion, String precio, String imgrt,String ubicacion) {
        this.codigo = codigo;
        this.categoria = categoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imgrt = imgrt;
        this.ubicacion = ubicacion;
    }
    public String getCodigo() {
        return codigo;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public String getImgrt() {
        return imgrt;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getUbicacion(){return ubicacion;}

    public String getGlobalVariable() {
        return ipserser;
    }
    public static String getGlobalip() {
        return ip;
    }
}
