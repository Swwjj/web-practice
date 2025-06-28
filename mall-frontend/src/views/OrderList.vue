<template>
  <div class="order-list-container">
    <button class="back-btn" @click="goBack">← 返回</button>
    <h2>我的订单</h2>
    <div class="filter-section">
      <label>订单状态：</label>
      <select v-model="status" @change="fetchOrders">
        <option value="">全部</option>
        <option value="1">未付款</option>
        <option value="2">已付款</option>
        <option value="3">已发货</option>
        <option value="4">已完成</option>
        <option value="5">已关闭</option>
      </select>
    </div>
    <div v-if="orders.length === 0" class="empty-order">暂无订单</div>
    <div v-else>
      <div v-for="order in orders" :key="order.orderNo" class="order-card">
        <div class="order-header">
          <span>订单号：{{ order.orderNo }}</span>
          <span class="order-status">{{ order.statusDesc }}</span>
          <span class="order-amount">总金额：¥{{ order.amount }}</span>
          <button class="detail-btn" @click="goDetail(order.orderNo)">查看详情</button>
        </div>
        <div class="order-items">
          <div v-for="item in order.orderItems" :key="item.goodsId" class="order-item">
            <img :src="item.iconUrl || '/default-product.png'" :alt="item.goodsName" class="item-img" />
            <div class="item-info">
              <div class="item-name">{{ item.goodsName }}</div>
              <div class="item-qty">数量：{{ item.quantity }}</div>
              <div class="item-price">单价：¥{{ item.curPrice }}</div>
              <div class="item-total">小计：¥{{ item.totalPrice }}</div>
            </div>
          </div>
        </div>
        <div class="order-footer">
          <span>下单时间：{{ order.created }}</span>
        </div>
      </div>
      <div class="pagination">
        <button class="page-btn" :disabled="pageNum === 1" @click="changePage(pageNum - 1)">上一页</button>
        <span>第 {{ pageNum }} / {{ totalPage }} 页</span>
        <button class="page-btn" :disabled="pageNum === totalPage" @click="changePage(pageNum + 1)">下一页</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getOrderList } from '@/api/order';

const router = useRouter();
const orders = ref([]);
const pageNum = ref(1);
const pageSize = ref(10);
const totalPage = ref(1);
const status = ref('');

const fetchOrders = async () => {
  const res = await getOrderList({ pageNo: pageNum.value, pageSize: pageSize.value, status: status.value });
  if (res.status === 0) {
    orders.value = res.data.data;
    pageNum.value = res.data.pageNum;
    pageSize.value = res.data.pageSize;
    totalPage.value = res.data.totalPage;
  } else {
    orders.value = [];
    totalPage.value = 1;
  }
};

const changePage = (page) => {
  if (page >= 1 && page <= totalPage.value) {
    pageNum.value = page;
    fetchOrders();
  }
};

const goDetail = (orderNo) => {
  router.push(`/orders/${orderNo}`);
};

const goBack = () => {
  router.push('/');
};

onMounted(() => {
  fetchOrders();
});
</script>

<style scoped>
.order-list-container {
  max-width: 900px;
  margin: 40px auto;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  padding: 32px;
  color: #222;
}
.filter-section {
  margin-bottom: 24px;
}
.order-card {
  border: 1px solid #eee;
  border-radius: 6px;
  margin-bottom: 24px;
  padding: 18px 20px;
  background: #fafbfc;
}
.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  font-size: 15px;
}
.order-status {
  color: #409EFF;
  font-weight: 500;
}
.order-amount {
  color: #e74c3c;
  font-weight: 500;
}
.detail-btn {
  background: #409EFF;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 6px 18px;
  font-size: 14px;
  cursor: pointer;
  font-weight: 500;
  transition: background 0.2s;
}
.detail-btn:hover {
  background: #66b1ff;
}
.order-items {
  display: flex;
  gap: 24px;
  margin-bottom: 10px;
}
.order-item {
  display: flex;
  gap: 10px;
  align-items: center;
  background: #fff;
  border-radius: 4px;
  padding: 8px 12px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.03);
}
.item-img {
  width: 48px;
  height: 48px;
  object-fit: cover;
  border-radius: 6px;
  background: #f5f5f5;
}
.item-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}
.item-name {
  font-weight: 500;
  color: #333;
}
.item-qty, .item-price, .item-total {
  font-size: 13px;
  color: #666;
}
.order-footer {
  text-align: right;
  color: #888;
  font-size: 13px;
}
.empty-order {
  text-align: center;
  color: #888;
  margin: 40px 0;
  font-size: 18px;
  font-weight: 500;
}
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  margin-top: 24px;
}
.page-btn {
  padding: 0.5rem 1.2rem;
  border: 1px solid #dee2e6;
  background: white;
  color: #409EFF;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 15px;
}
.page-btn:disabled {
  background: #f8f9fa;
  color: #aaa;
  cursor: not-allowed;
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