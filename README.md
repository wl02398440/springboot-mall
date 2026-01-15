# 🛒 SIMPLESHOP 購物平台

> SIMPLESHOP 是一個模擬電商運作的購物平台專案。
> (無需安裝 MySQL 或 Maven 即可直接執行)

![Project Status](https://img.shields.io/badge/status-active-brightgreen)
![Backend](https://img.shields.io/badge/backend-Spring%20Boot-green)
![Frontend](https://img.shields.io/badge/frontend-Vue.js-blue)
![Database](https://img.shields.io/badge/database-H2-orange)

## 🛠️ 技術架構
* **後端**：採用 **Spring Boot** 建構 RESTful API。
* **資料庫**：使用 **H2 In-Memory Database** 進行儲存，方便快速開發與測試。
* **前端**：使用 **Vue.js** 打造使用者介面。

## ✨ 主要功能

### 1. 商品管理系統
* **商品 CRUD**：支援新增、修改、刪除與查詢商品。
* **圖片上傳優化**：實作前端圖片預覽與檔案上傳功能。

### 2. 訂單管理系統
* **訂單系統**：支援更改訂單狀態、查看訂單商品明細。
* **購物系統**：模擬綠界結帳功能、商品、訂單隨結帳後同步更改。

### 2. 會員管理系統
* **郵件服務**：支援註冊、登入功能。
* **郵件服務**：註冊成功後，自動發送通知信件。

## 🚀 快速開始

本專案內建 Maven Wrapper，只需準備 JDK 17+ 環境。  

下載專案後在JDK17+環境執行SpringbootMallApplication (src/main/java/com.sam.springbootmall/SpringbootMallApplication)，

系統啟動後，請直接開啟：http://localhost:8080

**測試帳號**：網頁開啟後，登入帳號密碼預設為`user1@gmail.com`/111。
```text
角色	 帳號	            密碼
管理員	 admin@gmail.com	111
會員	 user1@gmail.com	111
```

### 📂 專案結構

```text
com.sam.springbootmall
├── src
│   └── main
│       ├── java        # 後端 Java 程式碼
│       │   └── com.sam.springbootmall
│       │       ├── config      # 設定檔
│       │       ├── constant    # Enums (ProductCategory)
│       │       ├── controller  # Controller 層 (API 接口)
│       │       ├── dao         # 資料庫存取層 (Repository)
│       │       ├── dto         # 資料傳輸物件 (Request/Response)
│       │       ├── model       # 資料庫實體 (Entity)
│       │       ├── rowmapper   # 資料庫傳輸媒介
│       │       ├── service     # 業務邏輯層
│       │       ├── util        # 輔助工具
│       │       └── SpringbootMallApplication.java  # 執行專案程式
│       └── resources
│           ├── static          # 前端程式碼 (HTML, CSS, JS)
│           ├── application.properties  # 專案設定檔
│           ├── data.sql        # 資料庫初始化腳本(insert values)
│           └── schema.sql      # 資料庫初始化腳本(create table)
└── uploads             # 圖片儲存區
```

