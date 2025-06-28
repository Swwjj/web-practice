<template>
  <div class="product-detail">
    <!-- 顶部导航 -->
    <div class="header">
      <button class="back-btn" @click="goBack">
        <span>←</span> 返回
      </button>
      <h1 class="page-title">商品详情</h1>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>加载中...</p>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="error">
      <p>{{ error }}</p>
      <button @click="fetchProductDetail" class="retry-btn">重试</button>
    </div>

    <!-- 商品详情内容 -->
    <div v-else-if="product" class="product-content">
      <!-- 商品图片区域 -->
      <div class="product-images">
        <div class="main-image">
          <img :src="product.iconUrl || '/default-product.png'" :alt="product.name" />
        </div>
        <div v-if="subImages.length > 0" class="sub-images">
          <div 
            v-for="(image, index) in subImages" 
            :key="index"
            class="sub-image"
            @click="currentImage = image"
          >
            <img :src="image" :alt="`${product.name} - 图片${index + 1}`" />
          </div>
        </div>
      </div>

      <!-- 商品信息区域 -->
      <div class="product-info">
        <div class="product-header">
          <h2 class="product-name">{{ product.name }}</h2>
          <div v-if="product.hot" class="hot-badge">🔥 热销</div>
        </div>

        <div class="price-section">
          <div class="price">¥{{ product.price }}</div>
          <div class="stock">库存: {{ product.stock }} 件</div>
        </div>

        <div v-if="product.specParam" class="spec-section">
          <h3>规格参数</h3>
          <div class="spec-content">{{ product.specParam }}</div>
        </div>

        <div class="action-section">
          <div class="quantity-selector">
            <label>数量:</label>
            <div class="quantity-controls">
              <button @click="decreaseQuantity" :disabled="quantity <= 1">-</button>
              <input v-model.number="quantity" type="number" min="1" :max="product.stock" />
              <button @click="increaseQuantity" :disabled="quantity >= product.stock">+</button>
            </div>
          </div>

          <div class="action-buttons">
            <button class="add-to-cart-btn" @click="addToCart" :disabled="product.stock <= 0">
              {{ product.stock > 0 ? '加入购物车' : '已售罄' }}
            </button>
            <button class="buy-now-btn" @click="buyNow" :disabled="product.stock <= 0">
              {{ product.stock > 0 ? '立即购买' : '已售罄' }}
            </button>
          </div>
        </div>
      </div>

      <!-- 商品详情区域 -->
      <div class="detail-section">
        <h3>商品详情</h3>
        <div class="detail-content" v-html="product.detail"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getProductDetail } from '@/api/product';

const route = useRoute();
const router = useRouter();

const product = ref(null);
const loading = ref(true);
const error = ref('');
const quantity = ref(1);
const currentImage = ref('');

// 计算子图片数组
const subImages = computed(() => {
  if (!product.value?.subImages) return [];
  return product.value.subImages.split(',').filter(img => img.trim());
});

// 获取商品详情
const fetchProductDetail = async () => {
  const productId = route.params.id;
  if (!productId) {
    error.value = '商品ID不能为空';
    loading.value = false;
    return;
  }

  try {
    loading.value = true;
    error.value = '';
    const res = await getProductDetail(productId);
    
    if (res.status === 0) {
      product.value = res.data;
      currentImage.value = res.data.iconUrl;
    } else {
      error.value = res.msg || '获取商品详情失败';
    }
  } catch (e) {
    error.value = e.message || '获取商品详情失败';
  } finally {
    loading.value = false;
  }
};

// 数量控制
const decreaseQuantity = () => {
  if (quantity.value > 1) {
    quantity.value--;
  }
};

const increaseQuantity = () => {
  if (product.value && quantity.value < product.value.stock) {
    quantity.value++;
  }
};

// 加入购物车
const addToCart = () => {
  // TODO: 实现加入购物车功能
  alert(`已将 ${quantity.value} 件 ${product.value.name} 加入购物车`);
};

// 立即购买
const buyNow = () => {
  // TODO: 实现立即购买功能
  alert(`立即购买 ${quantity.value} 件 ${product.value.name}`);
};

// 返回上一页
const goBack = () => {
  router.back();
};

onMounted(() => {
  fetchProductDetail();
});
</script>

<style scoped>
.product-detail {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem;
}

.header {
  display: flex;
  align-items: center;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #eee;
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

.page-title {
  margin-left: 1rem;
  font-size: 1.5rem;
  color: #2c3e50;
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

.product-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
  margin-bottom: 2rem;
}

.product-images {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.main-image {
  width: 100%;
  height: 400px;
  border-radius: 8px;
  overflow: hidden;
  background: #f8f9fa;
  display: flex;
  align-items: center;
  justify-content: center;
}

.main-image img {
  max-width: 100%;
  max-height: 100%;
  object-fit: cover;
}

.sub-images {
  display: flex;
  gap: 0.5rem;
  overflow-x: auto;
}

.sub-image {
  width: 80px;
  height: 80px;
  border-radius: 6px;
  overflow: hidden;
  cursor: pointer;
  border: 2px solid transparent;
  transition: border-color 0.3s ease;
}

.sub-image:hover {
  border-color: #3498db;
}

.sub-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.product-header {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.product-name {
  font-size: 1.8rem;
  color: #2c3e50;
  margin: 0;
}

.hot-badge {
  background: #ff4757;
  color: white;
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: bold;
}

.price-section {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.price {
  font-size: 2rem;
  font-weight: bold;
  color: #e74c3c;
}

.stock {
  color: #7f8c8d;
  font-size: 0.9rem;
}

.spec-section h3 {
  margin-bottom: 0.5rem;
  color: #2c3e50;
}

.spec-content {
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 6px;
  color: #2c3e50;
}

.action-section {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.quantity-selector {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.quantity-controls {
  display: flex;
  align-items: center;
  border: 1px solid #dee2e6;
  border-radius: 6px;
  overflow: hidden;
}

.quantity-controls button {
  width: 40px;
  height: 40px;
  border: none;
  background: #f8f9fa;
  cursor: pointer;
  transition: background 0.3s ease;
}

.quantity-controls button:hover:not(:disabled) {
  background: #e9ecef;
}

.quantity-controls button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.quantity-controls input {
  width: 60px;
  height: 40px;
  border: none;
  text-align: center;
  font-size: 1rem;
}

.action-buttons {
  display: flex;
  gap: 1rem;
}

.add-to-cart-btn, .buy-now-btn {
  flex: 1;
  padding: 1rem;
  border: none;
  border-radius: 6px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
}

.add-to-cart-btn {
  background: #f39c12;
  color: white;
}

.add-to-cart-btn:hover:not(:disabled) {
  background: #e67e22;
}

.buy-now-btn {
  background: #e74c3c;
  color: white;
}

.buy-now-btn:hover:not(:disabled) {
  background: #c0392b;
}

.add-to-cart-btn:disabled, .buy-now-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.detail-section {
  grid-column: 1 / -1;
  margin-top: 2rem;
  padding: 2rem;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.detail-section h3 {
  margin-bottom: 1rem;
  color: #2c3e50;
  font-size: 1.3rem;
}

.detail-content {
  line-height: 1.6;
  color: #2c3e50;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .product-content {
    grid-template-columns: 1fr;
    gap: 1rem;
  }

  .main-image {
    height: 300px;
  }

  .action-buttons {
    flex-direction: column;
  }

  .price-section {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style> 