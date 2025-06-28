<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { register, checkInfo } from '@/api/user';

const router = useRouter();

const registerForm = ref({
  account: '',
  password: '',
  confirmPassword: '',
  email: '',
  phone: '',
  question: '',
  asw: ''
});

const formErrors = ref({});
const successMessage = ref('');

// 校验函数
const validateField = async (field, type) => {
  if (!registerForm.value[field]) {
    formErrors.value[field] = '此字段不能为空';
    return false;
  }
  try {
    await checkInfo({ info: registerForm.value[field], type: type });
    formErrors.value[field] = ''; // 清除错误
    return true;
  } catch (error) {
    formErrors.value[field] = error.message;
    return false;
  }
};

const handleRegister = async () => {
  // 清空之前的错误
  formErrors.value = {};
  successMessage.value = '';

  // 前端基础校验
  if (registerForm.value.password !== registerForm.value.confirmPassword) {
    formErrors.value.confirmPassword = '两次输入的密码不一致';
    return;
  }

  // 挨个校验
  const isAccountValid = await validateField('account', 'account');
  const isEmailValid = await validateField('email', 'email');
  const isPhoneValid = await validateField('phone', 'phone');

  if (!isAccountValid || !isEmailValid || !isPhoneValid) {
    return;
  }
  
  // 提交注册
  try {
    const { password, confirmPassword, ...registerData } = registerForm.value;
    await register(registerData);
    successMessage.value = '注册成功！正在跳转到登录页面...';
    setTimeout(() => {
      router.push('/login');
    }, 2000);
  } catch (error) {
    formErrors.value.submit = error.message || '注册失败，请稍后重试';
  }
};
</script>

<template>
  <div class="register-container">
    <div class="register-box">
      <h2>用户注册</h2>
      <form @submit.prevent="handleRegister">
        <div class="input-group">
          <label for="account">用户名</label>
          <input type="text" v-model="registerForm.account" @blur="validateField('account', 'account')" placeholder="请输入用户名" required>
          <span v-if="formErrors.account" class="error-text">{{ formErrors.account }}</span>
        </div>
        <div class="input-group">
          <label for="password">密码</label>
          <input type="password" v-model="registerForm.password" placeholder="请输入密码" required>
        </div>
        <div class="input-group">
          <label for="confirmPassword">确认密码</label>
          <input type="password" v-model="registerForm.confirmPassword" placeholder="请再次输入密码" required>
          <span v-if="formErrors.confirmPassword" class="error-text">{{ formErrors.confirmPassword }}</span>
        </div>
        <div class="input-group">
          <label for="email">电子邮箱</label>
          <input type="email" v-model="registerForm.email" @blur="validateField('email', 'email')" placeholder="请输入电子邮箱" required>
           <span v-if="formErrors.email" class="error-text">{{ formErrors.email }}</span>
        </div>
        <div class="input-group">
          <label for="phone">手机号码</label>
          <input type="tel" v-model="registerForm.phone" @blur="validateField('phone', 'phone')" placeholder="请输入手机号码" required>
           <span v-if="formErrors.phone" class="error-text">{{ formErrors.phone }}</span>
        </div>
        <div class="input-group">
          <label for="question">密保问题</label>
          <input type="text" v-model="registerForm.question" placeholder="请输入密保问题" required>
        </div>
        <div class="input-group">
          <label for="asw">密保答案</label>
          <input type="text" v-model="registerForm.asw" placeholder="请输入密保答案" required>
        </div>

        <div v-if="formErrors.submit" class="error-message">
          {{ formErrors.submit }}
        </div>
        <div v-if="successMessage" class="success-message">
            {{ successMessage }}
        </div>

        <button type="submit" class="register-button">注册</button>
         <div class="login-link">
          已经有账号了？ <router-link to="/login">立即登录</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
/* 样式与登录页面类似，可以复用或调整 */
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 50px 0;
  background-color: #f0f2f5;
}

.register-box {
  width: 500px;
  padding: 40px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}

.input-group {
  margin-bottom: 15px;
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

.register-button {
  width: 100%;
  padding: 10px;
  border: none;
  border-radius: 4px;
  background-color: #67c23a;
  color: white;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.register-button:hover {
  background-color: #85ce61;
}

.error-text {
    font-size: 12px;
    color: #f56c6c;
}

.error-message, .success-message {
  text-align: center;
  margin-bottom: 15px;
  padding: 10px;
  border-radius: 4px;
}

.error-message {
  color: #f56c6c;
  background-color: #fef0f0;
}

.success-message {
    color: #67c23a;
    background-color: #f0f9eb;
}

.login-link {
  margin-top: 20px;
  font-size: 14px;
  text-align: center;
}

.login-link a {
  color: #409EFF;
  text-decoration: none;
}
</style> 