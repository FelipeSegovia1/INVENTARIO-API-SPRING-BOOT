import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Navbar } from "../../componentes/Navbar/Navbar";

const API_BASE_URL = 'http://localhost:8080/api';

export function CrearProducto() {

    const navigate = useNavigate();

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [loadingCategories, setLoadingCategories] = useState(true);
    const [categorias, setCategorias] = useState([]);
    const [producto, setProducto] = useState({
        nombre: "",
        descripcion: "",
        precio: "",
        categoriaId: ""
    });

    useEffect(() => {
        fetch(`${API_BASE_URL}/categorias`)
            .then(response => {
                if (!response.ok) {
                    throw new Error("Error al obtener las categorías");
                }
                return response.json();
            })
            .then(data => {
                setCategorias(data);
                setLoadingCategories(false);
            })
            .catch(error => {
                console.error("Error al cargar las categorías:", error);
                setError("Error al cargar las categorías");
                setLoadingCategories(false);
            });
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setProducto(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        const productoParaEnviar = { ...producto };

        try {
            const response = await fetch(`${API_BASE_URL}/productos`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(productoParaEnviar)
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.message || "Error al crear el producto");
            }

            navigate('/inventario', {
                state: { mensaje: `Producto "${producto.nombre}" creado exitosamente.` }
            });
        } catch (err) {
            setError(err.message || "Error al crear el producto");
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <Navbar />

            <div className="crear-producto-container">
                <div className="form-card">
                    <h2>Crear Nuevo Producto</h2>

                    {error && (
                        <div className="error-message">
                            {error}
                            <button
                                onClick={() => setError(null)}
                                className="Error-close"
                            >
                                X
                            </button>
                        </div>
                    )}

                    {loadingCategories ? (
                        <p>Cargando categorías...</p>
                    ) : (
                        <form onSubmit={handleSubmit}>
                            <div className="form-row">
                                <div className="form-group">
                                    <label htmlFor="nombre">
                                        Nombre del producto <span className="required">*</span>
                                    </label>
                                    <input
                                        type="text"
                                        id="nombre"
                                        name="nombre"
                                        value={producto.nombre}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>
                            </div>

                            <button type="submit" disabled={loading}>
                                {loading ? "Creando..." : "Crear Producto"}
                            </button>
                        </form>
                    )}
                </div>
            </div>
        </>
    );
}
