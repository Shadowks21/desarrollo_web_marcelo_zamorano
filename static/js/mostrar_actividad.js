const activities = {
    'row-1': {
        description: 'Un emocionante torneo de básquetbol para todas las edades.',
        date: '2023-04-15',
        inicio: '19:00',
        termino: '22:00',
        comuna: 'San Antonio',
        sector: 'Multicancha de Planicies de Bellavista',
        tema: 'Deporte',
        organizador: 'Juan Pérez',
        totalFotos: 1,
        photos: [
            '../jscript/img/row_1.webp'
        ]
    },
    'row-2': {
        description: 'Un festival lleno de actividades culturales y recreativas.',
        date: '2023-04-16',
        inicio: '12:00',
        termino: '18:00',
        comuna: 'Macul',
        sector: 'Jardín de Macul',
        tema: 'Cultura',
        organizador: 'María González',
        totalFotos: 1,
        photos: [
            '../jscript/img/row_2.jpg'
        ]
    },
    'row-3': {
        description: 'Actividades recreativas para toda la familia.',
        date: '2023-04-17',
        inicio: '16:00',
        termino: '20:00',
        comuna: 'Temuco',
        sector: 'Diversos barrios de la comuna',
        tema: 'Recreación',
        organizador: 'Carlos López',
        totalFotos: 1,
        photos: [
            '../jscript/img/row_3.webp'
        ]
    },
    'row-4': {
        description: 'Diversión con juegos de agua para combatir el calor.',
        date: '2023-04-18',
        inicio: '11:00',
        termino: '19:00',
        comuna: 'Conchalí',
        sector: 'Plazas de agua en diversos sectores',
        tema: 'Juegos',
        organizador: 'Fernanda Díaz',
        totalFotos: 1,
        photos: [
            '../jscript/img/row_4.jpg'
        ]
    },
    'row-5': {
        description: 'Un concierto al aire libre con música en vivo.',
        date: '2023-04-19',
        inicio: '12:00',
        termino: '18:00',
        comuna: 'Ñuñoa',
        sector: 'Plaza Ñuñoa',
        tema: 'Música',
        organizador: 'Roberto Silva',
        totalFotos: 1,
        photos: [
            '../jscript/img/row_5.webp'
        ]
    }
};

const showActivityDetails = (rowId) => {
    const activity = activities[rowId];
    if (!activity) return;

    document.getElementById('activity-description').textContent = activity.description;
    document.getElementById('activity-date').textContent = activity.date;
    document.getElementById('activity-start-time').textContent = activity.inicio;
    document.getElementById('activity-end-time').textContent = activity.termino;
    document.getElementById('activity-commune').textContent = activity.comuna;
    document.getElementById('activity-sector').textContent = activity.sector;
    document.getElementById('activity-theme').textContent = activity.tema;
    document.getElementById('activity-photo-count').textContent = activity.totalFotos;
    document.getElementById('activity-organizer').textContent = activity.organizador;

    const photosContainer = document.getElementById('photos-container');
    photosContainer.innerHTML = ''; // Limpiar fotos anteriores
    activity.photos.forEach((photo) => {
        // console.log(photo); Sirvio para depurar.
        const img = document.createElement('img');
        img.src = photo;
        img.width = 320;
        img.height = 240;
        img.style.cursor = 'pointer';
        img.alt = 'Actividad';
        img.addEventListener('click', () => showPhotoModal(photo));
        photosContainer.appendChild(img);
    });
    document.getElementById('titulo').style.display = 'none';
    document.getElementById('activity-table').style.display = 'none';
    document.getElementById('activity-details').style.display = 'block';
};

const showPhotoModal = (photo) => {
    const modal = document.getElementById('photo-modal');
    let modalImg = document.createElement('img');
    modalImg.src = photo;
    modal.appendChild(modalImg);
    modal.style.display = 'block';
};

const closeModal = () => {
    const modal = document.getElementById('photo-modal');
    modal.style.display = 'none';
};

const backToList = () => {
    document.getElementById('titulo').style.display = 'block';
    document.getElementById('activity-details').style.display = 'none';
    document.getElementById('activity-table').style.display = 'table';
};

const backToHome = () => {
    window.location.href = 'index.html';
};

for (let i = 1; i <= 5; i++) {
    const rowId = `row-${i}`;
    document.getElementById(rowId).addEventListener('click', () => showActivityDetails(rowId));
}
document.getElementById('back-to-list').addEventListener('click', backToList);
document.getElementById('back-to-home').addEventListener('click', backToHome);
document.getElementById('close-modal').addEventListener('click', closeModal);
