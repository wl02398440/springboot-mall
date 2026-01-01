
// 導覽列共有物件
const authMixin = {
    data() {
        return {
            showLogin: false,
            showRegister: false,
            loginForm: { email: 'www1@gmail.com', password: '111' },
            isLoggedIn: false,
            currentUser: null,
            userId: null,
        };
    },
    methods: {
        //載入共有data
        checkLoginStatus() {
            const storedUser = sessionStorage.getItem('shop_user');
            if (storedUser) {
                const userData = JSON.parse(storedUser);
                this.currentUser = userData.userName;
                this.isLoggedIn = true;
                this.userId = userData.userId;
            }
        },
        //登出帳號
        logout() {
            sessionStorage.clear();
            this.isLoggedIn = false;
            this.currentUser = null;
            this.userId = null;
            Swal.fire({
                title: '下次再來~',
                showConfirmButton: false, // 不顯示確定按鈕
                timer: 1000
            }).then(() => {
                location.href = '../html/home.html';
            });
        },
        //購物車
        goShopcart(){
            location.href = '../html/shopcart.html'
        }

    }
};