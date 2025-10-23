fetch("http://localhost:3000/productos")
  .then(res => res.json())
  .then(productos => {
    const contenedor = document.getElementById("ropa");
    contenedor.innerHTML = "";

    productos.forEach(prod => {
      contenedor.innerHTML += `
        <tr style="cursor:pointer;" onclick="window.location.href='detalleProducto.html?id=${prod.id}'">
          <td><img src="${prod.foto}" alt="${prod.nombre}" style="width:80px; height:80px;"></td>
          <td>${prod.nombre}</td>
          <td>$${prod.precio}</td>
        </tr>
      `;
    });
  })
  .catch(err => console.error("Error al cargar productos:", err));