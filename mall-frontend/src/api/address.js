/**
 * [Mock] 获取收货人地址列表（前端开发阶段本地测试用，联调时请注释本函数，启用下方真实请求）
 */
export function getAddressList() {
  return Promise.resolve({
    status: 0,
    data: mockAddressList.slice()
  });
}

/**
 * [Mock] 新增收货人地址（前端开发阶段本地测试用，联调时请注释本函数，启用下方真实请求）
 */
export function addAddress(data) {
  const newAddr = {
    id: ++mockIdSeed,
    uid: 1,
    name: data.name,
    phone: null,
    mobile: data.mobile,
    province: data.province,
    city: data.city,
    district: data.district,
    addr: data.addr,
    zip: data.zip,
    default_addr: 0,
    del_state: 0,
    created: Date.now(),
    updated: Date.now()
  };
  mockAddressList.push(newAddr);
  return Promise.resolve({
    status: 0,
    data: mockAddressList.slice()
  });
}

/**
 * [Mock] 获取单个地址详情（前端开发阶段本地测试用，联调时请注释本函数，启用下方真实请求）
 */
export function getAddressById(id) {
  const addr = mockAddressList.find(a => String(a.id) === String(id));
  if (addr) {
    return Promise.resolve({ status: 0, data: { ...addr } });
  } else {
    return Promise.resolve({ status: 1, msg: '参数错误！' });
  }
}

/**
 * [Mock] 更新收货人地址（前端开发阶段本地测试用，联调时请注释本函数，启用下方真实请求）
 */
export function updateAddress(data) {
  const idx = mockAddressList.findIndex(a => String(a.id) === String(data.id));
  if (idx !== -1) {
    mockAddressList[idx] = {
      ...mockAddressList[idx],
      ...data,
      updated: Date.now()
    };
    return Promise.resolve({ status: 0, data: { ...mockAddressList[idx] } });
  } else {
    return Promise.resolve({ status: 1, msg: '参数错误！' });
  }
}

/**
 * [Mock] 删除收货人地址（前端开发阶段本地测试用，联调时请注释本函数，启用下方真实请求）
 */
export function deleteAddress(id) {
  mockAddressList = mockAddressList.filter(a => String(a.id) !== String(id));
  return Promise.resolve({
    status: 0,
    data: mockAddressList.slice()
  });
}

/**
 * [Mock] 设置默认收货地址（前端开发阶段本地测试用，联调时请注释本函数，启用下方真实请求）
 */
export function setDefaultAddress(id) {
  let found = false;
  mockAddressList = mockAddressList.map(a => {
    if (String(a.id) === String(id)) {
      found = true;
      return { ...a, default_addr: 1, updated: Date.now() };
    } else {
      return { ...a, default_addr: 0 };
    }
  });
  if (found) {
    return Promise.resolve({
      status: 0,
      data: mockAddressList.slice()
    });
  } else {
    return Promise.resolve({ status: 1, msg: '默认地址设置失败！' });
  }
}

// ====== MOCK数据内存存储 ======
let mockAddressList = [
  {
    id: 4,
    uid: 3,
    name: '李四',
    phone: null,
    mobile: '123456789',
    province: '山东省',
    city: '烟台市',
    district: '芝罘区',
    addr: '魁玉路100号',
    zip: '264000',
    default_addr: 0,
    del_state: 0,
    created: 1519038748000,
    updated: 1519039374000
  },
  {
    id: 5,
    uid: 3,
    name: '张三',
    phone: null,
    mobile: '123456789',
    province: '山东省',
    city: '烟台市',
    district: '芝罘区',
    addr: '魁玉路100号',
    zip: '264000',
    default_addr: 0,
    del_state: 0,
    created: 1519038756000,
    updated: 1519038756000
  }
];
let mockIdSeed = 100;

// import request from './request';

// /**
// * [真实请求] 获取收货人地址列表（联调时请启用，开发阶段可注释）
// */
// export function getAddressList() {
//   return request({
//     url: '/addr/findaddrs.do',
//     method: 'get'
//   });
// }

// /**
// * [真实请求] 新增收货人地址（联调时请启用，开发阶段可注释）
// */
// export function addAddress(data) {
//   return request({
//     url: '/addr/saveaddr.do',
//     method: 'post',
//     data
//   });
// }

// /**
// * [真实请求] 获取单个地址详情（联调时请启用，开发阶段可注释）
// */
// export function getAddressById(id) {
//   return request({
//     url: '/addr/findAddressById.do',
//     method: 'post',
//     data: { id }
//   });
// }

// /**
// * [真实请求] 更新收货人地址（联调时请启用，开发阶段可注释）
// */
// export function updateAddress(data) {
//   return request({
//     url: '/addr/saveaddr.do', // 假设使用相同的接口
//     method: 'post',
//     data
//   });
// }

// /**
// * [真实请求] 删除收货人地址（联调时请启用，开发阶段可注释）
// */
// export function deleteAddress(id) {
//   return request({
//     url: '/addr/deladdr.do',
//     method: 'get',
//     params: { id }
//   });
// }

// /**
// * [真实请求] 设置默认收货地址（联调时请启用，开发阶段可注释）
// */
// export function setDefaultAddress(id) {
//   return request({
//     url: '/addr/setdefault.do',
//     method: 'get',
//     params: { id }
//   });
// }