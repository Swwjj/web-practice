<template>
  <div class="home-container">
    <div class="header">
      <h1>欢迎来到商城</h1>
      <div class="user-actions">
        <router-link to="/products" class="main-btn">查看全部商品</router-link>
        <router-link to="/product-types" class="main-btn">产品分类</router-link>
        <router-link to="/profile" class="main-btn">个人中心</router-link>
        <button @click="handleLogout" class="main-btn logout-red">退出登录</button>
      </div>
    </div>
    
    <!-- 楼层商品展示 -->
    <div class="floor-container">
      <!-- 热销商品展示 -->
      <div v-if="hotProducts.length > 0" class="hot-section">
        <h3 class="hot-title">🔥 热销商品</h3>
        <div class="hot-product-grid">
          <div 
            v-for="product in hotProducts" 
            :key="product.id" 
            class="hot-product-card"
            @click="goToProductDetail(product.id)"
          >
            <div class="hot-badge">热销</div>
            <div class="product-image">
              <img :src="product.iconUrl || '/default-product.png'" :alt="product.name" />
            </div>
            <div class="product-info">
              <h4 class="product-name">{{ product.name }}</h4>
              <div class="product-price">¥{{ product.price }}</div>
              <div class="product-stock">库存: {{ product.stock }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 第一层：润滑油、锂基脂等 -->
      <div v-if="floorData.oneFloor && floorData.oneFloor.length > 0" class="floor-section">
        <h3 class="floor-title">润滑油专区</h3>
        <div class="product-grid">
          <div 
            v-for="product in floorData.oneFloor" 
            :key="product.id" 
            class="product-card"
            @click="goToProductDetail(product.id)"
          >
            <div class="product-image">
              <img :src="product.iconUrl || '/default-product.png'" :alt="product.name" />
            </div>
            <div class="product-info">
              <h4 class="product-name">{{ product.name }}</h4>
              <div class="product-price">¥{{ product.price }}</div>
              <div class="product-stock">库存: {{ product.stock }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 第二层：机械配件 -->
      <div v-if="floorData.twoFloor && floorData.twoFloor.length > 0" class="floor-section">
        <h3 class="floor-title">机械配件专区</h3>
        <div class="product-grid">
          <div 
            v-for="product in floorData.twoFloor" 
            :key="product.id" 
            class="product-card"
            @click="goToProductDetail(product.id)"
          >
            <div class="product-image">
              <img :src="product.iconUrl || '/default-product.png'" :alt="product.name" />
            </div>
            <div class="product-info">
              <h4 class="product-name">{{ product.name }}</h4>
              <div class="product-price">¥{{ product.price }}</div>
              <div class="product-stock">库存: {{ product.stock }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 第三层：压路机等大型设备 -->
      <div v-if="floorData.threeFloor && floorData.threeFloor.length > 0" class="floor-section">
        <h3 class="floor-title">工程设备专区</h3>
        <div class="product-grid">
          <div 
            v-for="product in floorData.threeFloor" 
            :key="product.id" 
            class="product-card"
            @click="goToProductDetail(product.id)"
          >
            <div class="product-image">
              <img :src="product.iconUrl || '/default-product.png'" :alt="product.name" />
            </div>
            <div class="product-info">
              <h4 class="product-name">{{ product.name }}</h4>
              <div class="product-price">¥{{ product.price }}</div>
              <div class="product-stock">库存: {{ product.stock }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading">加载中...</div>
      
      <!-- 错误状态 -->
      <div v-if="error" class="error">{{ error }}</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { getFloorProducts, getHotProducts } from '@/api/product';

const router = useRouter();
const userStore = useUserStore();

const loading = ref(true);
const error = ref('');
const floorData = ref({
  oneFloor: [],
  twoFloor: [],
  threeFloor: [],
  fourFloor: []
});
const hotProducts = ref([]);

const handleLogout = async () => {
  await userStore.logout();
  router.push('/login');
};

const goToProductDetail = (productId) => {
  router.push(`/product/${productId}`);
};

const fetchFloorProducts = async () => {
  loading.value = true;
  error.value = '';
  try {
    const res = await getFloorProducts();
    if (res.status === 0) {
      floorData.value = res.data;
    } else {
      error.value = res.msg || '获取楼层数据失败';
    }
  } catch (e) {
    error.value = e.message || '获取楼层数据失败';
  } finally {
    loading.value = false;
  }
};

const fetchHotProducts = async () => {
  try {
    const res = await getHotProducts();
    if (res.status === 0) {
      hotProducts.value = res.data;
    } else {
      error.value = res.msg || '获取热销商品失败';
    }
  } catch (e) {
    error.value = e.message || '获取热销商品失败';
  }
};

onMounted(() => {
  fetchFloorProducts();
  fetchHotProducts();
});
</script>

<style scoped>
.home-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}
.main-btn {
  display: inline-block;
  padding: 8px 24px;
  background: #409EFF;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 15px;
  line-height: 1.2;
  cursor: pointer;
  margin-right: 8px;
  text-decoration: none;
  transition: background 0.2s;
}
.main-btn:last-child {
  margin-right: 0;
}
.main-btn:hover {
  background: #66b1ff;
  text-decoration: none;
}
.logout-red {
  background: #f56c6c;
}
.logout-red:hover {
  background: #ff7875;
}

/* 楼层商品样式 */
.floor-container {
  margin-top: 20px;
}
.floor-section {
  margin-bottom: 40px;
}
.floor-title {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #409EFF;
}
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}
.product-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}
.product-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0,0,0,0.15);
}
.product-image {
  height: 200px;
  overflow: hidden;
  background: #f5f5f5;
}
.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.product-info {
  padding: 16px;
}
.product-name {
  font-size: 16px;
  color: #333;
  margin: 0 0 8px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.product-price {
  font-size: 18px;
  color: #f56c6c;
  font-weight: bold;
  margin-bottom: 4px;
}
.product-stock {
  font-size: 14px;
  color: #999;
}
.loading {
  text-align: center;
  padding: 40px;
  color: #666;
}
.error {
  text-align: center;
  padding: 40px;
  color: #f56c6c;
}

/* 热销商品样式 */
.hot-section {
  margin-bottom: 2rem;
  padding: 1.5rem;
  background: linear-gradient(135deg, #ff6b6b, #ff8e8e);
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(255, 107, 107, 0.2);
}

.hot-title {
  color: white;
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 1rem;
  text-align: center;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.hot-product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
}

.hot-product-card {
  background: white;
  border-radius: 8px;
  padding: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.hot-product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
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
  z-index: 1;
}

.hot-product-card .product-image {
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 0.75rem;
  border-radius: 6px;
  overflow: hidden;
  background: #f8f9fa;
}

.hot-product-card .product-image img {
  max-width: 100%;
  max-height: 100%;
  object-fit: cover;
}

.hot-product-card .product-info {
  text-align: center;
}

.hot-product-card .product-name {
  font-size: 0.9rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: #2c3e50;
  line-height: 1.3;
}

.hot-product-card .product-price {
  font-size: 1.1rem;
  font-weight: bold;
  color: #ff4757;
  margin-bottom: 0.25rem;
}

.hot-product-card .product-stock {
  font-size: 0.8rem;
  color: #7f8c8d;
}
</style>
