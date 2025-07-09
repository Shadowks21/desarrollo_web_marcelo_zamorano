// Funciones para integrar Flask con la API de Java

const JAVA_API_URL = 'http://localhost:8080/api';

// Función para buscar actividades usando la API de Java
async function buscarActividadesJava(nombre = '') {
    try {
        const response = await fetch(`${JAVA_API_URL}/actividades/buscar?nombre=${encodeURIComponent(nombre)}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error al buscar actividades:', error);
        throw error;
    }
}

// Función para obtener estadísticas desde Java
async function obtenerEstadisticasJava() {
    try {
        const response = await fetch(`${JAVA_API_URL}/stats`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error al obtener estadísticas:', error);
        throw error;
    }
}

// Función para búsqueda avanzada
async function busquedaAvanzadaJava(filtros) {
    try {
        const params = new URLSearchParams();
        
        if (filtros.nombre) params.append('nombre', filtros.nombre);
        if (filtros.comunaId) params.append('comunaId', filtros.comunaId);
        if (filtros.regionId) params.append('regionId', filtros.regionId);
        if (filtros.fechaInicio) params.append('fechaInicio', filtros.fechaInicio);
        if (filtros.fechaFin) params.append('fechaFin', filtros.fechaFin);
        if (filtros.page) params.append('page', filtros.page);
        if (filtros.size) params.append('size', filtros.size);

        const response = await fetch(`${JAVA_API_URL}/actividades/buscar-avanzado?${params}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error en búsqueda avanzada:', error);
        throw error;
    }
}

// Función para obtener actividades activas
async function obtenerActividadesActivas(page = 0, size = 10) {
    try {
        const response = await fetch(`${JAVA_API_URL}/actividades/activas?page=${page}&size=${size}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error al obtener actividades activas:', error);
        throw error;
    }
}

// Función para verificar el estado de la API de Java
async function verificarEstadoJava() {
    try {
        const response = await fetch(`${JAVA_API_URL}/health`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error al verificar estado de Java:', error);
        return null;
    }
}

// Función para crear un widget de búsqueda que use la API de Java
function crearWidgetBusquedaJava(containerId) {
    const container = document.getElementById(containerId);
    if (!container) {
        console.error(`Container con ID ${containerId} no encontrado`);
        return;
    }

    container.innerHTML = `
        <div class="java-search-widget">
            <h4>Búsqueda Avanzada (Java API)</h4>
            <div class="row">
                <div class="col-md-6">
                    <input type="text" id="java-search-input" class="form-control" placeholder="Buscar actividades...">
                </div>
                <div class="col-md-3">
                    <button onclick="realizarBusquedaJava()" class="btn btn-primary">Buscar</button>
                </div>
                <div class="col-md-3">
                    <button onclick="mostrarEstadisticasJava()" class="btn btn-info">Stats</button>
                </div>
            </div>
            <div id="java-search-results" class="mt-3"></div>
        </div>
    `;
}

// Función para realizar búsqueda y mostrar resultados
async function realizarBusquedaJava() {
    const input = document.getElementById('java-search-input');
    const resultsDiv = document.getElementById('java-search-results');
    
    if (!input || !resultsDiv) return;

    const searchTerm = input.value.trim();
    
    try {
        resultsDiv.innerHTML = '<div class="text-center"><div class="spinner-border" role="status"></div></div>';
        
        const actividades = await buscarActividadesJava(searchTerm);
        
        if (actividades.length === 0) {
            resultsDiv.innerHTML = '<div class="alert alert-info">No se encontraron actividades</div>';
            return;
        }

        let html = '<div class="table-responsive"><table class="table table-striped"><thead><tr>';
        html += '<th>ID</th><th>Nombre</th><th>Email</th><th>Fecha</th><th>Acciones</th>';
        html += '</tr></thead><tbody>';

        actividades.forEach(actividad => {
            html += `<tr>
                <td>${actividad.id}</td>
                <td>${actividad.nombre}</td>
                <td>${actividad.email}</td>
                <td>${new Date(actividad.diaHoraInicio).toLocaleDateString()}</td>
                <td>
                    <a href="/actividad/${actividad.id}" class="btn btn-sm btn-outline-primary">Ver</a>
                    <a href="http://localhost:8080/java/actividad/${actividad.id}" target="_blank" class="btn btn-sm btn-outline-info">Java</a>
                </td>
            </tr>`;
        });

        html += '</tbody></table></div>';
        resultsDiv.innerHTML = html;
        
    } catch (error) {
        resultsDiv.innerHTML = '<div class="alert alert-danger">Error al buscar actividades</div>';
    }
}

// Función para mostrar estadísticas de Java
async function mostrarEstadisticasJava() {
    const resultsDiv = document.getElementById('java-search-results');
    if (!resultsDiv) return;

    try {
        resultsDiv.innerHTML = '<div class="text-center"><div class="spinner-border" role="status"></div></div>';
        
        const stats = await obtenerEstadisticasJava();
        
        let html = '<div class="row">';
        
        // Estadísticas por tema
        if (stats.actividades_por_tema) {
            html += '<div class="col-md-6"><h5>Actividades por Tema</h5><ul class="list-group">';
            stats.actividades_por_tema.forEach(item => {
                html += `<li class="list-group-item d-flex justify-content-between align-items-center">
                    ${item.tema}
                    <span class="badge bg-primary rounded-pill">${item.cantidad}</span>
                </li>`;
            });
            html += '</ul></div>';
        }
        
        // Estadísticas por comuna
        if (stats.actividades_por_comuna) {
            html += '<div class="col-md-6"><h5>Actividades por Comuna</h5><ul class="list-group">';
            stats.actividades_por_comuna.slice(0, 5).forEach(item => {
                html += `<li class="list-group-item d-flex justify-content-between align-items-center">
                    ${item.comuna}
                    <span class="badge bg-success rounded-pill">${item.cantidad}</span>
                </li>`;
            });
            html += '</ul></div>';
        }
        
        html += '</div>';
        resultsDiv.innerHTML = html;
        
    } catch (error) {
        resultsDiv.innerHTML = '<div class="alert alert-danger">Error al obtener estadísticas</div>';
    }
}

// Función para verificar conectividad con Java al cargar la página
async function verificarConectividadJava() {
    const estado = await verificarEstadoJava();
    if (estado) {
        console.log('✅ Conectado a Java API:', estado);
        
        // Mostrar indicador de conexión
        const indicator = document.createElement('div');
        indicator.innerHTML = `
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle"></i> Conectado a Java API
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        `;
        document.body.insertBefore(indicator, document.body.firstChild);
        
        // Auto-hide después de 3 segundos
        setTimeout(() => {
            indicator.remove();
        }, 3000);
    } else {
        console.warn('❌ No se pudo conectar a Java API');
    }
}

// Ejecutar verificación al cargar la página
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', verificarConectividadJava);
} else {
    verificarConectividadJava();
}

// Exportar funciones para uso global
window.JavaAPI = {
    buscarActividades: buscarActividadesJava,
    obtenerEstadisticas: obtenerEstadisticasJava,
    busquedaAvanzada: busquedaAvanzadaJava,
    obtenerActividadesActivas: obtenerActividadesActivas,
    verificarEstado: verificarEstadoJava,
    crearWidgetBusqueda: crearWidgetBusquedaJava
};
