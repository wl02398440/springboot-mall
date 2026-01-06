
// 註冊帳號
function register() {
    // 前端防呆
    if (!this.loginForm.email || !this.loginForm.password || !this.confirmPassword) {
        Swal.fire({
            icon: 'warning', // 圖示
            title: '請輸入 Email 和密碼！',
            showConfirmButton: false, // 不顯示確定按鈕
            timer: 1000
        });
        // alert("請輸入 Email 和密碼");
        return;
    }
    if (this.loginForm.password !== this.confirmPassword) {
        Swal.fire({
            icon: 'warning', // 圖示
            title: '密碼不一致！',
            showConfirmButton: false, // 不顯示確定按鈕
            timer: 1000
        });
        return;
    }
    // 發送 POST 請求給後端
    fetch('http://localhost:8080/users/register', {  // API 路徑
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(this.loginForm)
    })
        .then(response => {
            // 檢查後端回應狀態
            if (!response.ok) {
                return response.json().then(errorBody => {
                    let msg = errorBody.message;
                    if (errorBody.errors && errorBody.errors.length > 0) {
                        msg = errorBody.errors[0].defaultMessage;
                    }
                    throw new Error(msg);
                });
            }
            return response.json();
        })
        .then(userData => {
            // 註冊成功後的處理
            console.log("註冊成功，使用者資料:", userData);
            Swal.fire({
                icon: 'success', // 圖示
                title: '註冊成功，請使用新帳號登入！',
                showConfirmButton: false, // 不顯示確定按鈕
                timer: 1000
            });

            this.showRegister = false; // 關閉註冊視窗
            // 清空表單
            // this.loginForm.email = '';
            // this.loginForm.password = '';
        })
        .catch(error => {
            // 錯誤處理
            console.error("發生錯誤:", error);
            Swal.fire({
                icon: 'warning', // 圖示
                title: error.message,
                showConfirmButton: true, // 不顯示確定按鈕
                timer: 1000
            });
        });
}

// 登入帳號
function login(){
    // 前端防呆
    if (!this.loginForm.email || !this.loginForm.password
        || !this.loginForm.password ) {
        Swal.fire({
            icon: 'warning', // 圖示
            title: '請輸入完整資訊 (含驗證碼)',
            showConfirmButton: false, // 不顯示確定按鈕
            timer: 1000
        });
        return;
    }
    // 發送 POST 請求給後端
    fetch('http://localhost:8080/users/login', {  // API 路徑
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(this.loginForm)
    })
        .then(response => {
            // 檢查後端回應狀態
            if (!response.ok) {
                return response.json().then(errorBody => {
                    let msg = errorBody.message;
                    if (errorBody.errors && errorBody.errors.length > 0) {
                        msg = errorBody.errors[0].defaultMessage;
                    }
                    throw new Error(msg);
                    });
            }
            return response.json();
        })
        .then(userData => {
            // 登入成功後的處理
            let shortName = userData.email.split('@')[0];
            // 確認是否管理者
            if (shortName === `www1`){
                this.isManager = true;
            }
            userData.userName = shortName;
            userData.isManager = this.isManager;
            this.currentUser = userData.userName;
            this.isLoggedIn = true;
            this.userId = userData.userId;
            sessionStorage.setItem('shop_user', JSON.stringify(userData));
            console.log("登入成功，使用者資料:", userData);
            Swal.fire({
                icon: 'success', // 圖示
                title: '歡迎回來，' + userData.userName,
                showConfirmButton: false, // 不顯示確定按鈕
                timer: 1000
            });
            this.showLogin = false; // 關閉登入視窗
            // 清空表單
            this.loginForm.email = '';
            this.loginForm.password = '';
        })
        .catch(error => {
            // 錯誤處理
            console.error("發生錯誤:", error);
            Swal.fire({
                icon: 'warning', // 圖示
                title: error.message,
                showConfirmButton: false, // 不顯示確定按鈕
                timer: 10000,

            });
        });
}

// 開始選購
function shopNow() {
    if (this.isLoggedIn) {
        location.href = '../html/productMall.html';
    } else {
        Swal.fire({
            icon: 'warning', // 圖示
            title: '請先登入會員才能開始選購喔！',
            showConfirmButton: false, // 不顯示確定按鈕
            timer: 1000
        }).then(() => {
            this.showLogin = true;// 打開登入視窗
        })
    }
}


// 關閉視窗
function switchModal() {
    this.showLogin = false;
    this.showRegister = true;
}
// 清空欄位
function claen(){
    this.loginForm.email ='';
    this.loginForm.password ='';
}
// 刷新驗證碼
function refreshCaptcha(){
    // 加時間戳記強制瀏覽器重新抓取
    this.captchaUrl = 'http://localhost:8080/captcha?t=' + Date.now();
    this.loginForm.captcha = '';
}

new Vue({
    el: '#app',
    mixins: [authMixin],
    data: {
        showLogin: false,
        showRegister: false,
        loginForm: {
            email: 'www1@gmail.com',
            password: '111',
            captcha: ''
        },
        captchaUrl: 'http://localhost:8080/captcha'
    },
    mounted() {
        this.checkLoginStatus(); // 呼叫共用的

    },
    computed: {
        isAdmin() {
            return this.isManager;
        }
    },
    methods: {
        login,
        register,
        shopNow,
        switchModal,
        claen,
        refreshCaptcha
    }
});