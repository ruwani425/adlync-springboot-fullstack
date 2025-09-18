let sender = getCookie("msgId");
let receiver = getCookie("receiverId");
let room = ''

function connect() {
    const socket = new SockJS('http://localhost:8080/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/chat/' + room, function (messageOutput) {
            showMessage(JSON.parse(messageOutput.body));
        });
    });
}

function startChat() {
    if (!sender || !receiver) {
        alert('Please enter both names.');
        return;
    }

    // Compute room name by sorting names alphabetically
    const names = [sender, receiver].sort();
    room = names.join('_');

    connect();
}


function sendMessage() {
    const content = document.getElementById('messageInput').value.trim();
    if (content && stompClient) {
        const chatMessage = {
            from: sender,
            content: content
        };
        stompClient.send('/app/message/' + room, {}, JSON.stringify(chatMessage));
        document.getElementById('messageInput').value = '';
    }
}

function showMessage(message) {
    const messages = document.getElementById('messages');
    const div = document.createElement('div');
    div.textContent = '[' + new Date(message.timestamp).toLocaleTimeString() + '] ' + message.from + ': ' + message.content;
    messages.appendChild(div);
    messages.scrollTop = messages.scrollHeight;
}

$('#messageBtn').on('click', () => {
    const sellerId = window.currentSellerData?.id || 'seller';
    const adId = window.currentAdData?.post_id || 'ad';
    const roomId = `ad-${adId}-seller-${sellerId}`;
    $('#chatModal').modal('show');
    startChat();
});

$('#sendChatBtn').on('click', () => {
    const msg = $('#chatInput').val().trim();
    if (msg === '') return;
    const sellerId = window.currentSellerData?.id || 'seller';
    const adId = window.currentAdData?.post_id || 'ad';
    const roomId = `ad-${adId}-seller-${sellerId}`;
    sendMessage(roomId, msg);
    $('#chatInput').val('');
});

$('#chatInput').on('keypress', function (e) {
    if (e.which === 13) $('#sendChatBtn').click();
});

