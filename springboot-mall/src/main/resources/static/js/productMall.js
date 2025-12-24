
//增加數量
function plusButton(product){
    product.count++;
}
//減少數量
function subButton(product) {
    if (product.count > 0) {
        product.count--;
    }
}
//刪除商品
function handledelete(index){
    this.productList.splice(index,1);
}
//載入資料庫數據
function fetchProducts() {
    fetch('http://localhost:8080/products')
        .then(response => {
            // 檢查 HTTP 狀態碼是否為 200
            if (!response.ok) {
                throw new Error('網路回應不正常');
            }
            return response.json();
        })
        .then(data => {
            console.log("後端回傳的資料:", data); // F12看主控台

            this.productList = data.results;
            this.total = data.total;
            this.imageUrl = data.imageUrl
        })
        .catch(error => {
            console.error("發生錯誤:", error);
            this.errorMsg = "無法連線到後端，請確認 SpringBoot 是否已啟動";
        });
}

new Vue({
    el:'#app',
    mixins: [authMixin],
    data:{
        productList: [],
        total: 0,
        errorMsg: ''
    },
    mounted() {
        this.checkLoginStatus(); // 呼叫共用的
        this.fetchProducts();
    },
    methods: {
        fetchProducts,
        plusButton,
        subButton,
        handledelete
    }
})