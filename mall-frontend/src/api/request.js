import axios from 'axios';

// 创建 axios 实例
const service = axios.create({
  // VITE_APP_BASE_API 是在 .env.development 文件中配置的
  baseURL: 'http://localhost:8080/actionmall', 
  timeout: 5000, // 请求超时时间
});

// 配置为 withCredentials = true，使得请求带上凭证（如 cookie）
service.defaults.withCredentials = true;

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 这里可以添加 token 等
    return config;
  },
  error => {
    // do something with request error
    console.log(error); // for debug
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data;
    // 如果 status 不是 0，则判定为错误
    if (res.status !== 0) {
      // 这里可以根据业务需求处理不同的错误码
      // ElMessage({
      //   message: res.msg || 'Error',
      //   type: 'error',
      //   duration: 5 * 1000
      // });
      console.error('API Error: ' + res.msg);
      return Promise.reject(new Error(res.msg || 'Error'));
    } else {
      return res;
    }
  },
  error => {
    console.log('err' + error); // for debug
    // ElMessage({
    //   message: error.message,
    //   type: 'error',
    //   duration: 5 * 1000
    // });
    return Promise.reject(error);
  }
);

export default service; 