<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="css/styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Proyecto Final - SRI y Documentos</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
</head>
<body>

<header>
    <h1>Gestión de Contribuyentes y Documentos</h1>
</header>

<main>
    <div class="btn-container">
        <a href="contribuyente" class="btn">Ver Contribuyentes (GET)</a>
        <a href="formularioContribuyente.jsp" class="btn">Registrar Contribuyente</a>
        <a href="formularioDocumento.jsp" class="btn">Registrar Documento</a>
    </div>

    <section id="tabla-datos">
        <h2>Listado de Contribuyentes</h2>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>RUC</th>
                <th>Nombre</th>
                <th>Dirección</th>
                <th>Teléfono</th>
                <th>Email</th>
            </tr>
            </thead>
            <tbody id="tabla-body">
            </tbody>
        </table>
        <button id="toggle-mode" className="btn">Modo Oscuro</button>
    </section>
</main>

<footer>
    <p>&copy; 2025 Proyecto Final - SRI y Documentos</p>
</footer>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        fetch("/proyectoFinal/contribuyente")
            .then(response => response.json())
            .then(data => {
                console.log("Datos recibidos:", data); // Verificación en consola

                let tbody = document.getElementById("tabla-body");
                tbody.innerHTML = "";

                if (!Array.isArray(data)) {
                    console.error("Error: La respuesta del servidor no es un array.");
                    return;
                }

                data.forEach(item => {
                    let row = document.createElement("tr");

                    let idCell = document.createElement("td");
                    let rucCell = document.createElement("td");
                    let nombreCell = document.createElement("td");
                    let direccionCell = document.createElement("td");
                    let telefonoCell = document.createElement("td");
                    let emailCell = document.createElement("td");

                    idCell.textContent = item.id;
                    rucCell.textContent = item.ruc;
                    nombreCell.textContent = item.nombre;
                    direccionCell.textContent = item.direccion;
                    telefonoCell.textContent = item.telefono;
                    emailCell.textContent = item.email;

                    row.appendChild(idCell);
                    row.appendChild(rucCell);
                    row.appendChild(nombreCell);
                    row.appendChild(direccionCell);
                    row.appendChild(telefonoCell);
                    row.appendChild(emailCell);

                    tbody.appendChild(row);
                });
            })
            .catch(error => console.error("Error cargando datos:", error));
    });
</script>

<script>

    document.getElementById("toggle-mode").addEventListener("click", function () {
        document.body.classList.toggle("dark-mode");
    });
</script>
</body>
</html>