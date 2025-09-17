/**
 * Profile Image Manager - Reusable component for loading and displaying user profile images
 * across all pages of the application
 */
class ProfileImageManager {
    constructor() {
        this.defaultAvatarBase = 'https://ui-avatars.com/api/';
        this.currentUser = null;
        this.profileImageUrl = null;
    }

    /**
     * Initialize profile image loading for the current authenticated user
     */
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

    /**
     * Fetch user data from the API
     */
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

    /**
     * Determine the appropriate profile image URL
     */
    determineProfileImageUrl(userData) {
        let profileUrl = userData.profileImageUrl;

        // Use fallback avatar if no profile image URL or if it's empty
        if (!profileUrl || profileUrl.trim() === '') {
            profileUrl = this.generateFallbackAvatar(userData.name, 120);
        } else {
            profileUrl = profileUrl.trim();
        }

        return profileUrl;
    }

    /**
     * Generate fallback avatar URL
     */
    generateFallbackAvatar(name, size = 40) {
        const encodedName = encodeURIComponent(name || 'User');
        return `${this.defaultAvatarBase}?name=${encodedName}&background=059669&color=fff&size=${size}&rounded=true`;
    }

    /**
     * Update all profile images on the current page
     */
    updateAllProfileImages() {
        if (!this.profileImageUrl || !this.currentUser) return;

        // Common profile image selectors
        const profileSelectors = [
            '#profileImg',           // Large profile image
            '#navProfileImg',        // Navigation profile image
            '.profile-avatar',       // Any element with profile-avatar class
            '.profile-avatar-large', // Large profile avatars
            '#photoPreview'          // Photo preview in modals
        ];

        profileSelectors.forEach(selector => {
            const elements = $(selector);
            if (elements.length > 0) {
                elements.each((index, element) => {
                    this.setProfileImage($(element));
                });
            }
        });

        // Update user name and email where available
        this.updateUserInfo();
    }

    /**
     * Set profile image for a specific element
     */
    setProfileImage($element, customSize = null) {
        if (!$element.length || !this.profileImageUrl) return;

        let imageUrl = this.profileImageUrl;

        // Handle size-specific requirements
        if (customSize && imageUrl.includes("ui-avatars")) {
            imageUrl = imageUrl.replace(/size=\d+/, `size=${customSize}`);
        } else if ($element.hasClass('profile-avatar-small') || $element.attr('id') === 'navProfileImg') {
            // Small avatars (navigation, etc.)
            if (imageUrl.includes("ui-avatars")) {
                imageUrl = imageUrl.replace(/size=\d+/, 'size=40');
            }
        } else if ($element.hasClass('profile-avatar-large') || $element.attr('id') === 'profileImg') {
            // Large avatars (profile page, etc.)
            if (imageUrl.includes("ui-avatars")) {
                imageUrl = imageUrl.replace(/size=\d+/, 'size=120');
            }
        }

        // Set the image source
        $element.attr('src', imageUrl);

        // Add error handling
        $element.off('error.profileImage').on('error.profileImage', () => {
            const fallbackUrl = this.generateFallbackAvatar(this.currentUser?.name, customSize || 40);
            $element.attr('src', fallbackUrl);
        });
    }

    /**
     * Update user information elements
     */
    updateUserInfo() {
        if (!this.currentUser) return;

        // Update user name
        $('#userName, .user-name').text(this.currentUser.name);

        // Update user email
        $('#userEmail, .user-email').text(this.currentUser.email);

        // Update join date if available
        if (this.currentUser.joinDate) {
            const joinDate = this.formatJoinDate(this.currentUser.joinDate);
            $('#joinDate, .join-date').text(joinDate);
        }
    }

    /**
     * Show profile-related elements
     */
    showProfileElements() {
        $('#profileDropdown').show();
        $('#signInBtn').hide();

        // Enable post ad functionality for authenticated users
        $('#postAdBtn').off('click.auth').on('click.auth', () => {
            window.location.href = this.getRelativePath('postad.html');
        });
    }

    /**
     * Hide profile-related elements
     */
    hideProfileElements() {
        $('#profileDropdown').hide();
        $('#signInBtn').show();

        // Redirect unauthenticated users to signup when trying to post ad
        $('#postAdBtn').off('click.auth').on('click.auth', () => {
            window.location.href = this.getRelativePath('signup.html');
        });

        // Handle sign in button
        $('#signInBtn').off('click.auth').on('click.auth', () => {
            window.location.href = this.getRelativePath('signin.html');
        });
    }

    /**
     * Update profile image after upload
     */
    async updateProfileImage(newImageUrl) {
        if (!newImageUrl) return;

        this.profileImageUrl = newImageUrl;
        this.updateAllProfileImages();

        // Update the user object
        if (this.currentUser) {
            this.currentUser.profileImageUrl = newImageUrl;
        }
    }

    /**
     * Get relative path based on current page location
     */
    getRelativePath(filename) {
        const currentPath = window.location.pathname;
        if (currentPath.includes('/pages/')) {
            return filename; // Already in pages directory
        }
        return `pages/${filename}`; // Need to navigate to pages directory
    }

    /**
     * Format join date
     */
    formatJoinDate(isoDate) {
        const date = new Date(isoDate);
        const options = { year: 'numeric', month: 'long' };
        return `Joined ${date.toLocaleDateString('en-US', options)}`;
    }

    /**
     * Cookie helper methods
     */
    getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
        return null;
    }

    deleteCookie(name) {
        document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/`;
    }

    /**
     * Logout functionality
     */
    logout() {
        if (confirm("Are you sure you want to logout?")) {
            this.deleteCookie("token");
            this.deleteCookie("user");
            window.location.href = this.getRelativePath('../index.html');
        }
    }

    /**
     * Initialize logout handlers
     */
    initLogoutHandlers() {
        $(document).off('click', '[data-logout]').on('click', '[data-logout]', (e) => {
            e.preventDefault();
            this.logout();
        });

        // Handle onclick logout calls
        window.logout = () => this.logout();
    }
}

// Create global instance
const profileImageManager = new ProfileImageManager();

// Auto-initialize when DOM is ready
$(document).ready(() => {
    profileImageManager.init();
    profileImageManager.initLogoutHandlers();
});

// Expose for manual initialization if needed
window.ProfileImageManager = ProfileImageManager;
window.profileImageManager = profileImageManager;