
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
        })
        .catch(error => {
            // 錯誤處理
            console.error("發生錯誤:", error);
            Swal.fire({
                icon: 'warning',
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
        || !this.loginForm.captcha ) {
        Swal.fire({
            icon: 'warning',
            title: '請輸入完整資訊 (含驗證碼)',
            showConfirmButton: false, // 不顯示確定按鈕
            timer: 1000
        });
        return;
    }
    // 發送 POST 請求給後端
    fetch('http://localhost:8080/users/login', {
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
            if (shortName === `admin`){
                this.isManager = true;
            }
            console.log("管理者:", shortName);
            userData.userName = shortName;
            userData.isManager = this.isManager;
            this.currentUser = userData.userName;
            this.isLoggedIn = true;
            this.userId = userData.userId;
            sessionStorage.setItem('shop_user', JSON.stringify(userData));
            this.fetchCartInfo(); //載入購物車
            console.log("登入成功，使用者資料:", userData);
            Swal.fire({
                icon: 'success',
                title: '歡迎回來，' + userData.userName,
                showConfirmButton: false, // 不顯示確定按鈕
                timer: 1000
            });
            this.showLogin = false;
            // 清空表單
            this.loginForm.email = '';
            this.loginForm.password = '';
        })
        .catch(error => {
            // 錯誤處理
            console.error("發生錯誤:", error);
            Swal.fire({
                icon: 'warning',
                title: error.message,
                showConfirmButton: false, // 不顯示確定按鈕
                timer: 1000,
            });
        });
}

// 開始選購
function shopNow() {
    if (this.isLoggedIn) {
        location.href = '../html/productMall.html';
    } else {
        Swal.fire({
            icon: 'warning',
            title: '請先登入會員才能開始選購喔！',
            showConfirmButton: false, // 不顯示確定按鈕
            timer: 1000
        }).then(() => {
            this.showLogin = true;// 打開登入視窗
        })
    }
}

// 顧客訂單
function orderUser() {
    if (this.isLoggedIn) {
        location.href = '../html/orderUser.html';
    } else {
        Swal.fire({
            icon: 'warning',
            title: '請先登入會員喔！',
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

// 自動播放
function startSlide() {
    this.timer = setInterval(this.nextSlide, 3000);
}

// 停止自動撥放
function stopSlide() {
    clearInterval(this.timer);
    this.timer = null;
}

// 下一個頁面
function nextSlide() {
    this.currentSlide = (this.currentSlide + 1) % this.banners.length;
}

// 上一個頁面
function prevSlide() {
    this.currentSlide = (this.currentSlide - 1 + this.banners.length) % this.banners.length;
}

//載入購物車
function fetchCartInfo() {
    if(!this.userId) return;

    fetch(`http://localhost:8080/getOrderList/${this.userId}`, {
        method: 'GET',
        // cookie
        credentials: 'include'
    })
        .then(res => res.json())
        .then(data => {
            this.shopCart = data.results;
        })
        .catch(err => console.error("無法取得購物車", err));
}

//刪除商品
function handleDelete(productId){
    // 發送 DELETE 請求給後端
    // API 路徑
    fetch(`http://localhost:8080/deleteOrderList/${this.userId}/${productId}`,{
        method: 'DELETE'
    })
        .then(response => {
            // 檢查後端回應狀態
            if (!response.ok) {
                throw new Error('網路回應不正常');
            }
            return response.json();
        })
        .then(data => {
            console.log("後端回傳的資料:", data);
            this.fetchCartInfo();
        })
        .catch(error => {
            // 錯誤處理
            console.error("發生錯誤:", error);
            alert(error.message); // 彈出視窗
        });
}

new Vue({
    el: '#app',
    mixins: [authMixin],
    data: {
        showLogin: false,
        showRegister: false,
        showQA: false,
        loginForm: {
            email: 'user1@gmail.com',
            password: '111',
            captcha: ''
        },
        captchaUrl: 'http://localhost:8080/captcha',
        // --- 輪播圖設定 ---
        currentSlide: 0,
        timer: null,
        // 圖片設定
        banners: [
            'http://localhost:8080/images/home-1.jpg',
            'http://localhost:8080/images/home-2.jpg',
            'http://localhost:8080/images/home-3.jpg',
            'http://localhost:8080/images/home-4.jpg'
        ],
        shopCart: []
    },
    mounted() {
        this.checkLoginStatus(); // 呼叫共用的
        this.startSlide();
        if (this.isLoggedIn) {
            this.fetchCartInfo(); //載入購物車
        }
    },
    // 離開頁面或是元件銷毀前，停止計時器，避免記憶體洩漏
    beforeDestroy() {
        this.stopSlide();
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
        refreshCaptcha,
        startSlide,
        stopSlide,
        nextSlide,
        prevSlide,
        fetchCartInfo,
        handleDelete,
        orderUser
    }
});