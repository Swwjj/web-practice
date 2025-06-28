// mock订单池，支持多种状态
const mockOrders = {
  '1001': {
    orderNo: '1001',
    amount: 316,
    type: 1,
    typeDesc: '在线支付',
    freight: 0,
    status: 1,
    statusDesc: '未付款',
    paymentTime: '',
    deliveryTime: '',
    finishTime: '',
    closeTime: '',
    created: '2023-06-01 10:00:00',
    orderItems: [
      {
        orderNo: '1001',
        goodsId: 9,
        goodsName: '润滑油',
        iconUrl: '/upload/47fb50a7-c9d2-4638-971e-adaa1ddccef2.png',
        curPrice: 79,
        quantity: 3,
        totalPrice: 237,
        created: null
      }
    ],
    addrId: 21,
    deliveryName: '张三',
    address: {
      name: '张三',
      phone: null,
      mobile: '12345678901',
      province: '山东省',
      city: '青岛市',
      district: '市南区',
      addr: '香港中路1号',
      zip: '266000'
    }
  },
  '1002': {
    orderNo: '1002',
    amount: 863.56,
    type: 1,
    typeDesc: '在线支付',
    freight: 0,
    status: 2,
    statusDesc: '已付款',
    paymentTime: '2023-06-02 09:00:00',
    deliveryTime: '',
    finishTime: '',
    closeTime: '',
    created: '2023-06-02 08:50:00',
    orderItems: [
      {
        orderNo: '1002',
        goodsId: 7,
        goodsName: '锂基脂 00#',
        iconUrl: '/upload/aa2e590f-c4eb-42e0-8260-54af15ed4760.jpg',
        curPrice: 215.89,
        quantity: 2,
        totalPrice: 431.78,
        created: null
      }
    ],
    addrId: 22,
    deliveryName: '李四',
    address: {
      name: '李四',
      phone: null,
      mobile: '12345678902',
      province: '江苏省',
      city: '南京市',
      district: '鼓楼区',
      addr: '中山路2号',
      zip: '210000'
    }
  },
  '1003': {
    orderNo: '1003',
    amount: 242,
    type: 1,
    typeDesc: '在线支付',
    freight: 0,
    status: 6,
    statusDesc: '订单取消',
    paymentTime: '',
    deliveryTime: '',
    finishTime: '',
    closeTime: '',
    created: '2023-06-03 16:31:09',
    orderItems: [
      {
        orderNo: '1003',
        goodsId: 8,
        goodsName: 'aaaaa',
        iconUrl: '',
        curPrice: 11,
        quantity: 21,
        totalPrice: 231,
        created: null
      },
      {
        orderNo: '1003',
        goodsId: 29,
        goodsName: '压路机',
        iconUrl: '',
        curPrice: 11,
        quantity: 1,
        totalPrice: 11,
        created: null
      }
    ],
    addrId: 26,
    deliveryName: '阿萨德',
    address: {
      name: '阿萨德',
      phone: null,
      mobile: '12345678912',
      province: '江苏省',
      city: '常州市',
      district: '钟楼区',
      addr: '燃烧',
      zip: '261300'
    }
  },
  '1004': {
    orderNo: '1004',
    amount: 888,
    type: 1,
    typeDesc: '在线支付',
    freight: 0,
    status: 3, // 已发货
    statusDesc: '已发货',
    paymentTime: '2023-06-10 10:00:00',
    deliveryTime: '2023-06-11 12:00:00',
    finishTime: '',
    closeTime: '',
    created: '2023-06-10 09:00:00',
    orderItems: [
      {
        orderNo: '1004',
        goodsId: 12,
        goodsName: '测试商品-已发货',
        iconUrl: '',
        curPrice: 222,
        quantity: 4,
        totalPrice: 888,
        created: null
      }
    ],
    addrId: 24,
    deliveryName: '测试收货人',
    address: {
      name: '测试收货人',
      phone: null,
      mobile: '12345678988',
      province: '测试省',
      city: '测试市',
      district: '测试区',
      addr: '测试地址',
      zip: '000000'
    }
  }
};

// 获取订单详情
export function getOrderDetail(orderNo) {
  const order = mockOrders[orderNo];
  if (order) {
    return Promise.resolve({ status: 0, data: order });
  } else {
    return Promise.resolve({ status: 1, msg: '订单不存在！' });
  }
}

// 取消订单
export function cancelOrder(orderNo) {
  const order = mockOrders[orderNo];
  if (!order) {
    return Promise.resolve({ status: 1, msg: '订单不存在！' });
  }
  if (order.status !== 1) {
    return Promise.resolve({ status: 1, msg: '当前订单不可取消！' });
  }
  order.status = 6;
  order.statusDesc = '订单取消';
  return Promise.resolve({ status: 0, msg: '该订单已经取消！' });
}

// 获取订单列表
export function getOrderList({ pageNo = 1, pageSize = 10, status = '' } = {}) {
  const allOrders = Object.values(mockOrders);
  const filtered = status ? allOrders.filter(o => String(o.status) === String(status)) : allOrders;
  const start = (pageNo - 1) * pageSize;
  const end = start + Number(pageSize);
  const pageData = filtered.slice(start, end);
  return Promise.resolve({
    status: 0,
    data: {
      pageNum: Number(pageNo),
      pageSize: Number(pageSize),
      totalRecord: filtered.length,
      totalPage: Math.ceil(filtered.length / pageSize),
      startIndex: start,
      data: pageData,
      prePage: Math.max(1, Number(pageNo) - 1),
      nextPage: Math.min(Math.ceil(filtered.length / pageSize), Number(pageNo) + 1)
    }
  });
}

// 创建订单（仍用原有mock）
export function createOrder({ addrId }) {
  // 生成新订单号
  const orderNo = String(Date.now());
  const newOrder = {
    orderNo,
    amount: 500,
    type: 1,
    typeDesc: '在线支付',
    freight: 0,
    status: 1,
    statusDesc: '未付款',
    paymentTime: '',
    deliveryTime: '',
    finishTime: '',
    closeTime: '',
    created: new Date().toLocaleString(),
    orderItems: [
      {
        orderNo,
        goodsId: 99,
        goodsName: '测试商品',
        iconUrl: '',
        curPrice: 100,
        quantity: 5,
        totalPrice: 500,
        created: null
      }
    ],
    addrId,
    deliveryName: '测试收货人',
    address: {
      name: '测试收货人',
      phone: null,
      mobile: '12345678999',
      province: '测试省',
      city: '测试市',
      district: '测试区',
      addr: '测试地址',
      zip: '000000'
    }
  };
  mockOrders[orderNo] = newOrder;
  return Promise.resolve({ status: 0, data: newOrder });
}

// 确认收货
export function confirmReceipt(orderNo) {
  const order = mockOrders[orderNo];
  if (!order) {
    return Promise.resolve({ status: 1, msg: '订单不存在！' });
  }
  if (order.status !== 3) {
    return Promise.resolve({ status: 1, msg: '当前订单不可确认收货！' });
  }
  order.status = 4;
  order.statusDesc = '已完成';
  return Promise.resolve({ status: 0, msg: '订单已确认收货！' });
}

// 真实请求（联调时启用）
// import request from './request';
// export function createOrder({ addrId }) {
//   return request({
//     url: '/order/createorder.do',
//     method: 'post',
//     data: { addrId }
//   });
// }
// export function getOrderList({ pageNo, pageSize, status }) {
//   return request({
//     url: '/order/getlist.do',
//     method: 'get',
//     params: { pageNo, pageSize, status }
//   });
// }
// export function getOrderDetail(orderNo) {
//   return request({
//     url: '/order/getdetail.do',
//     method: 'get',
//     params: { orderNo }
//   });
// }
// export function cancelOrder(orderNo) {
//   return request({
//     url: '/order/cancelorder.do',
//     method: 'post',
//     data: { orderNo }
//   });
// }
// export function confirmReceipt(orderNo) {
//   return request({
//     url: '/order/confirmreceipt.do',
//     method: 'post',
//     data: { orderNo }
//   });
// } 