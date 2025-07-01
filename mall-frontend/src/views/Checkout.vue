<template>
  <div class="checkout-container">
    <h2>订单结算</h2>
    <div class="address-section">
      <h3>收货地址</h3>
      <div v-if="addressLoading">地址加载中...</div>
      <div v-else-if="addressError" class="empty-address">{{ addressError }}</div>
      <div v-else-if="address">
        <div>{{ address.name }}，{{ address.mobile }}</div>
        <div>{{ address.province }}{{ address.city }}{{ address.district }}{{ address.addr }}</div>
      </div>
      <div v-else class="empty-address">暂无收货地址</div>
    </div>
    <div class="cart-section">
      <h3>商品列表</h3>
      <table class="cart-table">
        <thead>
          <tr>
            <th>商品</th>
            <th>单价</th>
            <th>数量</th>
            <th>小计</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in cartList" :key="item.productId">
            <td class="product-info">
              <img :src="item.iconUrl || '/default-product.png'" :alt="item.name" class="product-img" />
              <span>{{ item.name }}</span>
            </td>
            <td>¥{{ item.price }}</td>
            <td>{{ item.quantity }}</td>
            <td>¥{{ item.price * item.quantity }}</td>
          </tr>
        </tbody>
      </table>
      <div v-if="cartList.length === 0" class="empty-cart">购物车为空</div>
    </div>
    <div class="summary-section">
      <span>总价：<b>¥{{ totalPrice }}</b></span>
      <button class="submit-btn" :disabled="!address || cartList.length === 0 || loading" @click="handleSubmit">提交订单</button>
    </div>
    <div v-if="msg" :class="{'success-msg': success, 'error-msg': !success}" class="msg">{{ msg }}</div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { createOrder } from '@/api/order';
import { getAddressList } from '@/api/address';
import { useCartStore } from '@/stores/cart';

const router = useRouter();
const cartStore = useCartStore();

const address = ref(null);
const addressLoading = ref(true);
const addressError = ref('');

onMounted(async () => {
  addressLoading.value = true;
  addressError.value = '';
  try {
    const res = await getAddressList();
    if (res.status === 0 && Array.isArray(res.data)) {
      // 先找默认地址，没有则取第一个
      address.value = res.data.find(a => a.default_addr === 1) || res.data[0] || null;
    } else {
      address.value = null;
      addressError.value = res.msg || '地址获取失败';
    }
  } catch (e) {
    address.value = null;
    addressError.value = e.message || '地址获取失败';
  } finally {
    addressLoading.value = false;
  }
});

const cartList = computed(() => cartStore.cartList);
const totalPrice = computed(() => cartStore.totalPrice);
const msg = ref('');
const success = ref(false);
const loading = ref(false);

const handleSubmit = async () => {
  if (!address.value || cartList.value.length === 0) return;
  loading.value = true;
  msg.value = '';
  success.value = false;
  try {
    const items = cartList.value.map(item => ({
      productId: item.productId,
      quantity: item.quantity,
      price: item.price
    }));
    const res = await createOrder({ addrId: address.value.id, items });
    if (res.status === 0) {
      msg.value = '订单创建成功，正在跳转...';
      success.value = true;
      setTimeout(() => {
        router.push(`/orders/${res.data.orderNo}`);
      }, 1200);
    } else {
      msg.value = res.msg || '订单创建失败！';
      success.value = false;
    }
  } catch (e) {
    msg.value = e.message || '订单创建失败！';
    success.value = false;
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.checkout-container {
  max-width: 900px;
  margin: 40px auto;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  padding: 32px;
  color: #222;
}
.address-section, .cart-section {
  margin-bottom: 24px;
}
.cart-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 12px;
  background: #fafbfc;
}
.cart-table th, .cart-table td {
  padding: 12px 8px;
  border-bottom: 1px solid #eee;
  text-align: left;
}
.product-info {
  display: flex;
  align-items: center;
  gap: 10px;
}
.product-img {
  width: 48px;
  height: 48px;
  object-fit: cover;
  border-radius: 6px;
  background: #f5f5f5;
}
.empty-cart, .empty-address {
  text-align: center;
  color: #888;
  margin: 24px 0;
  font-size: 16px;
}
.summary-section {
  margin-top: 32px;
  text-align: right;
  font-size: 18px;
  color: #333;
}
.submit-btn {
  background: #409EFF;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 10px 32px;
  font-size: 16px;
  cursor: pointer;
  margin-left: 32px;
  font-weight: 500;
  transition: background 0.2s;
}
.submit-btn:disabled {
  background: #dbeafe;
  color: #888;
  cursor: not-allowed;
}
.success-msg {
  color: #67c23a;
  background: #f0f9eb;
  border: 1px solid #c2e7b0;
  margin-top: 18px;
  text-align: center;
  padding: 8px 0;
  border-radius: 4px;
}
.error-msg {
  color: #f56c6c;
  background: #fef0f0;
  border: 1px solid #fbc4c4;
  margin-top: 18px;
  text-align: center;
  padding: 8px 0;
  border-radius: 4px;
}
</style> 