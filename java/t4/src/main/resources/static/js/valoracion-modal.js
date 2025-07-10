class ValoracionModal {
    constructor() {
        this.selectedRating = 0;
        this.currentActivityId = null;
        this.modal = null;
        this.stars = null;
        this.evaluarBtn = null;
        this.cancelarBtn = null;

        this.init();
    }

    init() {
        document.addEventListener('DOMContentLoaded', () => {
            this.initElements();
            this.bindEvents();
        });
    }

    initElements() {
        this.modal = document.getElementById('valoracionModal');
        this.stars = document.querySelectorAll('.star');
        this.evaluarBtn = document.getElementById('evaluarBtn');
        this.cancelarBtn = document.getElementById('cancelarBtn');
    }

    bindEvents() {
        // Abrir modal
        document.querySelectorAll('.btn-valorar').forEach(btn => {
            btn.addEventListener('click', (e) => this.openModal(e));
        });

        // Cerrar modal
        this.cancelarBtn.addEventListener('click', () => this.closeModal());

        // Cerrar modal al hacer clic fuera
        this.modal.addEventListener('click', (e) => {
            if (e.target === this.modal) {
                this.closeModal();
            }
        });

        // Manejo de estrellas
        this.stars.forEach((star, index) => {
            star.addEventListener('mouseenter', () => this.highlightStars(index + 1));
            star.addEventListener('mouseleave', () => this.handleStarMouseLeave());
            star.addEventListener('click', () => this.selectRating(index + 1));
        });

        // Evaluar
        this.evaluarBtn.addEventListener('click', () => this.submitRating());
    }

    openModal(event) {
        this.currentActivityId = event.target.closest('.btn-valorar').dataset.id;
        this.selectedRating = 0;
        this.resetStars();
        this.evaluarBtn.disabled = true;
        this.modal.style.display = 'flex';
    }

    closeModal() {
        this.modal.style.display = 'none';
        this.resetStars();
        this.selectedRating = 0;
    }

    highlightStars(rating) {
        this.stars.forEach((star, index) => {
            if (index < rating) {
                star.classList.add('filled');
                star.textContent = '★';
            } else {
                star.classList.remove('filled');
                star.textContent = '☆';
            }
        });
    }

    handleStarMouseLeave() {
        if (this.selectedRating === 0) {
            this.resetStars();
        } else {
            this.highlightStars(this.selectedRating);
        }
    }

    selectRating(rating) {
        this.selectedRating = rating;
        this.highlightStars(rating);
        this.evaluarBtn.disabled = false;
    }

    resetStars() {
        this.stars.forEach(star => {
            star.classList.remove('filled');
            star.textContent = '☆';
        });
    }

    async submitRating() {
        if (this.selectedRating >= 1 && this.selectedRating <= 7) {
            try {
                const response = await fetch('/valorar', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        actividadId: this.currentActivityId,
                        nota: this.selectedRating
                    })
                });

                const data = await response.json();

                if (data.success) {
                    // Actualizar la nota promedio en la tarjeta
                    const notaElement = document.querySelector(`.nota-promedio[data-id="${this.currentActivityId}"]`);
                    if (notaElement) {
                        notaElement.textContent = data.notaPromedio.toFixed(1);
                    }

                    this.closeModal();
                } else {
                    alert('Error al enviar la valoración: ' + data.message);
                }
            } catch (error) {
                console.error('Error:', error);
                alert('Error al enviar la valoración');
            }
        }
    }
}

// Exportar la clase para uso global
window.ValoracionModal = ValoracionModal;

// Instanciar automáticamente
new ValoracionModal();