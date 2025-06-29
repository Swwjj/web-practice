<template>
  <div class="cart-container">
    <button class="back-btn" @click="goBack">← 返回</button>
    <h2>购物车</h2>
    <div class="cart-actions">
      <button class="clear-btn" @click="handleClearCart">清空购物车</button>
    </div>
    <div v-if="msg" :class="{'success-msg': success, 'error-msg': !success}">{{ msg }}</div>
    <div v-if="cartStore.cartList.length === 0" class="empty-cart">购物车为空</div>
    <table v-else class="cart-table">
      <thead>
        <tr>
          <th>商品</th>
          <th>单价</th>
          <th>数量</th>
          <th>小计</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in cartStore.cartList" :key="item.id">
          <td class="product-info">
            <img :src="item.iconUrl || '/default-product.png'" :alt="item.name" class="product-img" />
            <span>{{ item.name }}</span>
          </td>
          <td>¥{{ item.price }}</td>
          <td>{{ item.quantity }}</td>
          <td>¥{{ item.totalPrice }}</td>
          <td>
            <button class="delete-btn" @click="handleDelete(item.productId)">删除</button>
          </td>
        </tr>
      </tbody>
    </table>
    <div v-if="cartStore.cartList.length > 0" class="cart-summary">
      <span>总价：<b>¥{{ cartStore.totalPrice }}</b></span>
      <button class="checkout-btn" @click="goCheckout">去结算</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { useCartStore } from '@/stores/cart';
import { clearCart, deleteCartItem } from '@/api/cart';

const router = useRouter();
const userStore = useUserStore();
const cartStore = useCartStore();

const msg = ref('');
const success = ref(false);

const refreshCart = async () => {
  await cartStore.fetchCartList();
  await cartStore.fetchCartCount();
  
  // 调试信息
  console.log('购物车页面数据:', {
    cartList: cartStore.cartList,
    totalPrice: cartStore.totalPrice,
    cartCount: cartStore.cartCount
  });
};

onMounted(async () => {
  if (!userStore.isLoggedIn) {
    msg.value = '请先登录后再查看购物车！';
    success.value = false;
    setTimeout(() => router.push('/login'), 1500);
    return;
  }
  await refreshCart();
});

const handleClearCart = async () => {
  msg.value = '';
  success.value = false;
  if (!userStore.isLoggedIn) {
    msg.value = '请先登录后再操作购物车！';
    success.value = false;
    setTimeout(() => router.push('/login'), 1500);
    return;
  }
  try {
    const res = await clearCart();
    if (res.status === 0) {
      msg.value = res.msg || '购物车已清空！';
      success.value = true;
      await refreshCart();
    } else {
      msg.value = res.msg || '清空购物车失败！';
      success.value = false;
    }
  } catch (e) {
    msg.value = e.message || '清空购物车失败！';
    success.value = false;
  }
};

const handleDelete = async (productId) => {
  msg.value = '';
  success.value = false;
  if (!userStore.isLoggedIn) {
    msg.value = '请先登录后再操作购物车！';
    success.value = false;
    setTimeout(() => router.push('/login'), 1500);
    return;
  }
  try {
    const res = await deleteCartItem(productId);
    if (res.status === 0) {
      msg.value = '商品已删除';
      success.value = true;
      await refreshCart();
    } else {
      msg.value = res.msg || '删除失败！';
      success.value = false;
    }
  } catch (e) {
    msg.value = e.message || '删除失败！';
    success.value = false;
  }
};

const goBack = () => {
  if (window.history.length > 1) {
    router.back();
  } else {
    router.push('/');
  }
};

const goCheckout = () => {
  if (cartStore.cartList.length > 0) {
    router.push('/checkout');
  }
};
</script>

<style scoped>
.cart-container {
  max-width: 900px;
  margin: 40px auto;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  padding: 32px;
  position: relative;
  color: #222;
}
.cart-actions {
  margin-bottom: 20px;
}
.clear-btn {
  background: #f56c6c;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 10px 24px;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.2s;
  font-weight: 500;
}
.clear-btn:hover {
  background: #ff7875;
}
.clear-btn:disabled {
  background: #f8d7da;
  color: #b94a48;
}
.success-msg {
  color: #67c23a;
  background: #f0f9eb;
  border: 1px solid #c2e7b0;
  margin-top: 12px;
  text-align: center;
  padding: 8px 0;
  border-radius: 4px;
}
.error-msg {
  color: #f56c6c;
  background: #fef0f0;
  border: 1px solid #fbc4c4;
  margin-top: 12px;
  text-align: center;
  padding: 8px 0;
  border-radius: 4px;
}
.cart-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 24px;
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
.delete-btn {
  background: #f56c6c;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 6px 16px;
  font-size: 14px;
  cursor: pointer;
  transition: background 0.2s;
  font-weight: 500;
}
.delete-btn:hover {
  background: #ff7875;
}
.cart-summary {
  margin-top: 24px;
  text-align: right;
  font-size: 18px;
  color: #333;
}
.empty-cart {
  text-align: center;
  color: #666;
  margin: 40px 0;
  font-size: 18px;
  font-weight: 500;
}
.back-btn {
  position: absolute;
  left: 24px;
  top: 24px;
  background: none;
  border: none;
  color: #409EFF;
  font-size: 16px;
  cursor: pointer;
  padding: 0;
}
.back-btn:hover {
  text-decoration: underline;
}
.checkout-btn {
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
.checkout-btn:disabled {
  background: #dbeafe;
  color: #888;
  cursor: not-allowed;
}
</style> 