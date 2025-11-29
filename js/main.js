document.addEventListener('DOMContentLoaded', () => {
    console.log('UEM Assistant Loaded');

    // Highlight active menu item based on current URL
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('nav a');

    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (currentPath.includes(href) && href !== 'index.html') {
            link.classList.add('active');
        } else if ((currentPath.endsWith('/') || currentPath.endsWith('index.html')) && href === 'index.html') {
            link.classList.add('active');
        }
    });

    // Chat Functionality
    const chatForm = document.getElementById('chat-form');
    if (chatForm) {
        const chatInput = document.getElementById('chat-input');
        const chatMessages = document.getElementById('chat-messages');

        chatForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const message = chatInput.value.trim();
            if (!message) return;

            // Add User Message
            addMessage(message, 'user');
            chatInput.value = '';

            // Show loading state
            const loadingDiv = document.createElement('div');
            loadingDiv.classList.add('message', 'bot');
            loadingDiv.textContent = 'Digitando...';
            loadingDiv.id = 'loading-message';
            chatMessages.appendChild(loadingDiv);
            chatMessages.scrollTop = chatMessages.scrollHeight;

            // Call Backend API
            fetch('http://127.0.0.1:5000/chat', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ message: message })
            })
                .then(response => response.json())
                .then(data => {
                    // Remove loading message
                    const loadingMsg = document.getElementById('loading-message');
                    if (loadingMsg) loadingMsg.remove();

                    if (data.error) {
                        addMessage("Erro: " + data.error, 'bot');
                    } else {
                        addMessage(data.response, 'bot');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    const loadingMsg = document.getElementById('loading-message');
                    if (loadingMsg) loadingMsg.remove();
                    addMessage("Desculpe, ocorreu um erro ao conectar com o servidor.", 'bot');
                });
        });

        function addMessage(text, sender) {
            const div = document.createElement('div');
            div.classList.add('message', sender);

            // Simple Markdown Formatter
            if (sender === 'bot') {
                let formattedText = text
                    // Escape HTML first to prevent XSS
                    .replace(/&/g, "&amp;")
                    .replace(/</g, "&lt;")
                    .replace(/>/g, "&gt;")
                    // Bold: **text**
                    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
                    // Italic: *text*
                    .replace(/\*(.*?)\*/g, '<em>$1</em>')
                    // Lists: - item
                    .replace(/^\s*-\s+(.*)$/gm, '<li>$1</li>')
                    // Line breaks
                    .replace(/\n/g, '<br>')
                    // Links: [text](url)
                    .replace(/\[(.*?)\]\((.*?)\)/g, '<a href="$2" target="_blank" style="color: var(--uem-green-primary); text-decoration: underline;">$1</a>')
                    // Raw URLs: http://... or https://...
                    .replace(/(https?:\/\/[^\s<]+)/g, (match) => {
                        // Avoid double-linking if already inside an <a> tag (simple check)
                        if (match.includes('href="')) return match;
                        return `<a href="${match}" target="_blank" style="color: var(--uem-green-primary); text-decoration: underline;">${match}</a>`;
                    });

                // Wrap lists if they exist
                if (formattedText.includes('<li>')) {
                    formattedText = formattedText.replace(/((<li>.*<\/li>)+)/g, '<ul>$1</ul>');
                }

                div.innerHTML = formattedText;
            } else {
                div.textContent = text;
            }

            chatMessages.appendChild(div);
            chatMessages.scrollTop = chatMessages.scrollHeight;
        }
    }

    // Modal Functionality
    const modal = document.getElementById('event-modal');
    const closeBtn = document.querySelector('.btn-close');
    const eventButtons = document.querySelectorAll('.btn-view-event');

    if (modal && closeBtn && eventButtons.length > 0) {
        function openModal(title, date, location, desc) {
            document.getElementById('modal-title').textContent = title;
            document.getElementById('modal-date').textContent = date;
            document.getElementById('modal-location').textContent = location;
            document.getElementById('modal-desc').textContent = desc;

            modal.classList.add('active');
            document.body.style.overflow = 'hidden';
        }

        function closeModal() {
            modal.classList.remove('active');
            document.body.style.overflow = '';
        }

        eventButtons.forEach(btn => {
            btn.addEventListener('click', () => {
                const card = btn.closest('.event-card');
                const title = card.querySelector('.event-title').textContent;
                const date = card.querySelector('.event-date').textContent;
                const location = card.querySelector('.event-location span').textContent;
                // Get full description from data attribute or just expand the short one
                const desc = card.querySelector('.event-desc').textContent + " Esta é uma descrição completa do evento simulada para demonstração do modal. O evento contará com a presença de diversos palestrantes e especialistas da área.";

                openModal(title, date, location, desc);
            });
        });

        closeBtn.addEventListener('click', closeModal);

        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                closeModal();
            }
        });

        // Close on Escape key
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape' && modal.classList.contains('active')) {
                closeModal();
            }
        });
    }

    // Documents Module - Fetch from Backend
    const docList = document.querySelector('.documents-list');
    if (docList) {
        fetch('http://127.0.0.1:5000/documents')
            .then(response => response.json())
            .then(data => {
                // Handle new response format with documents array
                const documents = data.documents || data;

                if (documents && documents.length > 0) {
                    docList.innerHTML = ''; // Clear hardcoded/loading items

                    documents.forEach(doc => {
                        const docItem = document.createElement('div');
                        docItem.className = 'doc-item';
                        docItem.innerHTML = `
                            <div class="doc-icon">
                                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                                    <polyline points="14 2 14 8 20 8"></polyline>
                                </svg>
                            </div>
                            <div class="doc-info">
                                <h3 class="doc-title">${doc.name}</h3>
                                <p class="doc-meta">${doc.type} • ${doc.size}</p>
                            </div>
                            <button class="btn-download" onclick="window.open('../${doc.name}', '_blank')">Visualizar</button>
                        `;
                        docList.appendChild(docItem);
                    });
                } else {
                    docList.innerHTML = '<div class="doc-item" style="justify-content: center; color: #666;"><p>Nenhum documento disponível no momento.</p></div>';
                }
            })
            .catch(err => {
                console.error('Error fetching documents:', err);
                docList.innerHTML = '<div class="doc-item" style="justify-content: center; color: #d32f2f;"><p>Erro ao carregar documentos. Por favor, tente novamente.</p></div>';
            });
    }
});
