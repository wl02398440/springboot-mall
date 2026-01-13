
// 載入訂單
function fetchOrders() {
        fetch(`http://localhost:8080/orders/${this.userId}`, {
            method: 'GET',
            credentials: 'include'
        })
            .then(res => res.json())
            .then(data => {
                this.total = data.total;
                this.offset = data.offset;
                this.limit = data.limit;
                this.orderList = data.results;
                console.log("訂單資料:", this.orderList);
            })
            .catch(err => {
        console.error("載入失敗", err);
    });
}

// 結帳
function checkout(orderId, totalAmount) {
    window.location.href =
        `payment.html?oid=${orderId}&total=${totalAmount}`;
}

// 取消訂單
function cancelOrder(orderId) {
    Swal.fire({
        title: '確定要取消?',
        text: "取消訂單無法復原",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: '確定'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`http://localhost:8080/updateOrder/${orderId}/cancel`, {
                method: 'PUT'
            })
                .then(response => {
                    if (response.ok) {
                        Swal.fire({
                            icon: 'success',
                            title: '取消成功！'
                        }).then(() => {
                            this.fetchOrders();
                        });
                    } else {
                        Swal.fire('取消失敗', '伺服器回應錯誤', 'error');
                    }
                })
        }
    });
}

// 格式化日期
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleString('zh-TW', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    });
}

// 根據狀態回傳對應的 CSS class
function getStatusClass(status) {
    switch (status) {
        case '未付款': return 'status-unpaid';
        case '已付款': return 'status-paid';
        case '已出貨': return 'status-shipped';
        case '已取消': return 'status-cancel';
        default: return '';
    }
}

// 顯示訂單詳情
function viewDetail(orderId) {
    this.currentOrder = this.orderList.find(o => o.orderId === orderId);
    this.currentOrderItems = this.currentOrder.orderItems;
    this.showModal = true;
}

// 關閉訂單詳情
function closeModal() {
    this.showModal = false;
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
    el: '#app',
    mixins: [authMixin],
    data: {
        orderList: [],
        showModal: false,
        currentOrder: {},     // 存放當前選中的訂單資訊(by orderId)
        currentOrderItems: [], // 存放當前訂單的商品清單
        shopCart: {},
        cart: []
    },
    mounted() {
        this.checkLoginStatus(); // 呼叫共用的
        this.fetchOrders();
        this.fetchBuyItemList();
    },
    methods: {
        fetchOrders,
        formatDate,
        getStatusClass,
        viewDetail,
        closeModal,
        checkout,
        cancelOrder,
        fetchBuyItemList,
        handleDelete
    }
});
