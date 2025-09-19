// Chat functionality for Adlync
class ChatManager {
    constructor() {
        this.stompClient = null;
        this.currentChatId = null;
        this.currentUserId = null;
        this.currentPostId = null;
        this.currentOwnerId = null;
        this.isConnected = false;
        this.isOwner = false;
        this.ownerChats = [];
        this.activeOwnerChatId = null;
        
        this.initializeEventListeners();
    }

    initializeEventListeners() {
        // Message button click
        $('#messageBtn').on('click', () => {
            this.openChat();
        });

        // View Chats button click (for ad owners)
        $('#viewChatsBtn').on('click', () => {
            this.openOwnerChatList();
        });

        // Send message on button click
        $(document).on('click', 'button[onclick="sendMessage()"]', (e) => {
            e.preventDefault();
            this.sendMessage();
        });

        // Enter key to send message
        $('#messageInput').on('keypress', (e) => {
            if (e.which === 13) {
                this.sendMessage();
            }
        });

        // Owner message input events
        $('#ownerMessageInput').on('keypress', (e) => {
            if (e.which === 13) {
                this.sendOwnerMessage();
            }
        });

        $('#ownerSendBtn').on('click', () => {
            this.sendOwnerMessage();
        });

        // Chat list item click
        $(document).on('click', '.chat-list-item', (e) => {
            const chatId = $(e.currentTarget).data('chat-id');
            this.selectOwnerChat(chatId);
        });

        // Disconnect when modals close
        $('#chatModal').on('hidden.bs.modal', () => {
            this.disconnect();
        });

        $('#chatListModal').on('hidden.bs.modal', () => {
            this.disconnect();
        });

        // Check if user is ad owner when page loads
        $(document).ready(() => {
            setTimeout(() => {
                this.checkIfOwner();
            }, 1000);
        });
    }

    async openChat() {
        try {
            // Get user ID from cookie (use actual user ID, not msgId)
            this.currentUserId = getCookie("userId");
            
            if (!this.currentUserId) {
                // Fallback: try to get user ID from token if not in cookie
                const token = getCookie("token");
                if (token) {
                    await this.getUserIdFromToken(token);
                }
            }
            
            // Get current ad and seller data
            this.currentPostId = window.currentAdData?.post_id;
            this.currentOwnerId = window.currentSellerData?.id;

            if (!this.currentUserId) {
                alert('Please sign in to chat with the seller.');
        return;
    }

            console.log("Current User ID:", this.currentUserId);
            console.log("Current Post ID:", this.currentPostId);
            console.log("Current Owner ID:", this.currentOwnerId);

            if (!this.currentPostId || !this.currentOwnerId) {
                alert('Unable to start chat. Ad or seller information is missing.');
                return;
            }

            if (this.currentUserId == this.currentOwnerId) {
                alert('You cannot chat with yourself.');
                return;
            }

            // Check if chat already exists or create new one
            await this.createOrGetChat();

            // Show modal
            $('#chatModal').modal('show');

            // Load chat history
            await this.loadChatHistory();

            // Connect to WebSocket
            await this.connect();

        } catch (error) {
            console.error('Error opening chat:', error);
            alert(`Failed to open chat: ${error.message}`);
        }
    }

    async createOrGetChat() {
        try {
            console.log(`Checking for existing chat: postId=${this.currentPostId}, userId=${this.currentUserId}, ownerId=${this.currentOwnerId}`);
            
            // First check if chat exists
            const existingChatResponse = await fetch(
                `http://localhost:8080/api/chat/between/${this.currentPostId}/${this.currentUserId}/${this.currentOwnerId}`
            );

            if (existingChatResponse.ok) {
                const responseText = await existingChatResponse.text();
                console.log('Existing chat response text:', responseText);
                
                if (responseText) {
                    try {
                        const existingChat = JSON.parse(responseText);
                        console.log('Existing chat response:', existingChat);
                        
                        if (existingChat && existingChat.chat_id) {
                            this.currentChatId = existingChat.chat_id;
                            console.log('Found existing chat with ID:', this.currentChatId);
                            return;
                        }
                    } catch (jsonError) {
                        console.error('Failed to parse existing chat JSON:', jsonError);
                    }
                }
            } else if (existingChatResponse.status === 404) {
                console.log('No existing chat found (404), will create new one');
            } else {
                console.error('Error checking for existing chat:', existingChatResponse.status);
            }

            console.log('No existing chat found, creating new chat...');

            // Create new chat with first message
            const chatData = {
                clientUserId: parseInt(this.currentUserId),
                ownerUserId: parseInt(this.currentOwnerId),
                postId: parseInt(this.currentPostId),
                firstMessage: "Hi, I'm interested in your ad."
            };

            console.log('Creating chat with data:', chatData);

            const response = await fetch('http://localhost:8080/api/chat/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(chatData)
            });

            console.log('Create chat response status:', response.status);

            if (response.ok) {
                const responseText = await response.text();
                console.log('Create chat response text:', responseText);
                
                if (responseText) {
                    try {
                        const chat = JSON.parse(responseText);
                        this.currentChatId = chat.chat_id;
                        console.log('Chat created with ID:', this.currentChatId);
                    } catch (jsonError) {
                        console.error('Failed to parse JSON response:', jsonError);
                        throw new Error('Invalid response from server');
                    }
                } else {
                    throw new Error('Empty response from server');
                }
            } else {
                const errorText = await response.text();
                console.error('Failed to create chat. Status:', response.status, 'Response:', errorText);
                throw new Error(`Failed to create chat: ${response.status} - ${errorText}`);
            }

        } catch (error) {
            console.error('Error creating/getting chat:', error);
            throw error;
        }
    }

    async loadChatHistory() {
        try {
            if (!this.currentChatId) return;

            console.log('Loading chat history for chat:', this.currentChatId);
            const response = await fetch(`http://localhost:8080/api/chat/${this.currentChatId}/messages`);
            if (response.ok) {
                const messages = await response.json();
                console.log('Loaded chat history:', messages);
                
                // Clear existing messages
                $('#messages').empty();
                $('#chatMessages').empty();
                $('#noChatHistory').hide();
                
                if (messages.length === 0) {
                    $('#noChatHistory').show();
                    return;
                }
                
                // Display each message
                messages.forEach(msg => {
                    const isOwnMessage = msg.senderUserId == this.currentUserId;
                    this.displayMessage({
                        from: msg.senderUserId.toString(),
                        content: msg.content,
                        timestamp: new Date(msg.sent_at)
                    }, isOwnMessage);
                });
                
                this.scrollToBottom();
            } else {
                console.error('Failed to load chat history, status:', response.status);
                $('#noChatHistory').show();
            }
        } catch (error) {
            console.error('Error loading chat history:', error);
            $('#noChatHistory').show();
        }
    }

    connect() {
        return new Promise((resolve, reject) => {
            try {
                console.log("hi")
                const socket = new SockJS('http://localhost:8080/ws');
                this.stompClient = Stomp.over(socket);
                
                this.stompClient.connect({}, (frame) => {
                    console.log('Connected to chat:', frame);
                    this.isConnected = true;
                    
                    // Subscribe to the chat room using chat ID
                    this.stompClient.subscribe(`/topic/chat/${this.currentChatId}`, (messageOutput) => {
                        const message = JSON.parse(messageOutput.body);
                        // Only display if it's not our own message (to avoid duplicates)
                        if (message.from !== this.currentUserId.toString()) {
                            this.displayMessage(message, false);
                        }
                    });
                    
                    resolve();
                }, (error) => {
                    console.error('WebSocket connection error:', error);
                    this.isConnected = false;
                    reject(error);
                });
                
            } catch (error) {
                console.error('Error connecting to WebSocket:', error);
                reject(error);
            }
        });
    }

    disconnect() {
        if (this.stompClient && this.isConnected) {
            this.stompClient.disconnect(() => {
                console.log('Disconnected from chat');
            });
            this.isConnected = false;
        }
    }

    async sendMessage() {
        const messageInput = $('#messageInput');
        const content = messageInput.val().trim();
        
        if (!content) {
            return;
        }

        if (!this.isConnected || !this.stompClient || !this.currentChatId) {
            alert('Not connected to chat. Please try again.');
            return;
        }

        try {
            // Display message immediately (optimistic UI)
        const chatMessage = {
                from: this.currentUserId.toString(),
                content: content,
                timestamp: new Date()
            };
            this.displayMessage(chatMessage, true);

            // Send via WebSocket for real-time delivery
            this.stompClient.send(`/app/message/${this.currentChatId}`, {}, JSON.stringify(chatMessage));

            // Clear input
            messageInput.val('');

        } catch (error) {
            console.error('Error sending message:', error);
            alert('Failed to send message. Please try again.');
        }
    }

    displayMessage(message, isOwnMessage) {
        const messagesContainer = $('#chatMessages');
        
        // Hide "no chat history" message when first message appears
        $('#noChatHistory').hide();
        
        const timestamp = new Date(message.timestamp).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
        
        const messageHtml = `
            <div class="mb-3 d-flex ${isOwnMessage ? 'justify-content-end' : 'justify-content-start'}">
                <div class="message-bubble">
                    <div class="p-3 rounded-4 ${isOwnMessage ? 'bg-primary text-white' : 'bg-white border'}">
                        <p class="mb-1 message-text">${this.escapeHtml(message.content)}</p>
                        <small class="opacity-75 message-time">${timestamp}</small>
                    </div>
                </div>
            </div>
        `;
        
        messagesContainer.append(messageHtml);
        this.scrollToBottom();
    }

    scrollToBottom() {
        const container = $('#chatMessages');
        if (container.length) {
            container.scrollTop(container[0].scrollHeight);
        }
        
        // Also scroll admin chat container if it exists
        const adminContainer = $('#activeChatMessages');
        if (adminContainer.length && adminContainer.is(':visible')) {
            adminContainer.scrollTop(adminContainer[0].scrollHeight);
        }
    }

    async getUserIdFromToken(token) {
        try {
            const response = await fetch('http://localhost:8080/api/users/getUserByToken', {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + token
                }
            });
            
            if (response.ok) {
                const userData = await response.json();
                this.currentUserId = userData.id;
                // Store it in cookie for future use
                setCookie("userId", userData.id, 1);
                console.log("User ID retrieved from token:", userData.id);
            } else {
                throw new Error('Failed to get user data from token');
            }
        } catch (error) {
            console.error('Error getting user ID from token:', error);
        }
    }

    async checkIfOwner() {
        try {
            this.currentUserId = getCookie("userId");
            this.currentPostId = window.currentAdData?.post_id;
            this.currentOwnerId = window.currentSellerData?.id;

            if (this.currentUserId && this.currentOwnerId && this.currentUserId == this.currentOwnerId) {
                this.isOwner = true;
                $('#viewChatsBtn').show();
                $('#messageBtn').hide();
                console.log("User is the ad owner");
            } else {
                this.isOwner = false;
                $('#viewChatsBtn').hide();
                $('#messageBtn').show();
            }
        } catch (error) {
            console.error('Error checking owner status:', error);
        }
    }

    async openOwnerChatList() {
        try {
            if (!this.currentUserId || !this.currentPostId) {
                alert('Unable to load chats. Please try again.');
                return;
            }

            console.log("Loading chats for post ID:", this.currentPostId);

            // Load all chats for this post
            await this.loadOwnerChats();

            // Show the chat list modal
            $('#chatListModal').modal('show');

            // Connect to WebSocket for real-time updates
            await this.connectToAllChats();

        } catch (error) {
            console.error('Error opening owner chat list:', error);
            alert('Failed to load chats. Please try again.');
        }
    }

    async loadOwnerChats() {
        try {
            // Get chats by post ID
            const response = await fetch(`http://localhost:8080/api/chat/post/${this.currentPostId}/chats`);
            
            if (response.ok) {
                const responseText = await response.text();
                if (responseText) {
                    this.ownerChats = JSON.parse(responseText);
                    this.displayChatList();
                } else {
                    this.ownerChats = [];
                    this.displayChatList();
                }
            } else {
                console.error('Failed to load chats:', response.status);
                this.ownerChats = [];
                this.displayChatList();
            }
        } catch (error) {
            console.error('Error loading owner chats:', error);
            this.ownerChats = [];
            this.displayChatList();
        }
    }

    displayChatList() {
        const chatList = $('#chatList');
        const noChatMessage = $('#noChatMessage');
        const chatCount = $('#chatCount');

        if (this.ownerChats.length === 0) {
            noChatMessage.show();
            chatCount.text('0');
            return;
        }

        noChatMessage.hide();
        chatCount.text(this.ownerChats.length);

        let chatListHtml = '';
        this.ownerChats.forEach(chat => {
            const lastMessageTime = chat.last_message_at ? 
                new Date(chat.last_message_at).toLocaleString() : 'No messages';
            
            chatListHtml += `
                <div class="list-group-item list-group-item-action chat-list-item" data-chat-id="${chat.chat_id}">
                    <div class="d-flex align-items-center">
                        <img class="rounded-circle me-3" 
                             src="https://ui-avatars.com/api/?name=${encodeURIComponent(chat.clientUserName)}&background=059669&color=fff&size=40&rounded=true"
                             style="width: 40px; height: 40px; object-fit: cover;">
                        <div class="flex-grow-1">
                            <h6 class="mb-1">${this.escapeHtml(chat.clientUserName)}</h6>
                            <p class="mb-1 small text-muted">${chat.lastMessage ? this.escapeHtml(chat.lastMessage) : 'No messages yet'}</p>
                            <small class="text-muted">${lastMessageTime}</small>
                        </div>
                        ${chat.unreadCount > 0 ? `<span class="badge bg-primary rounded-pill">${chat.unreadCount}</span>` : ''}
                    </div>
                </div>
            `;
        });

        chatList.html(chatListHtml);
    }

    async selectOwnerChat(chatId) {
        try {
            console.log('Selecting owner chat:', chatId);
            this.activeOwnerChatId = chatId;
            const selectedChat = this.ownerChats.find(chat => chat.chat_id == chatId);

            if (!selectedChat) {
                console.error('Selected chat not found:', chatId);
                return;
            }

            console.log('Found selected chat:', selectedChat);

            // Update active chat header
            $('#activeChatUserName').text(selectedChat.clientUserName);
            $('#activeChatTime').text('Last seen: ' + new Date(selectedChat.last_message_at || Date.now()).toLocaleString());
            $('#activeChatUserImage').attr('src', 
                `https://ui-avatars.com/api/?name=${encodeURIComponent(selectedChat.clientUserName)}&background=059669&color=fff&size=50&rounded=true`);

            // Show chat interface elements
            $('#selectChatMessage').hide();
            $('#activeChatHeader').show();
            $('#activeChatInput').show();

            // Load messages for this chat
            await this.loadOwnerChatMessages(chatId);

            // Update active chat highlighting
            $('.chat-list-item').removeClass('active');
            $(`.chat-list-item[data-chat-id="${chatId}"]`).addClass('active');

            console.log('Chat selection completed');

        } catch (error) {
            console.error('Error selecting chat:', error);
            alert('Failed to select chat. Please try again.');
        }
    }

    async loadOwnerChatMessages(chatId) {
        try {
            console.log('Loading messages for chat:', chatId);
            const response = await fetch(`http://localhost:8080/api/chat/${chatId}/messages`);
            if (response.ok) {
                const messages = await response.json();
                console.log('Loaded messages:', messages);
                this.displayOwnerChatMessages(messages);
            } else {
                console.error('Failed to load messages, status:', response.status);
                this.displayOwnerChatMessages([]);
            }
        } catch (error) {
            console.error('Error loading chat messages:', error);
            this.displayOwnerChatMessages([]);
        }
    }

    displayOwnerChatMessages(messages) {
        const messagesContainer = $('#activeChatMessages');
        messagesContainer.empty();

        if (messages.length === 0) {
            messagesContainer.html(`
                <div class="text-center text-muted mt-5">
                    <i class="bi bi-chat-dots fs-1 mb-3 d-block opacity-50"></i>
                    <p>No messages yet</p>
                    <small>Start the conversation!</small>
                </div>
            `);
            return;
        }

        messages.forEach(msg => {
            const isOwnMessage = msg.senderUserId == this.currentUserId;
            const timestamp = new Date(msg.sent_at).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
            
            const messageHtml = `
                <div class="mb-3 d-flex ${isOwnMessage ? 'justify-content-end' : 'justify-content-start'}">
                    <div class="message-bubble">
                        <div class="p-3 rounded-4 ${isOwnMessage ? 'bg-primary text-white' : 'bg-white border'}">
                            <p class="mb-1 message-text">${this.escapeHtml(msg.content)}</p>
                            <small class="opacity-75 message-time">${timestamp}</small>
                        </div>
                    </div>
                </div>
            `;
            
            messagesContainer.append(messageHtml);
        });

        // Scroll to bottom
        messagesContainer.scrollTop(messagesContainer[0].scrollHeight);
    }

    async sendOwnerMessage() {
        const messageInput = $('#ownerMessageInput');
        const content = messageInput.val().trim();
        
        if (!content || !this.activeOwnerChatId) {
            return;
        }

        try {
            // Send message via API
            const messageData = {
                senderUserId: parseInt(this.currentUserId),
                chatId: parseInt(this.activeOwnerChatId),
                content: content,
                sent_at: new Date().toISOString()
            };

            const response = await fetch('http://localhost:8080/api/chat/message', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(messageData)
            });

            if (response.ok) {
                // Clear input
                messageInput.val('');
                
                // Reload messages to show the new message
                await this.loadOwnerChatMessages(this.activeOwnerChatId);
            } else {
                console.error('Failed to send message');
                alert('Failed to send message. Please try again.');
            }

        } catch (error) {
            console.error('Error sending owner message:', error);
            alert('Failed to send message. Please try again.');
        }
    }

    async connectToAllChats() {
        try {
            const socket = new SockJS('http://localhost:8080/ws');
            this.stompClient = Stomp.over(socket);
            
            this.stompClient.connect({}, (frame) => {
                console.log('Connected to owner chats:', frame);
                this.isConnected = true;
                
                // Subscribe to all chats for this post
                this.ownerChats.forEach(chat => {
                    this.stompClient.subscribe(`/topic/chat/${chat.chat_id}`, (messageOutput) => {
                        const message = JSON.parse(messageOutput.body);
                        this.handleOwnerChatMessage(message, chat.chat_id);
                    });
                });
                
            }, (error) => {
                console.error('WebSocket connection error:', error);
                this.isConnected = false;
            });
            
        } catch (error) {
            console.error('Error connecting to WebSocket:', error);
        }
    }

    handleOwnerChatMessage(message, chatId) {
        // If this message is for the currently active chat, display it
        if (chatId == this.activeOwnerChatId) {
            // Reload messages for real-time update
            this.loadOwnerChatMessages(chatId);
        }
        
        // Update chat list to show new message
        this.loadOwnerChats();
    }

    escapeHtml(text) {
        const map = {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#039;'
        };
        return text.replace(/[&<>"']/g, (m) => map[m]);
    }
}

// Initialize chat manager when document is ready
$(document).ready(() => {
    window.chatManager = new ChatManager();
});

// Legacy functions for backward compatibility
let sender, receiver, room, stompClient;

function connect() {
    if (window.chatManager) {
        return window.chatManager.connect();
    }
}

function startChat() {
    if (window.chatManager) {
        window.chatManager.openChat();
    }
}

function sendMessage() {
    if (window.chatManager) {
        window.chatManager.sendMessage();
    }
}

function showMessage(message) {
    if (window.chatManager) {
        window.chatManager.displayMessage(message, false);
    }
}

