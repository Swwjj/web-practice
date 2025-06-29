/**
 * [Mock] 用户登录（前端开发阶段本地测试用，联调时请注释本函数，启用下方真实请求）
 */
export function login(data) {
  return Promise.resolve({
    status: 0,
    data: {
      id: 1,
      account: data.account,
      name: '测试用户',
      email: 'test@example.com',
      phone: '123456789',
      age: 20,
      sex: 1,
      role: 0,
      create_time: Date.now(),
      update_time: Date.now(),
    }
  });
}


/**
 * [Mock] 用户注册
 */
export function register(data) {
  return Promise.resolve({ status: 0, msg: '注册成功！' });
}


/**
 * [Mock] 注册信息检测
 */
export function checkInfo(data) {
  // 假设所有检测都通过
  return Promise.resolve({ status: 0, msg: '信息验证成功！' });
}


/**
 * [Mock] 用户登出
 */
export function logout() {
  return Promise.resolve({ status: 0 });
}


/**
 * [Mock] 获取用户信息
 */
export function getUserInfo() {
  return Promise.resolve({
    status: 0,
    data: {
      id: 1,
      account: 'test',
      name: '测试用户',
      email: 'test@example.com',
      phone: '123456789',
      age: 20,
      sex: 1,
      role: 0,
      create_time: Date.now(),
      update_time: Date.now(),
      question: '你的密码是什么',
      asw: '不告诉你',
    }
  });
}


/**
 * [Mock] 用户信息修改
 */
export function updateUserInfo(data) {
  return Promise.resolve({
    status: 0,
    msg: '用户信息修改成功！',
    data: {
      id: 1,
      account: 'test',
      name: '测试用户',
      email: data.email,
      phone: data.phone,
      age: data.age,
      sex: data.sex,
      question: data.question,
      asw: data.asw,
      role: 0,
      create_time: Date.now(),
      update_time: Date.now(),
    }
  });
}


/**
 * [Mock] 用户修改密码
 */
export function updatePassword(data) {
  if (data.oldpwd === '123456') {
    return Promise.resolve({ status: 0, msg: '密码修改成功！' });
  } else {
    return Promise.resolve({ status: 1, msg: '原始密码错误！' });
  }
}


/**
 * [Mock] 获取用户密码问题
 */
export function getUserQuestion(data) {
  if (data.account === 'test') {
    return Promise.resolve({ status: 0, data: '你的密码是什么' });
  } else {
    return Promise.resolve({ status: 1, data: '未设置密码提示问题' });
  }
}


/**
 * [Mock] 验证用户密码问题答案
 */
export function checkUserAsw(data) {
  if (data.asw === '不告诉你') {
    return Promise.resolve({ status: 0, msg: 'mock-token-123456' });
  } else {
    return Promise.resolve({ status: 1, msg: '问题答案错误！' });
  }
}


/**
 * [Mock] 根据密码问题和答案重置密码
 */
export function resetPassword(data) {
  if (data.newpwd && data.userId) {
    return Promise.resolve({ status: 0, msg: '密码修改成功！' });
  } else {
    return Promise.resolve({ status: 1, msg: '重置失败' });
  }
}


/**
 * [Mock] 验证用户获得用户对象接口
 */
export function getUserByAccount(data) {
  if (data.account === 'admin') {
    return Promise.resolve({
      status: 0,
      data: {
        id: 1,
        account: 'admin',
        password: '',
        email: '546415155@qq.com',
        age: 18,
        sex: 0,
        del: 0,
        phone: '17844545452',
        question: '1',
        asw: '',
        role: 1,
        create_time: 1517672134000,
        update_time: 1555653582000,
        name: '小明000'
      }
    });
  } else {
    return Promise.resolve({ status: 1, msg: '用户名错误！' });
  }
}


// import request from './request';

// /**
// * [真实请求] 用户登录（联调时请启用，开发阶段可注释）
// */
// export function login(data) {
//   return request({
//     url: '/user/do_login.do',
//     method: 'post',
//     params: {
//       account: data.account,
//       password: data.password
//     }
//   });
// }

// /**
// * [真实请求] 用户注册
// */
// export function register(data) {
//   return request({
//     url: '/user/do_register.do',
//     method: 'post',
//     data
//   });
// }

// /**
// * [真实请求] 用户登出
// */
// export function logout() {
//   return request({
//     url: '/user/do_logout.do',
//     method: 'post',
//   });
// }

// /**
// * [真实请求] 获取用户信息
// */
// export function getUserInfo() {
//   return request({
//     url: '/user/getuserinfo.do',
//     method: 'get',
//   });
// }

// /**
// * [真实请求] 用户信息修改
// */
// export function updateUserInfo(data) {
//   return request({
//     url: '/user/updateuserinfo.do',
//     method: 'post',
//     data
//   });
// }

// /**
// * [真实请求] 用户修改密码
// */
// export function updatePassword(data) {
//   return request({
//     url: '/user/updatepassword.do',
//     method: 'post',
//     data
//   });
// }

// /**
// * [真实请求] 获取用户密码问题
// */
// export function getUserQuestion(data) {
//   return request({
//     url: '/user/getuserquestion.do',
//     method: 'post',
//     params: { account: data.account }
//   });
// }

// /**
// * [真实请求] 验证用户密码问题答案
// */
// export function checkUserAsw(data) {
//   return request({
//     url: '/user/checkuserasw.do',
//     method: 'post',
//     params: {
//       account: data.account,
//       question: data.question,
//       asw: data.asw
//     }
//   });
// }

// /**
// * [真实请求] 根据密码问题和答案重置密码
// */
// export function resetPassword(data) {
//   return request({
//     url: '/user/resetpassword.do',
//     method: 'post',
//     params: {
//       account: data.id,           // 用户id
//       asw: data.asw,         // 密保答案
//       newpwd: data.newpwd    // 新密码
//     }
//   });
// }

// /**
// * [真实请求] 验证用户获得用户对象接口
// */
// export function getUserByAccount(data) {
//   return request({
//     url: '/user/getUserByAccount.do',
//     method: 'post',
//     data
//   });
// }