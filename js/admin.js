/**
 * Admin Panel Logic
 */

// Check Authentication
if (!sessionStorage.getItem('uem_auth')) {
    window.location.href = 'login.html';
}

const API_EVENTS = 'http://localhost:8081/api/events';
const API_UPLOAD = 'http://localhost:5000/upload';

function switchTab(tabName) {
    // Update menu
    document.querySelectorAll('.menu-item').forEach(item => item.classList.remove('active'));
    event.target.classList.add('active');

    // Update content
    document.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));
    document.getElementById(`${tabName}-tab`).classList.add('active');
}

/**
 * Handle Event Creation
 */
async function createEvent(e) {
    e.preventDefault();
    const form = e.target;
    const formData = new FormData(form);

    const eventData = {
        title: formData.get('title'),
        description: formData.get('description'),
        eventDate: formData.get('eventDate'),
        location: formData.get('location'),
        organizer: formData.get('organizer'),
        category: formData.get('category'),
        maxCapacity: parseInt(formData.get('maxCapacity')),
        imageUrl: formData.get('imageUrl') || null,
        status: 'UPCOMING',
        tags: ['UEM', formData.get('category')] // Default tags
    };

    try {
        const response = await fetch(API_EVENTS, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(eventData)
        });

        if (response.ok) {
            showNotification('Evento criado com sucesso!', 'success');
            form.reset();
        } else {
            throw new Error('Falha ao criar evento');
        }
    } catch (error) {
        console.error(error);
        showNotification('Erro ao criar evento. Verifique o backend.', 'error');
    }
}

/**
 * Handle File Upload
 */
async function handleFileUpload(input) {
    const file = input.files[0];
    if (!file) return;

    const statusDiv = document.getElementById('uploadStatus');
    statusDiv.innerHTML = '⏳ Enviando e processando...';

    const formData = new FormData();
    formData.append('file', file);

    try {
        const response = await fetch(API_UPLOAD, {
            method: 'POST',
            body: formData
        });

        const result = await response.json();

        if (response.ok) {
            statusDiv.innerHTML = `✅ Sucesso! Arquivo <strong>${result.filename}</strong> processado.`;
            showNotification('Documento enviado com sucesso!', 'success');
        } else {
            throw new Error(result.error || 'Erro no upload');
        }
    } catch (error) {
        console.error(error);
        statusDiv.innerHTML = `❌ Erro: ${error.message}`;
        showNotification('Erro ao enviar documento.', 'error');
    }
}

function showNotification(msg, type) {
    const notif = document.getElementById('notification');
    notif.textContent = msg;
    notif.className = `notification ${type}`;
    notif.style.display = 'block';

    setTimeout(() => {
        notif.style.display = 'none';
    }, 3000);
}
