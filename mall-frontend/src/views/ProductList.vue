<template>
  <div class="product-list">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1>商品列表</h1>
      <button class="back-btn" @click="goBack">
        <span>←</span> 返回首页
      </button>
    </div>

    <!-- 筛选表单 -->
    <div class="filter-section">
      <form @submit.prevent="handleSearch" class="filter-form">
        <div class="form-row">
          <div class="form-group">
            <label>产品类型:</label>
            <select v-model="filters.productTypeId">
              <option value="0">全部类型</option>
              <option v-for="type in productTypes" :key="type.id" :value="type.id">{{ type.name }}</option>
            </select>
          </div>
          
          <div class="form-group">
            <label>配件类型:</label>
            <select v-model="filters.partsId">
              <option value="0">全部配件</option>
              <option v-for="type in productTypes" :key="type.id" :value="type.id">
                {{ type.name }}
              </option>
            </select>
          </div>
          
          <div class="form-group">
            <label>商品名称:</label>
            <input 
              type="text" 
              v-model="filters.name" 
              placeholder="输入商品名称搜索"
            />
          </div>
          
          <div class="form-group">
            <label>每页显示:</label>
            <select v-model="filters.pageSize">
              <option value="10">10条</option>
              <option value="20">20条</option>
              <option value="50">50条</option>
            </select>
          </div>
        </div>
        
        <div class="form-actions">
          <button type="submit" class="search-btn">搜索</button>
          <button type="button" @click="resetFilters" class="reset-btn">重置</button>
        </div>
      </form>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>加载中...</p>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="error">
      <p>{{ error }}</p>
      <button @click="fetchProducts" class="retry-btn">重试</button>
    </div>

    <!-- 商品列表 -->
    <div v-else class="products-section">
      <!-- 结果统计 -->
      <div class="results-info">
        <span>共找到 {{ pagination.totalRecord }} 件商品</span>
        <span>第 {{ pagination.pageNum }} 页，共 {{ pagination.totalPage }} 页</span>
      </div>

      <!-- 商品网格 -->
      <div v-if="products.length > 0" class="products-grid">
        <div 
          v-for="product in products" 
          :key="product.id" 
          class="product-card"
          @click="goToProductDetail(product.id)"
        >
          <div class="product-image">
            <img :src="product.iconUrl || '/default-product.png'" :alt="product.name" />
            <div v-if="product.hot" class="hot-badge">{{ product.hotStatus }}</div>
          </div>
          
          <div class="product-info">
            <h3 class="product-name">{{ product.name }}</h3>
            <div class="product-category">
              <span>{{ product.productCategory }}</span>
              <span>{{ product.partsCategory }}</span>
            </div>
            <div class="product-price">¥{{ product.price }}</div>
            <div class="product-status">{{ product.statusDesc }}</div>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-state">
        <p>暂无商品数据</p>
      </div>

      <!-- 分页组件 -->
      <div v-if="pagination.totalPage > 1" class="pagination">
        <button 
          @click="changePage(pagination.prePage)" 
          :disabled="pagination.pageNum <= 1"
          class="page-btn"
        >
          上一页
        </button>
        
        <div class="page-numbers">
          <button 
            v-for="page in getPageNumbers()" 
            :key="page"
            @click="changePage(page)"
            :class="['page-btn', { active: page === pagination.pageNum }]"
          >
            {{ page }}
          </button>
        </div>
        
        <button 
          @click="changePage(pagination.nextPage)" 
          :disabled="pagination.pageNum >= pagination.totalPage"
          class="page-btn"
        >
          下一页
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getProductList } from '@/api/product';
import { getProductTypes } from '@/api/productType';

const router = useRouter();

// 响应式数据
const loading = ref(false);
const error = ref('');
const products = ref([]);
const productTypes = ref([]);
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  totalRecord: 0,
  totalPage: 1,
  startIndex: 0,
  prePage: 1,
  nextPage: 1
});

// 筛选条件
const filters = reactive({
  productTypeId: '0',
  partsId: '0',
  pageNum: '1',
  pageSize: '10',
  name: ''
});

// 获取产品类型数据
const fetchProductTypes = async () => {
  try {
    const res = await getProductTypes();
    if (res.status === 0) {
      productTypes.value = res.data;
    }
  } catch (e) {
    console.error('获取产品类型失败:', e);
  }
};

// 获取商品列表
const fetchProducts = async () => {
  try {
    loading.value = true;
    error.value = '';
    
    const params = {
      productTypeId: filters.productTypeId,
      partsId: filters.partsId,
      pageNum: filters.pageNum,
      pageSize: filters.pageSize,
      name: filters.name
    };
    
    const res = await getProductList(params);
    
    if (res.status === 0) {
      products.value = res.data.data;
      Object.assign(pagination, res.data);
    } else {
      error.value = res.msg || '获取商品列表失败';
    }
  } catch (e) {
    error.value = e.message || '获取商品列表失败';
  } finally {
    loading.value = false;
  }
};

// 搜索处理
const handleSearch = () => {
  filters.pageNum = '1'; // 重置到第一页
  fetchProducts();
};

// 重置筛选条件
const resetFilters = () => {
  filters.productTypeId = '0';
  filters.partsId = '0';
  filters.pageNum = '1';
  filters.pageSize = '10';
  filters.name = '';
  fetchProducts();
};

// 切换页面
const changePage = (page) => {
  if (page >= 1 && page <= pagination.totalPage) {
    filters.pageNum = page.toString();
    fetchProducts();
  }
};

// 获取页码数组
const getPageNumbers = () => {
  const pages = [];
  const current = pagination.pageNum;
  const total = pagination.totalPage;
  
  if (total <= 7) {
    for (let i = 1; i <= total; i++) {
      pages.push(i);
    }
  } else {
    if (current <= 4) {
      for (let i = 1; i <= 5; i++) {
        pages.push(i);
      }
      pages.push('...');
      pages.push(total);
    } else if (current >= total - 3) {
      pages.push(1);
      pages.push('...');
      for (let i = total - 4; i <= total; i++) {
        pages.push(i);
      }
    } else {
      pages.push(1);
      pages.push('...');
      for (let i = current - 1; i <= current + 1; i++) {
        pages.push(i);
      }
      pages.push('...');
      pages.push(total);
    }
  }
  
  return pages;
};

// 跳转到商品详情
const goToProductDetail = (productId) => {
  router.push(`/product/${productId}`);
};

// 返回首页
const goBack = () => {
  router.push('/');
};

onMounted(() => {
  fetchProductTypes();
  fetchProducts();
});
</script>

<style scoped>
.product-list {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #eee;
}

.page-header h1 {
  margin: 0;
  color: #2c3e50;
  font-size: 1.8rem;
}

.back-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: #f8f9fa;
  border: 1px solid #dee2e6;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.back-btn:hover {
  background: #e9ecef;
}

.filter-section {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 2rem;
}

.filter-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group label {
  font-weight: 600;
  color: #2c3e50;
}

.form-group select,
.form-group input {
  padding: 0.5rem;
  border: 1px solid #dee2e6;
  border-radius: 4px;
  font-size: 0.9rem;
}

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

.search-btn,
.reset-btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 6px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.search-btn {
  background: #409EFF;
  color: white;
}

.search-btn:hover {
  background: #66b1ff;
}

.reset-btn {
  background: #f8f9fa;
  color: #6c757d;
  border: 1px solid #dee2e6;
}

.reset-btn:hover {
  background: #e9ecef;
}

.loading {
  text-align: center;
  padding: 3rem;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error {
  text-align: center;
  padding: 3rem;
  color: #e74c3c;
}

.retry-btn {
  margin-top: 1rem;
  padding: 0.5rem 1rem;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.results-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 6px;
  font-size: 0.9rem;
  color: #6c757d;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.product-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.product-image {
  height: 200px;
  position: relative;
  overflow: hidden;
  background: #f8f9fa;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.hot-badge {
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  background: #ff4757;
  color: white;
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: bold;
}

.product-info {
  padding: 1rem;
}

.product-name {
  font-size: 1.1rem;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 0.5rem 0;
  line-height: 1.3;
}

.product-category {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
}

.product-category span {
  background: #e9ecef;
  color: #6c757d;
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.8rem;
}

.product-price {
  font-size: 1.3rem;
  font-weight: bold;
  color: #e74c3c;
  margin-bottom: 0.5rem;
}

.product-status {
  font-size: 0.9rem;
  color: #28a745;
  font-weight: 500;
}

.empty-state {
  text-align: center;
  padding: 3rem;
  color: #6c757d;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.5rem;
  margin-top: 2rem;
}

.page-btn {
  padding: 0.5rem 1rem;
  border: 1px solid #dee2e6;
  background: white;
  color: #6c757d;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 40px;
}

.page-btn:hover:not(:disabled) {
  background: #e9ecef;
  border-color: #adb5bd;
}

.page-btn.active {
  background: #409EFF;
  color: white;
  border-color: #409EFF;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-numbers {
  display: flex;
  gap: 0.25rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .form-actions {
    flex-direction: column;
  }
  
  .results-info {
    flex-direction: column;
    gap: 0.5rem;
    text-align: center;
  }
  
  .products-grid {
    grid-template-columns: 1fr;
  }
  
  .pagination {
    flex-wrap: wrap;
  }
}
</style> 