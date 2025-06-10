package com.inventario.productos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.inventario")
public class InventarioApiSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventarioApiSpringBootApplication.class, args);
	}

}
