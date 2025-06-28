<template>
  <div class="login-container">
    <div class="login-box">
      <button class="back-btn" @click="goBack">← 返回</button>
      <h2>用户登录</h2>
      <form @submit.prevent="handleLogin">
        <div class="input-group">
          <label for="account">用户名</label>
          <input type="text" id="account" v-model="loginForm.account" placeholder="请输入用户名" required>
        </div>
        <div class="input-group">
          <label for="password">密码</label>
          <input type="password" id="password" v-model="loginForm.password" placeholder="请输入密码" required>
        </div>
        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>
        <button type="submit" class="login-button">登录</button>
        <div class="register-link">
          还没有账号？ <router-link to="/register">立即注册</router-link>
        </div>
        <div class="forgot-link">
          <router-link to="/forgot-password">忘记密码？</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';

const router = useRouter();
const userStore = useUserStore();

const loginForm = ref({
  account: 'admin', // 默认值方便测试
  password: '123'
});

const errorMessage = ref('');

const handleLogin = async () => {
  errorMessage.value = '';
  if (!loginForm.value.account || !loginForm.value.password) {
    errorMessage.value = '用户名和密码不能为空';
    return;
  }

  try {
    await userStore.login(loginForm.value);
    // 登录成功，跳转到首页
    router.push('/');
  } catch (error) {
    // 登录失败，显示错误信息
    errorMessage.value = error.message || '登录失败，请稍后再试';
  }
};

const goBack = () => {
  if (window.history.length > 1) {
    router.back();
  } else {
    router.push('/');
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}

.login-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
  position: relative;
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

h2 {
  margin-bottom: 20px;
  color: #333;
}

.input-group {
  margin-bottom: 20px;
  text-align: left;
}

.input-group label {
  display: block;
  margin-bottom: 5px;
  color: #555;
}

.input-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

.login-button {
  width: 100%;
  padding: 10px;
  border: none;
  border-radius: 4px;
  background-color: #409EFF;
  color: white;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.login-button:hover {
  background-color: #66b1ff;
}

.error-message {
  color: #f56c6c;
  margin-bottom: 15px;
}

.register-link {
  margin-top: 20px;
  font-size: 14px;
}

.register-link a {
  color: #409EFF;
  text-decoration: none;
}

.forgot-link {
  margin-top: 10px;
  text-align: right;
}
.forgot-link a {
  color: #e6a23c;
  text-decoration: none;
  font-size: 14px;
}
.forgot-link a:hover {
  text-decoration: underline;
}
</style>
