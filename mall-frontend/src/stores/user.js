import { defineStore } from 'pinia';
import { login as apiLogin, logout as apiLogout, getUserInfo as apiGetUserInfo, updateUserInfo as apiUpdateUserInfo, updatePassword as apiUpdatePassword } from '@/api/user';

export const useUserStore = defineStore('user', {
  state: () => ({
    // 从 localStorage 初始化用户信息，防止刷新后状态丢失
    userInfo: JSON.parse(localStorage.getItem('userInfo')) || null,
  }),
  getters: {
    // 判断用户是否登录
    isLoggedIn: (state) => !!state.userInfo,
    // 获取用户角色
    userRole: (state) => (state.userInfo ? state.userInfo.role : null),
  },
  actions: {
    // 登录方法
    async login(loginData) {
      try {
        const response = await apiLogin(loginData);
        if (response.status === 0) {
          this.userInfo = response.data;
          // 将用户信息存储到 localStorage
          localStorage.setItem('userInfo', JSON.stringify(response.data));
        }
        return response;
      } catch (error) {
        // 清理用户信息，以防万一
        this.logout();
        throw error;
      }
    },

    // 登出方法
    async logout() {
      try {
        await apiLogout();
      } catch (e) {
        // 即使 API 失败也要清理本地状态
      }
      this.userInfo = null;
      localStorage.removeItem('userInfo');
      // 如果有其他需要清除的数据，也在这里处理
      // 例如：清除购物车信息等
    },

    // 拉取用户信息
    async fetchUserInfo() {
      try {
        const res = await apiGetUserInfo();
        if (res.status === 0) {
          this.userInfo = res.data;
          localStorage.setItem('userInfo', JSON.stringify(res.data));
        }
        return res;
      } catch (error) {
        // 拉取失败时可选清空本地信息
        this.logout();
        throw error;
      }
    },

    // 用户信息修改
    async updateUserInfo(updateData) {
      const res = await apiUpdateUserInfo(updateData);
      if (res.status === 0) {
        this.userInfo = res.data;
        localStorage.setItem('userInfo', JSON.stringify(res.data));
      }
      return res;
    },

    // 用户修改密码
    async updatePassword({ oldpwd, newpwd }) {
      const res = await apiUpdatePassword({ oldpwd, newpwd });
      return res;
    },
  },
}); 