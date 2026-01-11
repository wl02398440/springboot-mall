// 載入訂單
function fetchOrders() {
        fetch(`http://localhost:8080/orders`, {
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

//訂單出貨
function shipped(orderId) {
    Swal.fire({
        title: '確定出貨?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: '確定'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`http://localhost:8080/updateOrder/${orderId}/shipped`, {
                method: 'PUT'
            })
                .then(response => {
                    if (response.ok) {
                        Swal.fire({
                            icon: 'success',
                            title: '出貨成功！'
                        }).then(() => {
                            this.fetchOrders();
                        });
                    } else {
                        Swal.fire('出貨失敗', '伺服器回應錯誤', 'error');
                    }
                })
        }
    });
}

//取消訂單
function cancelOrder(orderId) {
    Swal.fire({
        title: '確定要取消?',
        text: "取消訂單無法復原",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: '取消'
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

new Vue({
    el: '#app',
    mixins: [authMixin],
    data: {
        orderList: [],
        // 彈窗控制變數
        showModal: false,
        currentOrder: {},     // 存放當前選中的訂單資訊(by orderId)
        currentOrderItems: [] // 存放當前訂單的商品清單
    },
    mounted() {
        this.checkLoginStatus(); // 呼叫共用的
        this.fetchOrders();
    },
    methods: {
        fetchOrders,
        formatDate,
        getStatusClass,
        viewDetail,
        closeModal,
        shipped,
        cancelOrder,
    }
});
