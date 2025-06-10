async function renderLineChart() {
    try {
        const res = await fetch('/stats/actividades_por_dia');
        if (!res.ok) {
            throw new Error(res.statusText);
        }
        const data = await res.json();
        const labels = data.map(item => item.fecha);
        const values = data.map(item => item.cantidad);
        const lineCtx = document.getElementById('lineChart').getContext('2d');
        new Chart(lineCtx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Cantidad de Actividades',
                    data: values,
                    borderColor: '#03e9f4',
                    backgroundColor: 'rgba(3, 233, 244, 0.2)',
                    borderWidth: 2
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        display: true,
                        position: 'top',
                        labels: { color: '#c9d1d9' }
                    }
                },
                scales: {
                    x: { ticks: { color: '#c9d1d9' } },
                    y: { ticks: { color: '#c9d1d9' } }
                }
            }
        });
    } catch (error) {
        console.error('Error cargando gráfico de líneas:', error);
        document.getElementById('lineChartContainer').innerHTML = '<p style="color:red">Error al cargar el gráfico de líneas.</p>';
    }
}

async function renderPieChart() {
    try {
        const res = await fetch('/stats/actividades_por_tipo');
        if (!res.ok) {
            throw new Error(res.statusText);
        }
        const data = await res.json();
        const labels = data.map(item => item.tipo);
        const values = data.map(item => item.cantidad);
        const pieCtx = document.getElementById('pieChart').getContext('2d');
        new Chart(pieCtx, {
            type: 'pie',
            data: {
                labels: labels,
                datasets: [{
                    data: values,
                    backgroundColor: ['#03e9f4', '#029dbb', '#f4a261', '#e76f51', '#2a9d8f']
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        display: true,
                        position: 'top',
                        labels: { color: '#c9d1d9' }
                    }
                }
            }
        });
    } catch (error) {
        console.error('Error cargando gráfico de torta:', error);
        document.getElementById('pieChartContainer').innerHTML = '<p style="color:red">Error al cargar el gráfico de torta.</p>';
    }
}

async function renderBarChart() {
    try {
        const res = await fetch('/stats/actividades_por_horario');
        if (!res.ok) {
            throw new Error(res.statusText);
        }
        const data = await res.json();
        const labels = data.map(item => item.mes);
        const manana = data.map(item => item.manana || item.mannana || 0);
        const mediodia = data.map(item => item.mediodia || 0);
        const tarde = data.map(item => item.tarde || 0);
        const barCtx = document.getElementById('barChart').getContext('2d');
        new Chart(barCtx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [
                    {
                        label: 'Mañana',
                        data: manana,
                        backgroundColor: '#03e9f4'
                    },
                    {
                        label: 'Mediodía',
                        data: mediodia,
                        backgroundColor: '#029dbb'
                    },
                    {
                        label: 'Tarde',
                        data: tarde,
                        backgroundColor: '#f4a261'
                    }
                ]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        display: true,
                        position: 'top',
                        labels: { color: '#c9d1d9' }
                    }
                },
                scales: {
                    x: { ticks: { color: '#c9d1d9' } },
                    y: { ticks: { color: '#c9d1d9' } }
                }
            }
        });
    } catch (error) {
        console.error('Error cargando gráfico de barras:', error);
        document.getElementById('barChartContainer').innerHTML = '<p style="color:red">Error al cargar el gráfico de barras.</p>';
    }
}

// Llamar a las funciones para renderizar los 3 gráficos
renderLineChart();
renderPieChart();
renderBarChart();