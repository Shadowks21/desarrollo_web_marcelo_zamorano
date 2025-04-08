const activitiesByDay = {
    '2023-04-15': 2,
    '2023-04-16': 3,
    '2023-04-17': 1,
    '2023-04-18': 4,
    '2023-04-19': 2
};

const activitiesByType = {
    'Deporte': 5,
    'Cultura': 3,
    'Recreación': 4,
    'Juegos': 2,
    'Música': 1
};

const activitiesByMonthAndTime = {
    'Enero': { mannana: 2, mediodia: 3, tarde: 1 },
    'Febrero': { mannana: 1, mediodia: 2, tarde: 3 },
    'Marzo': { mannana: 3, mediodia: 1, tarde: 2 }
};

// Gráfico de líneas: Cantidad de actividades por día
const lineCtx = document.getElementById('lineChart').getContext('2d');
new Chart(lineCtx, {
    type: 'line',
    data: {
        labels: Object.keys(activitiesByDay),
        datasets: [{
            label: 'Cantidad de Actividades',
            data: Object.values(activitiesByDay),
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
                labels: {
                    color: '#c9d1d9'
                }
            }
        },
        scales: {
            x: {
                ticks: { color: '#c9d1d9' }
            },
            y: {
                ticks: { color: '#c9d1d9' }
            }
        }
    }
});

// Gráfico de torta: Total de actividades por tipo
const pieCtx = document.getElementById('pieChart').getContext('2d');
new Chart(pieCtx, {
    type: 'pie',
    data: {
        labels: Object.keys(activitiesByType),
        datasets: [{
            data: Object.values(activitiesByType),
            backgroundColor: ['#03e9f4', '#029dbb', '#f4a261', '#e76f51', '#2a9d8f']
        }]
    },
    options: {
        responsive: true,
        plugins: {
            legend: {
                display: true,
                position: 'top',
                labels: {
                    color: '#c9d1d9'
                }
            }
        }
    }
});

// Gráfico de barras: Cantidad de actividades por mes y horario
const barCtx = document.getElementById('barChart').getContext('2d');
new Chart(barCtx, {
    type: 'bar',
    data: {
        labels: Object.keys(activitiesByMonthAndTime),
        datasets: [
            {
                label: 'mannana',
                data: Object.values(activitiesByMonthAndTime).map(item => item.mannana),
                backgroundColor: '#03e9f4'
            },
            {
                label: 'Mediodía',
                data: Object.values(activitiesByMonthAndTime).map(item => item.mediodia),
                backgroundColor: '#029dbb'
            },
            {
                label: 'Tarde',
                data: Object.values(activitiesByMonthAndTime).map(item => item.tarde),
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
                labels: {
                    color: '#c9d1d9'
                }
            }
        },
        scales: {
            x: {
                ticks: { color: '#c9d1d9' }
            },
            y: {
                ticks: { color: '#c9d1d9' }
            }
        }
    }
});