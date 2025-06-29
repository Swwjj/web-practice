// // Mock 购物车数据存储（内存中）
// let mockCartData = {
//   lists: [
//     {
//       id: 16,
//       userId: 1,
//       productId: 29,
//       name: '压路机',
//       quantity: 1,
//       price: 11,
//       status: 2,
//       totalPrice: 11,
//       stock: 100,
//       iconUrl: '',
//       checked: 0
//     },
//     {
//       id: 17,
//       userId: 1,
//       productId: 6,
//       name: '锂基脂 00#',
//       quantity: 2,
//       price: 215.89,
//       status: 2,
//       totalPrice: 431.78,
//       stock: 89,
//       iconUrl: '/upload/10a42221-06a8-4001-8cc6-b2deb3b9e964.png',
//       checked: 0
//     }
//   ],
//   totalPrice: 442.78
// };
//
// // 计算购物车商品总数
// const calculateCartCount = () => {
//   return mockCartData.lists.reduce((total, item) => total + item.quantity, 0);
// };
//
// // 计算购物车总价
// const calculateTotalPrice = () => {
//   return mockCartData.lists.reduce((total, item) => total + item.totalPrice, 0);
// };
//
// // [Mock] 获取购物车中商品数量
// export function getCartCount() {
//   return Promise.resolve({
//     status: 0,
//     data: calculateCartCount()
//   });
// }
//
// /**
//  * 更新购物车中某商品数量
//  */
// export function updateCartItem({ productId, count, checked }) {
//   const item = mockCartData.lists.find(item => item.productId === productId);
//   if (item) {
//     item.quantity = count;
//     item.totalPrice = item.price * count;
//     item.checked = checked;
//     mockCartData.totalPrice = calculateTotalPrice();
//   }
//
//   return Promise.resolve({
//     status: 0,
//     data: {
//       lists: mockCartData.lists,
//       totalPrice: mockCartData.totalPrice
//     }
//   });
// }
//
// // [Mock] 清空购物车
// export function clearCart() {
//   mockCartData.lists = [];
//   mockCartData.totalPrice = 0;
//
//   return Promise.resolve({
//     status: 0,
//     msg: '成功清空购物车！'
//   });
// }
//
// // [Mock] 删除购物车中某件商品
// export function deleteCartItem(productId) {
//   mockCartData.lists = mockCartData.lists.filter(item => item.productId !== productId);
//   mockCartData.totalPrice = calculateTotalPrice();
//
//   return Promise.resolve({
//     status: 0,
//     data: {
//       lists: mockCartData.lists,
//       totalPrice: mockCartData.totalPrice
//     }
//   });
// }
//
// // [Mock] 获取购物车商品列表
// export function getCartList() {
//   // 确保总价是最新的
//   mockCartData.totalPrice = calculateTotalPrice();
//
//   // 调试信息
//   console.log('购物车数据:', {
//     lists: mockCartData.lists,
//     totalPrice: mockCartData.totalPrice,
//     calculatedTotal: calculateTotalPrice()
//   });
//
//   return Promise.resolve({
//     status: 0,
//     data: {
//       lists: mockCartData.lists,
//       totalPrice: mockCartData.totalPrice
//     }
//   });
// }
//
// // [Mock] 新增商品到购物车
// export function addCartItem({ productId, count, name, price, iconUrl }) {
//   const existingItem = mockCartData.lists.find(item => item.productId === productId);
//
//   if (existingItem) {
//     existingItem.quantity += count;
//     existingItem.totalPrice = existingItem.price * existingItem.quantity;
//   } else {
//     const newItem = {
//       id: Date.now(),
//       userId: 1,
//       productId,
//       name: name || `商品${productId}`,
//       quantity: count,
//       price: price || 100,
//       status: 2,
//       totalPrice: (price || 100) * count,
//       stock: 100,
//       iconUrl: iconUrl || '',
//       checked: 0
//     };
//     mockCartData.lists.push(newItem);
//   }
//   mockCartData.totalPrice = calculateTotalPrice();
//
//   return Promise.resolve({
//     status: 0,
//     msg: '商品已成功加入购物车！'
//   });
// }

// // [真实请求] 购物车相关接口（联调时请启用，开发阶段可注释）
import request from './request';

export function getCartCount() {
  return request({
    url: '/cart/getcartcount.do',
    method: 'get'
  });
}

export function updateCartItem({ productId, count, checked }) {
  return request({
    url: '/cart/updatecarts.do',
    method: 'get',
    params: { productId, count, checked }
  });
}

export function clearCart() {
  return request({
    url: '/cart/clearcarts.do',
    method: 'get'
  });
}

export function deleteCartItem(productId) {
  return request({
    url: '/cart/delcarts.do',
    method: 'post',
    params: { productId }
  });
}

export function getCartList() {
  return request({
    url: '/cart/findallcarts.do',
    method: 'get'
  });
}

export function addCartItem({ productId, count }) {
  return request({
    url: '/cart/savecart.do',
    method: 'post',
    params: { productId, count }
  });
}