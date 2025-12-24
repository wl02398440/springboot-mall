
// 導覽列共有物件
const authMixin = {
    data() {
        return {
            showLogin: false,
            showRegister: false,
            loginForm: { email: 'www1@gmail.com', password: '111' },
            isLoggedIn: false,
            currentUser: null
        };
    },
    methods: {
        //載入共有data
        checkLoginStatus() {
            const storedUser = sessionStorage.getItem('shop_user');
            if (storedUser) {
                this.currentUser = JSON.parse(storedUser).userName;
                this.isLoggedIn = true;
            }
        },
        //登出帳號
        logout() {
            sessionStorage.clear();
            this.isLoggedIn = false;
            this.currentUser = null;
            alert("已登出");
            location.href = '../html/home.html';
        },
    }
};