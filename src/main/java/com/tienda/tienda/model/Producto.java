package com.tienda.tienda.model;

import jakarta.persistence.*;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @Column(name = "codigo_producto")
    private Long codigoProducto;

    @Column(name = "nombre_producto", nullable = false, length = 50)
    private String nombreProducto;

    @Column(name = "nitproveedor", nullable = false)
    private Long nitProveedor;

    @Column(name = "precio_compra", nullable = false)
    private Double precioCompra;

    @Column(name = "ivacompra", nullable = false)
    private Double ivaCompra;

    @Column(name = "precio_venta", nullable = false)
    private Double precioVenta;

    public Producto() {}

    public Long getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(Long codigoProducto) { this.codigoProducto = codigoProducto; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public Long getNitProveedor() { return nitProveedor; }
    public void setNitProveedor(Long nitProveedor) { this.nitProveedor = nitProveedor; }

    public Double getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(Double precioCompra) { this.precioCompra = precioCompra; }

    public Double getIvaCompra() { return ivaCompra; }
    public void setIvaCompra(Double ivaCompra) { this.ivaCompra = ivaCompra; }

    public Double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(Double precioVenta) { this.precioVenta = precioVenta; }
}