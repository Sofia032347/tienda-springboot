package com.tienda.tienda.controller;

import com.tienda.tienda.model.Producto;
import com.tienda.tienda.repository.ProductoRepository;
import com.tienda.tienda.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    // LISTAR + BUSCAR
    @GetMapping
    public String listar(@RequestParam(required = false) String buscar, Model model) {
        List<Producto> listaProductos;

        if (buscar != null && !buscar.isEmpty()) {
            listaProductos = productoRepository.findByNombreProductoContainingIgnoreCase(buscar);
        } else {
            listaProductos = productoRepository.findAll();
        }

        model.addAttribute("listaProductos", listaProductos);
        model.addAttribute("buscar", buscar);
        return "productos";
    }

    // CARGAR CSV
    @PostMapping("/cargar-csv")
    public String cargarCsv(@RequestParam("archivo") MultipartFile archivo, Model model) {

        // Validar que sea un archivo CSV
        String nombreArchivo = archivo.getOriginalFilename();
        if (nombreArchivo == null || !nombreArchivo.toLowerCase().endsWith(".csv")) {
            model.addAttribute("error", "El archivo debe ser un CSV (.csv).");
            model.addAttribute("listaProductos", productoRepository.findAll());
            return "productos";
        }

        List<String> errores = new ArrayList<>();
        List<Producto> productosValidos = new ArrayList<>();
        int lineaNum = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                lineaNum++;

                // Saltar línea de encabezado si existe
                if (lineaNum == 1 && linea.toLowerCase().contains("codigo")) {
                    continue;
                }

                // Saltar líneas vacías
                if (linea.trim().isEmpty()) continue;

                String[] campos = linea.split(",");

                if (campos.length < 6) {
                    errores.add("Línea " + lineaNum + ": formato incorrecto, se esperan 6 columnas.");
                    continue;
                }

                try {
                    Long codigoProducto = Long.parseLong(campos[0].trim());
                    String nombreProducto = campos[1].trim();
                    Long nitProveedor = Long.parseLong(campos[2].trim());
                    Double precioCompra = Double.parseDouble(campos[3].trim());
                    Double ivaCompra = Double.parseDouble(campos[4].trim());
                    Double precioVenta = Double.parseDouble(campos[5].trim());

                    // Validar que el NIT exista en la base de datos de proveedores
                    if (!proveedorRepository.existsByNit(String.valueOf(nitProveedor))) {
                        errores.add("Línea " + lineaNum + ": NIT " + nitProveedor
                                + " no existe en la base de datos de proveedores.");
                        continue;
                    }

                    Producto p = new Producto();
                    p.setCodigoProducto(codigoProducto);
                    p.setNombreProducto(nombreProducto);
                    p.setNitProveedor(nitProveedor);
                    p.setPrecioCompra(precioCompra);
                    p.setIvaCompra(ivaCompra);
                    p.setPrecioVenta(precioVenta);

                    productosValidos.add(p);

                } catch (NumberFormatException e) {
                    errores.add("Línea " + lineaNum + ": datos numéricos inválidos.");
                }
            }

        } catch (Exception e) {
            model.addAttribute("error", "Error al leer el archivo: " + e.getMessage());
            model.addAttribute("listaProductos", productoRepository.findAll());
            return "productos";
        }

        // Guardar los productos válidos
        if (!productosValidos.isEmpty()) {
            productoRepository.saveAll(productosValidos);
        }

        // Preparar resumen
        String resumen = "Carga completada: " + productosValidos.size()
                + " producto(s) cargado(s) correctamente.";
        if (!errores.isEmpty()) {
            resumen += " " + errores.size() + " línea(s) con error.";
        }

        model.addAttribute("mensaje", resumen);
        model.addAttribute("erroresCsv", errores);
        model.addAttribute("listaProductos", productoRepository.findAll());
        return "productos";
    }
}