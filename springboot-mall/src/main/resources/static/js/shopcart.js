
//增加數量
function plusButton(product){
    if(product.stock > product.count) {
        product.count++;
        this.updateCount(product);
    } else {
        this.$set(product, 'showStockError', true);
        setTimeout(() => {
            this.$set(product, 'showStockError', false);
        }, 1500);
    }
}

//減少數量
function subButton(product) {
    if (product.count > 0) {
        product.count--;
        this.updateCount(product);
    }
}

//手動輸入購買數量
function handleInput(product) {
    // 輸入空的、非數字、小於1 預設改回0
    if (!product.count || product.count === '' || product.count < 1) {
        product.count = 0;
    }
    // 不能超過庫存
    if (product.count > product.stock) {
        product.count = product.stock;
        // 觸發錯誤提示
        this.$set(product, 'showStockError', true);
        setTimeout(() => {
            this.$set(product, 'showStockError', false);
        }, 1500);
    }
}

//更新購物車
function updateCount(product) {
    // 準備要傳給後端的資料 (JSON)
    const requestBody = {
        buyItemList :[
            {
                productId: product.productId,
                count: product.count
            }
        ]
    };
    console.log("準備送出的資料:", requestBody);

    // 發送 PUT 請求
    fetch(`http://localhost:8080/updateOrderList/${this.userId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody) // 轉成 JSON 字串
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("更新失敗");
            }
            return response.json();
        })
        .then(data => {
            console.log("更改成功，後端回傳:", data);
            this.fetchBuyItemList();
        })
        .catch(error => {
            console.error("錯誤:", error);
            alert("更改失敗，請稍後再試");
        });
}

//刪除商品
function handleDelete(productId){

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
            this.fetchBuyItemList();
        })
        .catch(error => {
            // 錯誤處理
            console.error("發生錯誤:", error);
            alert(error.message); // 彈出視窗
        });
}

//載入購物車
function fetchBuyItemList(){
    // 發送 GET 請求給後端
    fetch(`http://localhost:8080/getOrderList/${this.userId}`)
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
        .then(data => {
            console.log("後端回傳的資料:", data);

            this.productList = data.results;
            this.total = data.total;
            this.offset = data.offset
        })
        .catch(error => {
            // 錯誤處理
            console.error("發生錯誤:", error);
            alert(error.message); // 彈出視窗
        });
}

//結帳
function checkout(){
    if (this.productList.length === 0) {
        Swal.fire("購物車是空的喔！");
        return;
    }

    const buyItemList = this.productList.map(item => {
        return {
            productId: item.productId,
            count: item.count
        };
    });
    console.log("buyItemList:", buyItemList);
    const orderRequest = {
        buyItemList: buyItemList
    };

    // 模擬讀取中
    Swal.fire({
        title: '訂單建立中...',
        didOpen: () => { Swal.showLoading() }
    });

    fetch(`http://localhost:8080/${this.userId}/${this.currentUser}/orders`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(orderRequest),
        credentials: 'include'
    })
        .then(res => {
            if (!res.ok) throw new Error("建立訂單失敗");
            return res.json();
        })
        .then(orderData => {
            // 關閉 Loading
            Swal.close();
            // 建立成功後跳轉到 payment.html
            window.location.href =
                `payment.html?oid=${orderData.orderId}&total=${orderData.totalAmount}`;
        })
        .catch(err => {
            console.error(err);
            Swal.fire("結帳失敗", "請稍後再試", "error");
        });

}

new Vue({
    el:'#app',
    mixins: [authMixin],
    data:{
        productList: [],
        total: 0,
        offset: 0,
        errorMsg: '',
    },
    computed:{
        totalMoney(){
            return this.productList.reduce((sum, item) => {
                return sum + (item.price * item.count);
            }, 0);
        }
    },
    mounted() {
        this.checkLoginStatus(); // 呼叫共用的
        this.fetchBuyItemList();
    },
    methods: {
        fetchBuyItemList,
        plusButton,
        subButton,
        handleDelete,
        updateCount,
        handleInput,
        checkout,
    }
})
