<template>
  <div class="checkout-container">
    <button class="back-btn" @click="goBack">← 返回购物车</button>
    <h2>订单结算</h2>
    
    <div v-if="msg" :class="{'success-msg': success, 'error-msg': !success}">{{ msg }}</div>
    
    <!-- 收货地址 -->
    <div class="address-section">
      <h3>收货地址</h3>
      <div v-if="addresses.length === 0" class="no-address">
        <p>暂无收货地址</p>
        <button class="add-address-btn" @click="addAddress">添加地址</button>
      </div>
      <div v-else class="address-list">
        <div 
          v-for="address in addresses" 
          :key="address.id" 
          :class="['address-item', { 'selected': selectedAddressId === address.id }]"
          @click="selectAddress(address.id)"
        >
          <div class="address-info">
            <div class="contact-info">
              <span class="name">{{ address.name }}</span>
              <span class="phone">{{ address.mobile }}</span>
            </div>
            <div class="address-detail">
              {{ address.province }}{{ address.city }}{{ address.district }}{{ address.addr }}
            </div>
            <div class="zip">邮编：{{ address.zip }}</div>
          </div>
          <div class="select-indicator" v-if="selectedAddressId === address.id">✓</div>
        </div>
      </div>
    </div>

    <!-- 商品清单 -->
    <div class="order-items-section">
      <h3>商品清单</h3>
      <div class="order-items">
        <div v-for="item in cartStore.cartList" :key="item.id" class="order-item">
          <img :src="item.iconUrl || '/default-product.png'" :alt="item.name" class="item-img" />
          <div class="item-info">
            <div class="item-name">{{ item.name }}</div>
            <div class="item-price">¥{{ item.price }} × {{ item.quantity }}</div>
          </div>
          <div class="item-total">¥{{ item.totalPrice }}</div>
        </div>
      </div>
    </div>

    <!-- 订单总计 -->
    <div class="order-summary">
      <div class="summary-item">
        <span>商品总价：</span>
        <span>¥{{ cartStore.totalPrice }}</span>
      </div>
      <div class="summary-item">
        <span>运费：</span>
        <span>¥0.00</span>
      </div>
      <div class="summary-item total">
        <span>实付金额：</span>
        <span>¥{{ cartStore.totalPrice }}</span>
      </div>
    </div>

    <!-- 提交订单 -->
    <div class="checkout-actions">
      <button 
        class="submit-order-btn" 
        @click="submitOrder" 
        :disabled="!selectedAddressId || loading"
      >
        {{ loading ? '创建订单中...' : '提交订单' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { useCartStore } from '@/stores/cart';
import { createOrder } from '@/api/order';
import { getAddressList } from '@/api/address';

const router = useRouter();
const userStore = useUserStore();
const cartStore = useCartStore();

const addresses = ref([]);
const selectedAddressId = ref(null);
const loading = ref(false);
const msg = ref('');
const success = ref(false);

// 获取用户地址列表
const fetchAddresses = async () => {
  try {
    const res = await getAddressList();
    if (res.status === 0) {
      addresses.value = res.data || [];
      // 默认选择第一个地址
      if (addresses.value.length > 0) {
        selectedAddressId.value = addresses.value[0].id;
      }
    }
  } catch (e) {
    console.error('获取地址列表失败:', e);
  }
};

// 选择地址
const selectAddress = (addressId) => {
  selectedAddressId.value = addressId;
};

// 添加地址
const addAddress = () => {
  // TODO: 跳转到添加地址页面
  alert('跳转到添加地址页面');
};

// 提交订单
const submitOrder = async () => {
  if (!selectedAddressId.value) {
    msg.value = '请选择收货地址';
    success.value = false;
    return;
  }

  if (cartStore.cartList.length === 0) {
    msg.value = '购物车为空，无法创建订单';
    success.value = false;
    return;
  }

  loading.value = true;
  msg.value = '';
  success.value = false;

  try {
    const res = await createOrder({ addrId: selectedAddressId.value });
    
    if (res.status === 0) {
      msg.value = '订单创建成功！';
      success.value = true;
      
      // 清空购物车状态
      await cartStore.fetchCartList();
      await cartStore.fetchCartCount();
      
      // 跳转到订单详情页
      setTimeout(() => {
        router.push(`/orders/${res.data.orderNo}`);
      }, 1500);
    } else {
      msg.value = res.msg || '创建订单失败';
      success.value = false;
    }
  } catch (e) {
    msg.value = e.message || '创建订单失败，请稍后重试';
    success.value = false;
  } finally {
    loading.value = false;
  }
};

// 返回购物车
const goBack = () => {
  router.push('/cart');
};

onMounted(async () => {
  if (!userStore.isLoggedIn) {
    router.push('/login');
    return;
  }
  
  await fetchAddresses();
  await cartStore.fetchCartList();
});
</script>

<style scoped>
.checkout-container {
  max-width: 800px;
  margin: 40px auto;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  padding: 32px;
  color: #222;
}

.back-btn {
  background: none;
  border: none;
  color: #409EFF;
  font-size: 16px;
  cursor: pointer;
  padding: 0;
  margin-bottom: 20px;
}

.back-btn:hover {
  text-decoration: underline;
}

.success-msg, .error-msg {
  padding: 12px;
  border-radius: 4px;
  margin-bottom: 20px;
  text-align: center;
}

.success-msg {
  background: #f0f9eb;
  color: #67c23a;
  border: 1px solid #c2e7b0;
}

.error-msg {
  background: #fef0f0;
  color: #f56c6c;
  border: 1px solid #fbc4c4;
}

.address-section, .order-items-section {
  margin-bottom: 30px;
}

.address-section h3, .order-items-section h3 {
  margin-bottom: 16px;
  color: #333;
  font-size: 18px;
}

.no-address {
  text-align: center;
  padding: 40px;
  color: #666;
}

.add-address-btn {
  background: #409EFF;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  cursor: pointer;
  margin-top: 10px;
}

.address-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.address-item {
  border: 2px solid #eee;
  border-radius: 6px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
}

.address-item:hover {
  border-color: #409EFF;
}

.address-item.selected {
  border-color: #409EFF;
  background: #f0f9ff;
}

.contact-info {
  margin-bottom: 8px;
}

.name {
  font-weight: 500;
  margin-right: 12px;
}

.phone {
  color: #666;
}

.address-detail {
  margin-bottom: 4px;
  color: #333;
}

.zip {
  color: #999;
  font-size: 14px;
}

.select-indicator {
  position: absolute;
  top: 16px;
  right: 16px;
  color: #409EFF;
  font-weight: bold;
}

.order-items {
  border: 1px solid #eee;
  border-radius: 6px;
}

.order-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #eee;
}

.order-item:last-child {
  border-bottom: none;
}

.item-img {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 6px;
  margin-right: 16px;
}

.item-info {
  flex: 1;
}

.item-name {
  font-weight: 500;
  margin-bottom: 4px;
}

.item-price {
  color: #666;
  font-size: 14px;
}

.item-total {
  font-weight: 500;
  color: #e74c3c;
}

.order-summary {
  background: #f8f9fa;
  border-radius: 6px;
  padding: 20px;
  margin-bottom: 30px;
}

.summary-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
}

.summary-item:last-child {
  margin-bottom: 0;
}

.summary-item.total {
  border-top: 1px solid #dee2e6;
  padding-top: 12px;
  font-weight: 500;
  font-size: 18px;
  color: #e74c3c;
}

.checkout-actions {
  text-align: center;
}

.submit-order-btn {
  background: #409EFF;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 12px 40px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.3s ease;
}

.submit-order-btn:hover:not(:disabled) {
  background: #337ecc;
}

.submit-order-btn:disabled {
  background: #dbeafe;
  color: #888;
  cursor: not-allowed;
}
</style> 