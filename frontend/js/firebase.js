const firebaseConfig = {
    apiKey: "AIzaSyCP8U0SU_P6mQ7YuYdlfFI8AJS_cO8lTsA",
    authDomain: "g-exxplore.firebaseapp.com",
    databaseURL: "https://g-exxplore-default-rtdb.asia-southeast1.firebasedatabase.app",
    projectId: "g-exxplore",
    storageBucket: "g-exxplore.appspot.com",
    messagingSenderId: "171368449973",
    appId: "1:171368449973:web:93e92f9f028dad13503bfc",
    measurementId: "G-SNSBKHN49T"
};

firebase.initializeApp(firebaseConfig);
const storage = firebase.storage();

let selectedFiles = [];
let uploadedImageUrls = [];

function handleFileSelection(files) {
    selectedFiles = Array.from(files);
    displayImagePreviews();
}

function displayImagePreviews() {
    const previewContainer = document.getElementById('imagePreviewContainer');
    previewContainer.innerHTML = '';

    selectedFiles.forEach((file, index) => {
        const reader = new FileReader();
        reader.onload = function (e) {
            const previewDiv = document.createElement('div');
            previewDiv.className = 'image-preview';
            previewDiv.innerHTML = `
                <img src="${e.target.result}" alt="Preview ${index + 1}">
                <button type="button" class="remove-image" onclick="removeImage(${index})">×</button>
            `;
            previewContainer.appendChild(previewDiv);
        };
        reader.readAsDataURL(file);
    });
}

function removeImage(index) {
    selectedFiles.splice(index, 1);
    displayImagePreviews();
}

async function uploadImagesToFirebase(selected) {
    console.log(selected)
    if (selected.length === 0) {
        console.log(selectedFiles);
        return [];
    }

    const uploadPromises = [];

    for (let i = 0; i < selected.length; i++) {
        console.log("//////////////////////////////////////////////////////////")
        const file = selected[i];
        const fileName = `animals/${Date.now()}_${i}_${file.name}`;
        const storageRef = storage.ref(fileName);

        const uploadTask = storageRef.put(file);

        uploadPromises.push(
            new Promise((resolve, reject) => {
                uploadTask.on('state_changed',
                    (snapshot) => {
                        const progress = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
                        const overallProgress = ((i / selected.length) * 100) + (progress / selected.length);

                        // progressFill.style.width = overallProgress + '%';
                        // progressText.textContent = `Uploading images... ${Math.round(overallProgress)}%`;
                        console.log(overallProgress)
                    },
                    (error) => {
                        console.error('Upload failed:', error);
                        reject(error);
                    },
                    () => {
                        uploadTask.snapshot.ref.getDownloadURL().then((downloadURL) => {
                            resolve(downloadURL);
                        });
                    }
                );
            })
        );
    }

    try {
        const urls = await Promise.all(uploadPromises);
        uploadedImageUrls = urls;
        return urls;
    } catch (error) {
        throw error;
    }
}

function validateImages(files) {
    const maxSize = 5 * 1024 * 1024; // 5MB
    const allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];

    for (let file of files) {
        if (file.size > maxSize) {
            alert(`File "${file.name}" is too large. Maximum size is 5MB.`);
            return false;
        }

        if (!allowedTypes.includes(file.type)) {
            alert(`File "${file.name}" is not a valid image type. Allowed types: JPEG, PNG, GIF, WebP.`);
            return false;
        }
    }

    if (files.length > 5) {
        alert('Maximum 5 images allowed.');
        return false;
    }

    return true;
}