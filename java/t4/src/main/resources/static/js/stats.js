async function renderLineChart() {
    try {
        const res = await fetch('/stats/actividades_por_dia');
        if (!res.ok) {
            throw new Error(res.statusText);
        }
        const data = await res.json();
        const labels = data.map(item => item.fecha);
        const values = data.map(item => item.total);
        const lineCtx = document.getElementById('lineChart').getContext('2d');
        new Chart(lineCtx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Cantidad de Actividades',
                    data: values,
                    borderColor: '#ff8c00',
                    backgroundColor: 'rgba(255, 140, 0, 0.2)',
                    borderWidth: 2
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: true,
                        position: 'top',
                        labels: { color: '#c9d1d9' }
                    }
                },
                scales: {
                    x: {
                        ticks: { color: '#c9d1d9' },
                        grid: { color: 'rgba(255, 140, 0, 0.1)' }
                    },
                    y: {
                        ticks: {
                            color: '#c9d1d9',
                            stepSize: 1,
                            callback: function(value) {
                                return Number.isInteger(value) ? value : '';
                            }
                        },
                        grid: { color: 'rgba(255, 140, 0, 0.1)' },
                        beginAtZero: true
                    }
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
        const labels = data.map(item => item.tema);
        const values = data.map(item => item.total);
        const pieCtx = document.getElementById('pieChart').getContext('2d');
        new Chart(pieCtx, {
            type: 'pie',
            data: {
                labels: labels,
                datasets: [{
                    data: values,
                    backgroundColor: ['#ff8c00', '#ffa500', '#ffb347', '#e76f51', '#2a9d8f']
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: true,
                        position: 'top',
                        labels: { color: '#c9d1d9' }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return context.label + ': ' + context.parsed + ' actividades';
                            }
                        }
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

        if (!data || data.length === 0) {
            document.getElementById('barChartContainer').innerHTML = '<p style="color:orange">No hay datos disponibles para mostrar.</p>';
            return;
        }

        const labels = data.map(item => item.mes);
        const manana = data.map(item => item.manana || 0);
        const mediodia = data.map(item => item.mediodia || 0);
        const tarde = data.map(item => item.tarde || 0);

        const barCtx = document.getElementById('barChart').getContext('2d');
        new Chart(barCtx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [
                    {
                        label: 'Mañana (6-12h)',
                        data: manana,
                        backgroundColor: '#ff8c00',
                        borderColor: '#ff8c00',
                        borderWidth: 1
                    },
                    {
                        label: 'Mediodía (12-18h)',
                        data: mediodia,
                        backgroundColor: '#ffa500',
                        borderColor: '#ffa500',
                        borderWidth: 1
                    },
                    {
                        label: 'Tarde (18-24h)',
                        data: tarde,
                        backgroundColor: '#ffb347',
                        borderColor: '#ffb347',
                        borderWidth: 1
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: true,
                        position: 'top',
                        labels: {
                            color: '#c9d1d9',
                            padding: 20
                        }
                    }
                },
                scales: {
                    x: {
                        ticks: { color: '#c9d1d9' },
                        grid: { color: 'rgba(255, 140, 0, 0.1)' }
                    },
                    y: {
                        ticks: {
                            color: '#c9d1d9',
                            stepSize: 1,
                            callback: function(value) {
                                return Number.isInteger(value) ? value : '';
                            }
                        },
                        grid: { color: 'rgba(255, 140, 0, 0.1)' },
                        beginAtZero: true
                    }
                },
                interaction: {
                    mode: 'index',
                    intersect: false
                }
            }
        });
    } catch (error) {
        console.error('Error cargando gráfico de barras:', error);
        document.getElementById('barChartContainer').innerHTML = '<p style="color:red">Error al cargar el gráfico de barras: ' + error.message + '</p>';
    }
}

// Llamar a las funciones para renderizar los 3 gráficos
renderLineChart();
renderPieChart();
renderBarChart();