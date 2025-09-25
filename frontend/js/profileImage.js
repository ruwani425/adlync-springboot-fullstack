class ProfileImageManager {
    constructor() {
        this.defaultAvatarBase = 'https://ui-avatars.com/api/';
        this.currentUser = null;
        this.profileImageUrl = null;
    }


    async init() {
        const token = this.getCookie("token");
        if (!token) {
            this.hideProfileElements();
            return false;
        }

        try {
            const userData = await this.fetchUserData(token);
            this.currentUser = userData;
            this.profileImageUrl = this.determineProfileImageUrl(userData);

            this.updateAllProfileImages();
            this.showProfileElements();
            return true;
        } catch (error) {
            console.error('Failed to load user profile:', error);
            this.hideProfileElements();
            return false;
        }
    }

    fetchUserData(token) {
        return new Promise((resolve, reject) => {
            $.ajax({
                url: "http://localhost:8080/api/users/getUserByToken",
                method: "GET",
                headers: {"Authorization": "Bearer " + token},
                success: resolve,
                error: (xhr, status, error) => reject(new Error(`Failed to fetch user data: ${error}`))
            });
        });
    }

    determineProfileImageUrl(userData) {
        let profileUrl = userData.profileImageUrl;

        if (!profileUrl || profileUrl.trim() === '') {
            profileUrl = this.generateFallbackAvatar(userData.name, 120);
        } else {
            profileUrl = profileUrl.trim();
        }

        return profileUrl;
    }

    generateFallbackAvatar(name, size = 40) {
        const encodedName = encodeURIComponent(name || 'User');
        return `${this.defaultAvatarBase}?name=${encodedName}&background=059669&color=fff&size=${size}&rounded=true`;
    }


    updateAllProfileImages() {
        if (!this.profileImageUrl || !this.currentUser) return;

        const profileSelectors = [
            '#profileImg',
            '#navProfileImg',
            '.profile-avatar',
            '.profile-avatar-large',
            '#photoPreview'
        ];

        profileSelectors.forEach(selector => {
            const elements = $(selector);
            if (elements.length > 0) {
                elements.each((index, element) => {
                    this.setProfileImage($(element));
                });
            }
        });

        this.updateUserInfo();
    }

    setProfileImage($element, customSize = null) {
        if (!$element.length || !this.profileImageUrl) return;

        let imageUrl = this.profileImageUrl;

        if (customSize && imageUrl.includes("ui-avatars")) {
            imageUrl = imageUrl.replace(/size=\d+/, `size=${customSize}`);
        } else if ($element.hasClass('profile-avatar-small') || $element.attr('id') === 'navProfileImg') {
            if (imageUrl.includes("ui-avatars")) {
                imageUrl = imageUrl.replace(/size=\d+/, 'size=40');
            }
        } else if ($element.hasClass('profile-avatar-large') || $element.attr('id') === 'profileImg') {
            if (imageUrl.includes("ui-avatars")) {
                imageUrl = imageUrl.replace(/size=\d+/, 'size=120');
            }
        }

        $element.attr('src', imageUrl);

        $element.off('error.profileImage').on('error.profileImage', () => {
            const fallbackUrl = this.generateFallbackAvatar(this.currentUser?.name, customSize || 40);
            $element.attr('src', fallbackUrl);
        });
    }


    updateUserInfo() {
        if (!this.currentUser) return;

        $('#userName, .user-name').text(this.currentUser.name);

        $('#userEmail, .user-email').text(this.currentUser.email);

        if (this.currentUser.joinDate) {
            const joinDate = this.formatJoinDate(this.currentUser.joinDate);
            $('#joinDate, .join-date').text(joinDate);
        }
    }

    showProfileElements() {
        $('#profileDropdown').show();
        $('#signInBtn').hide();

        $('#postAdBtn').off('click.auth').on('click.auth', () => {
            window.location.href = this.getRelativePath('postad.html');
        });
    }

    hideProfileElements() {
        $('#profileDropdown').hide();
        $('#signInBtn').show();

        $('#postAdBtn').off('click.auth').on('click.auth', () => {
            window.location.href = this.getRelativePath('signup.html');
        });

        $('#signInBtn').off('click.auth').on('click.auth', () => {
            window.location.href = this.getRelativePath('signin.html');
        });
    }

    async updateProfileImage(newImageUrl) {
        if (!newImageUrl) return;

        this.profileImageUrl = newImageUrl;
        this.updateAllProfileImages();

        if (this.currentUser) {
            this.currentUser.profileImageUrl = newImageUrl;
        }
    }

    getRelativePath(filename) {
        const currentPath = window.location.pathname;
        if (currentPath.includes('/pages/')) {
            return filename;
        }
        return `pages/${filename}`;
    }

    formatJoinDate(isoDate) {
        const date = new Date(isoDate);
        const options = {year: 'numeric', month: 'long'};
        return `Joined ${date.toLocaleDateString('en-US', options)}`;
    }

    getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
        return null;
    }

    deleteCookie(name) {
        document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/`;
    }

    logout() {
        if (confirm("Are you sure you want to logout?")) {
            this.deleteCookie("token");
            this.deleteCookie("user");
            window.location.href = this.getRelativePath('../index.html');
        }
    }

    initLogoutHandlers() {
        $(document).off('click', '[data-logout]').on('click', '[data-logout]', (e) => {
            e.preventDefault();
            this.logout();
        });

        window.logout = () => this.logout();
    }
}

const profileImageManager = new ProfileImageManager();

$(document).ready(() => {
    profileImageManager.init();
    profileImageManager.initLogoutHandlers();
});

window.ProfileImageManager = ProfileImageManager;
window.profileImageManager = profileImageManager;