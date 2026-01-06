// 載入商品列表
function fetchManageProducts() {
    let url = `http://localhost:8080/products?limit=${this.limit}&offset=${this.offset}`;

    // 種類
    if (this.selectedCategory !== '') {
        url += `&category=${this.selectedCategory}`;
    }
    // 關鍵字
    if (this.searchKeyword.trim() !== '') {
        url += `&search=${this.searchKeyword.trim()}`;
    }
    console.log("前端拋出 URL:", url);

    fetch(url)
        .then(response => response.json())
        .then(data => {
            console.log("資料載入完成:", data);
            this.productList = data.results;
            this.total = data.total;
            // this.offset = data.offset;
        })
        .catch(err => console.error(err));
}

// 搜尋
function searchProducts() {
    this.offset = 0; // 搜尋時重置回第一頁
    this.fetchManageProducts();
}

function clearSearch() {
    this.selectedCategory = '';
    this.searchKeyword = '';
    this.offset = 0;
    this.fetchManageProducts();
}

// 分頁
function changePage(page) {
    if (page < 1 || page > this.totalPages) return;
    // this.selectedCategory = '';
    this.offset = (page - 1) * this.limit;
    this.fetchManageProducts();
    // 換頁後到最上方
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

// 新增商品
function addProduct() {
    const newProduct = {
        productId: null,
        productName: '',
        imageUrl: '',
        price: 0,
        stock: 0,
        category: 'FOOD', // 預設
        isNew: true
    };

    // 加在上面
    this.productList.unshift(newProduct);
}

// 儲存商品
function saveProduct(product, index) {
    if (!product.productName || !product.price || !product.stock) {
        Swal.fire('欄位錯誤', '請輸入商品名稱、價格、庫存', 'warning');
        return;
    }
    if (product.price < 0 || product.stock < 0) {
        Swal.fire('欄位錯誤', '商品價格、庫存不可小於0', 'warning');
        return;
    }

    // 新增商品
    if (product.isNew) {
        url = 'http://localhost:8080/products';
        method = 'POST';
        delete product.isNew;
    } else {
        url = `http://localhost:8080/products/${product.productId}`;
        method = 'PUT';
    }
    // 準備要傳給後端的資料 (JSON)
    const requestBody = {
        productName: product.productName,
        category: product.category,
        imageUrl: product.imageUrl,
        price: product.price,
        stock: product.stock,
    };
    console.log("準備送出的資料:", requestBody);

    fetch(url, {
        method: method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(requestBody)
    })
        .then(response => {
            if (response.ok) {
                Swal.fire({
                    icon: 'success',
                    title: '儲存成功',
                    showConfirmButton: false,
                    timer: 800
                });
                // 重新載入頁面
                this.fetchManageProducts();
            } else {
                throw new Error('儲存失敗');
            }
        })
        .catch(error => {
            Swal.fire('錯誤', '儲存失敗', 'error');
        });
}

// 刪除商品
function deleteProduct(product, index) {
    if (product.isNew) {
        this.productList.splice(index, 1);
        return;
    }

    Swal.fire({
        title: '確定要下架?',
        text: "此操作無法復原",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: '下架'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`http://localhost:8080/products/${product.productId}`, {
                method: 'DELETE'
            })
                .then(res => {
                    if(res.ok) {
                        Swal.fire('已刪除', '', 'success');
                        this.fetchManageProducts();
                    } else {
                        Swal.fire('失敗', '刪除失敗', 'error');
                    }
                });
        }
    });
}

// ...檔案上傳
function triggerFileUpload(index) {
    this.$refs.fileInputs[index].click();
}

// 處理檔案上傳
function handleFileUpload(event, product) {
    const file = event.target.files[0];
    if (!file) return;

    // 限制檔案大小 (例如 2MB)
    if (file.size > 2 * 1024 * 1024) {
        Swal.fire('檔案過大', '請上傳小於 2MB 的圖片', 'warning');
        return;
    }
    console.log("選取的檔案:", file);

    // --- 方案 A: 真實上傳 (需要後端 API 配合) ---
    // 這裡示範如果您有後端 API 該怎麼寫：

    const formData = new FormData();
    formData.append('file', file);

    fetch('http://localhost:8080/upload', {
        method: 'POST',
        body: formData
    })
    .then(res => res.text()) // 假設後端回傳圖片網址字串
    .then(imageUrl => {
        product.imageUrl = imageUrl; // 將回傳的網址填入欄位
    });


    // --- 方案 B: 前端預覽 (暫時使用，不用後端) ---
    // 這會產生一個暫時的網址 (blob:http://...)，讓你可以馬上看到圖
    // 缺點：沒辦法真正存到資料庫供其他人看
    // const previewUrl = URL.createObjectURL(file);
    // product.imageUrl = previewUrl;
    //
    // // 清空 input，這樣下次選同一個檔案才會觸發 change 事件
    // event.target.value = '';
}

new Vue({
    el: '#app',
    mixins: [authMixin],
    data: {
        productList: [],
        total: 0,
        offset: 0,
        limit: 5,
        selectedCategory: '',
        searchKeyword: ''
    },
    computed: {
        totalPages() {
            return Math.ceil(this.total / this.limit);
        },
        currentPage() {
            return Math.floor(this.offset / this.limit) + 1;
        }
    },
    mounted() {
        this.checkLoginStatus();
        this.fetchManageProducts();
    },
    methods: {
        fetchManageProducts,
        searchProducts,
        clearSearch,
        changePage,
        addBlankRow: addProduct,
        saveProduct,
        deleteProduct,
        triggerFileUpload,
        handleFileUpload
    }
});