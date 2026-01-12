
//SweetAlert2 icon: 圖示 (success, error, warning, info)

//增加數量
function plusButton(product){
    if(product.stock > product.count) {
        product.count++;
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
    }
}

//搜尋資料庫數據
function searchProducts() {
    this.offset = 0;
    this.fetchProducts();
}

//載入資料庫數據
function fetchProducts() {
    let url = `http://localhost:8080/products?
                limit=${this.limit}&offset=${this.offset}`;
    if (this.selectedCategory !== '') {
        url += `&category=${this.selectedCategory}`;
    }
    if (this.searchKeyword.trim() !== '') {
        url += `&search=${this.searchKeyword.trim()}`;
    }
    console.log("正在查詢 URL:", url);
    fetch(url)
        .then(response => response.json())
        .then(data => {
            console.log("後端回傳的商品資料:", data);
            this.productList = data.results;
            this.total = data.total;
            this.offset = data.offset;
            this.limit = data.limit;
        })
        .catch(error => {
            console.error("發生錯誤:", error);
        });
}

//載入購物車
function fetchBuyItemList(){
    // 發送 GET 請求給後端
    return fetch(`http://localhost:8080/getOrderList/${this.userId}`)
        .then(res => res.json())
        .then(data => {
            console.log("後端回傳的購物車資料:", data);
            this.shopCart = {};
            this.cart = data.results;
            data.results.forEach(item => {
                this.shopCart[item.productId] = item.count;
            });
            console.log("購物車資訊載入完成:", this.shopCart);
        })
}

//清空搜尋
function clearSearch() {
    this.selectedCategory = '';
    this.searchKeyword = '';
    this.offset = 0;
    this.fetchProducts();
}

// 換頁
function changePage(page) {
    if (page < 1 || page > this.totalPages) return;
    // 算出新的 offset
    this.offset = (page - 1) * this.limit;
    // 重新抓資料
    this.fetchProducts();
    // 換頁後自動滾動到最上面
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

//加入購物車
async function addToCart(product) {
    // 檢查數量是否大於 0
    if (product.count <= 0) {
        Swal.fire({
            icon: 'warning',
            title: '請至少選擇 1 個商品！',
            showConfirmButton: false, // 不顯示確定按鈕
            timer: 1000
        });
        return;
    }
    // 取得商品在購物車的數量
    await this.fetchBuyItemList()
    let currentInCart = this.shopCart[product.productId] || 0;
    console.log("商品購物車數量:", currentInCart)
    // 計算庫存是否足夠
    if (currentInCart + product.count > product.stock) {
        let remaining = product.stock - currentInCart;
        Swal.fire({
            icon: 'warning',
            title: '庫存不足',
            text: `購物車已有 ${currentInCart} 個，您只能再買 ${remaining} 個喔！`,
        });
        product.count = remaining;
        return;
    }
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

    // 發送 POST 請求
    fetch(`http://localhost:8080/createOrderList/${this.userId}`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(requestBody) // 轉成 JSON 字串
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("加入失敗");
            }
            return response.json();
        })
        .then(data => {
            console.log("加入成功，後端回傳:", data);
            Swal.fire({
                icon: 'success', // 圖示
                title: '已加入購物車',
                showConfirmButton: false, // 不顯示確定按鈕
                timer: 1000
            });
            product.count = 0;
            this.fetchProducts();
            this.fetchBuyItemList();
        })
        .catch(error => {
            console.error("錯誤:", error);
            alert("加入購物車失敗，請稍後再試");
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


new Vue({
    el:'#app',
    mixins: [authMixin],
    data:{
        productList: [],
        shopCart: {},
        total: 0,
        offset: 0,
        limit: 10,
        errorMsg: '',
        selectedCategory: '',
        searchKeyword: '',
        cart: []
    },
    mounted() {
        this.checkLoginStatus(); // 呼叫共用的
        this.fetchProducts();
        this.fetchBuyItemList();
    },
    computed: {
        // 計算總共有幾頁
        totalPages() {
            return Math.ceil(this.total / this.limit);
        },
        // 計算現在是第幾頁
        currentPage() {
            return Math.floor(this.offset / this.limit) + 1;
        }
    },
    methods: {
        fetchProducts,
        fetchBuyItemList,
        addToCart,
        plusButton,
        subButton,
        searchProducts,
        clearSearch,
        changePage,
        handleDelete
    }
})