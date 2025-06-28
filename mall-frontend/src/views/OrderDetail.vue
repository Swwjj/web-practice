<template>
  <div class="order-detail-container" v-if="order">
    <button class="back-btn" @click="goBack">← 返回</button>
    <h2>订单详情</h2>
    <div class="order-info">
      <div><b>订单号：</b>{{ order.orderNo }}</div>
      <div><b>状态：</b>{{ order.statusDesc }}</div>
      <div><b>总金额：</b>¥{{ order.amount }}</div>
      <div><b>下单时间：</b>{{ order.created }}</div>
      <div><b>支付方式：</b>{{ order.typeDesc }}</div>
      <div><b>运费：</b>¥{{ order.freight }}</div>
    </div>
    <div class="address-section" v-if="order.address">
      <h3>收货地址</h3>
      <div>{{ order.address.name }}，{{ order.address.mobile }}</div>
      <div>{{ order.address.province }}{{ order.address.city }}{{ order.address.district }}{{ order.address.addr }}</div>
      <div>邮编：{{ order.address.zip }}</div>
    </div>
    <div class="order-items-section">
      <h3>商品明细</h3>
      <table class="order-items-table">
        <thead>
          <tr>
            <th>商品</th>
            <th>单价</th>
            <th>数量</th>
            <th>小计</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in order.orderItems" :key="item.goodsId">
            <td class="product-info">
              <img :src="item.iconUrl || '/default-product.png'" :alt="item.goodsName" class="product-img" />
              <span>{{ item.goodsName }}</span>
            </td>
            <td>¥{{ item.curPrice }}</td>
            <td>{{ item.quantity }}</td>
            <td>¥{{ item.totalPrice }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="order-actions">
      <button v-if="showCancelBtn" class="cancel-btn" @click="handleCancel" :disabled="loading">取消订单</button>
      <button v-if="showConfirmBtn" class="confirm-btn" @click="handleConfirm" :disabled="loading">确认收货</button>
    </div>
    <div v-if="msg" :class="{'success-msg': success, 'error-msg': !success}" class="msg">{{ msg }}</div>
  </div>
  <div v-else class="error-msg">{{ errorMsg }}</div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getOrderDetail, cancelOrder, confirmReceipt } from '@/api/order';

const route = useRoute();
const router = useRouter();
const order = ref(null);
const errorMsg = ref('');
const msg = ref('');
const success = ref(false);
const loading = ref(false);

const fetchDetail = async () => {
  const orderNo = route.params.orderNo;
  if (!orderNo) {
    errorMsg.value = '订单号不能为空';
    return;
  }
  try {
    const res = await getOrderDetail(orderNo);
    if (res.status === 0) {
      order.value = res.data;
    } else {
      errorMsg.value = res.msg || '订单不存在或已被删除';
    }
  } catch (e) {
    errorMsg.value = e.message || '获取订单详情失败';
  }
};

const showCancelBtn = computed(() => order.value && order.value.status === 1); // 1=未付款
const showConfirmBtn = computed(() => order.value && order.value.status === 3); // 3=已发货

const handleCancel = async () => {
  if (!order.value) return;
  loading.value = true;
  msg.value = '';
  success.value = false;
  try {
    const res = await cancelOrder(order.value.orderNo);
    if (res.status === 0) {
      msg.value = res.msg || '订单已取消';
      success.value = true;
      await fetchDetail();
    } else {
      msg.value = res.msg || '取消失败！';
      success.value = false;
    }
  } catch (e) {
    msg.value = e.message || '取消失败！';
    success.value = false;
  } finally {
    loading.value = false;
  }
};

const handleConfirm = async () => {
  if (!order.value) return;
  loading.value = true;
  msg.value = '';
  success.value = false;
  try {
    const res = await confirmReceipt(order.value.orderNo);
    if (res.status === 0) {
      msg.value = res.msg || '已确认收货';
      success.value = true;
      await fetchDetail();
    } else {
      msg.value = res.msg || '确认收货失败！';
      success.value = false;
    }
  } catch (e) {
    msg.value = e.message || '确认收货失败！';
    success.value = false;
  } finally {
    loading.value = false;
  }
};

const goBack = () => {
  router.push('/orders');
};

onMounted(() => {
  fetchDetail();
});
</script>

<style scoped>
.order-detail-container {
  max-width: 900px;
  margin: 40px auto;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  padding: 32px;
  color: #222;
}
.order-info {
  margin-bottom: 24px;
  font-size: 16px;
  line-height: 2;
}
.address-section {
  margin-bottom: 24px;
  background: #fafbfc;
  border-radius: 6px;
  padding: 16px 20px;
}
.order-items-section {
  margin-bottom: 24px;
}
.order-items-table {
  width: 100%;
  border-collapse: collapse;
  background: #fafbfc;
}
.order-items-table th, .order-items-table td {
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
.order-actions {
  margin: 24px 0 0 0;
  text-align: right;
}
.cancel-btn {
  background: #f56c6c;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 10px 32px;
  font-size: 16px;
  cursor: pointer;
  font-weight: 500;
  transition: background 0.2s;
}
.cancel-btn:disabled {
  background: #f8d7da;
  color: #b94a48;
  cursor: not-allowed;
}
.confirm-btn {
  background: #67c23a;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 10px 32px;
  font-size: 16px;
  cursor: pointer;
  font-weight: 500;
  margin-left: 16px;
  transition: background 0.2s;
}
.confirm-btn:disabled {
  background: #e1f3d8;
  color: #b2cfb4;
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
</style> 