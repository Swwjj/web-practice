import request from './request';

// 获取用户地址列表
export function getAddressList() {
  return request({
    url: '/addr/findaddr.do',
    method: 'get'
  });
}

// 添加地址
export function addAddress(addressData) {
  return request({
    url: '/addr/saveaddr.do',
    method: 'post',
    data: addressData
  });
}

// 更新地址
export function updateAddress(addressData) {
  return request({
    url: '/addr/updateaddr.do',
    method: 'post',
    data: addressData
  });
}

// 删除地址
export function deleteAddress(addressId) {
  return request({
    url: '/addr/deladdr.do',
    method: 'get',
    params: { id: addressId }
  });
}

// 设置默认地址
export function setDefaultAddress(addressId) {
  return request({
    url: '/addr/setdefaultaddr.do',
    method: 'get',
    params: { id: addressId }
  });
}

// 根据ID获取地址详情
export function getAddressById(addressId) {
  return request({
    url: '/addr/findAddressById.do',
    method: 'post',
    data: { id: addressId }
  });
} 