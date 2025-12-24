new Vue({
    el: '#app',
    mixins: [authMixin],
    // data: {
    //     showLogin: false,    // 控制登入視窗顯示
    //     showRegister: false, // 控制註冊視窗顯示
    //     loginForm: {
    //         email: 'www1@gmail.com',
    //         password: '111'
    //     },
    //     confirmPassword: '',
    //     isLoggedIn: false,  // 登入狀態
    // },
    mounted() {
        this.checkLoginStatus(); // 呼叫共用的
    },
    methods: {
        login,
        register,
        shopNow,
        switchModal,
        claen
    }
});
// 註冊帳號
function register() {
    // 前端防呆
    if (!this.loginForm.email || !this.loginForm.password || !this.confirmPassword) {
        alert("請輸入 Email 和密碼");
        return;
    }
    if (this.loginForm.password !== this.confirmPassword) {
        alert("密碼不一致");
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
            alert(`註冊成功，請使用新帳號登入！`);

            this.showRegister = false; // 關閉註冊視窗
            // 清空表單
            this.loginForm.email = '';
            this.loginForm.password = '';
        })
        .catch(error => {
            // 錯誤處理
            console.error("發生錯誤:", error);
            alert(error.message); // 彈出視窗告訴使用者失敗原因
        });
}

// 登入帳號
function login(){
    // 前端防呆
    if (!this.loginForm.email || !this.loginForm.password) {
        alert("請輸入 Email 和密碼");
        return;
    }
    // 發送 POST 請求給後端
    fetch('http://localhost:8080/users/login', {  // API 路徑
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
            // 登入成功後的處理
            let shortName = userData.email.split('@')[0];
            userData.userName = shortName;
            this.currentUser = userData.userName;
            this.isLoggedIn = true;
            sessionStorage.setItem('shop_user', JSON.stringify(userData));
            console.log("登入成功，使用者資料:", userData);
            alert(`歡迎回來，${userData.userName}！`);
            this.showLogin = false; // 關閉登入視窗
            // 清空表單
            this.loginForm.email = '';
            this.loginForm.password = '';
        })
        .catch(error => {
            // 錯誤處理
            console.error("發生錯誤:", error);
            alert(error.message); // 彈出視窗告訴使用者失敗原因
        });
}

// 開始選購
function shopNow() {
    if (this.isLoggedIn) {
        location.href = '../html/productMall.html';
    } else {
        alert("請先登入會員才能開始選購喔！");
        this.showLogin = true; // 打開登入視窗
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