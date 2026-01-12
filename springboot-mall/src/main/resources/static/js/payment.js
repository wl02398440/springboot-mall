
// 等待頁面載入完成
document.addEventListener("DOMContentLoaded", function() {

    // 解析網址參數
    const urlParams = new URLSearchParams(window.location.search);
    const orderId = urlParams.get('oid');
    const total = urlParams.get('total');

    // 將資訊顯示在html上
    document.getElementById('orderId').innerText = orderId;
    document.getElementById('amount').innerText = total;

    // 綁定按鈕點擊事件
    const btnPay = document.getElementById('btnPay');
    btnPay.addEventListener('click', function() {
        confirmPayment(orderId);
    });
});

// 執行付款動作
function confirmPayment(orderId) {
    // 模擬付款
    Swal.fire({
        title: '正在連線銀行端...',
        html: '請勿關閉視窗',
        timer: 1500,
        timerProgressBar: true,
        didOpen: () => {
            Swal.showLoading();
        }
    }).then(() => {
        fetch(`http://localhost:8080/updateOrder/${orderId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include'
        })
            .then(response => {
                if (response.ok) {
                    Swal.fire({
                        icon: 'success',
                        title: '付款成功！',
                        text: '感謝您的購買，商品將盡快寄出',
                        confirmButtonText: '回到商場'
                    }).then(() => {
                        window.location.href = 'home.html';
                    });
                } else {
                    return response.json().then(errorBody => {
                        let msg = errorBody.message;
                        throw new Error(msg);
                    });
                }
            })
            .catch(error => {
                console.error(error);
                Swal.fire({
                    icon: 'warning',
                    title: error.message,
                });
            });
    });
}