<template>
  <div class="forgot-container">
    <div class="forgot-card">
      <h2>找回密码</h2>
      <form v-if="step === 1" @submit.prevent="handleGetQuestion">
        <div class="form-row">
          <label>账号：</label>
          <input v-model="account" type="text" required />
        </div>
        <div class="form-actions">
          <button type="submit" class="action-btn">获取密保问题</button>
        </div>
        <div v-if="msg" :class="{'error-msg': !success, 'success-msg': success}">{{ msg }}</div>
      </form>
      <form v-else-if="step === 2" @submit.prevent="handleCheckAnswer">
        <div class="form-row">
          <label>密保问题：</label>
          <input v-model="question" type="text" readonly />
        </div>
        <div class="form-row">
          <label>密保答案：</label>
          <input v-model="asw" type="text" required />
        </div>
        <div class="form-actions">
          <button type="submit" class="action-btn">验证答案</button>
          <button type="button" class="cancel-btn" @click="resetAll">返回</button>
        </div>
        <div v-if="msg" :class="{'error-msg': !success, 'success-msg': success}">{{ msg }}</div>
      </form>
      <form v-else-if="step === 3" @submit.prevent="handleResetPwd">
        <div class="form-row">
          <label>新密码：</label>
          <input v-model="newpwd" type="password" required />
        </div>
        <div class="form-actions">
          <button type="submit" class="action-btn">重置密码</button>
        </div>
        <div v-if="msg" :class="{'error-msg': !success, 'success-msg': success}">{{ msg }}</div>
      </form>
      <div v-if="step === 4" class="success-msg">
        密码重置成功！<router-link to="/login">去登录</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { getUserQuestion, checkUserAsw, resetPassword } from '@/api/user';

const router = useRouter();
const step = ref(1); // 1:账号 2:答案 3:新密码 4:完成
const account = ref('');
const question = ref('');
const asw = ref('');
const newpwd = ref('');
const userId = ref('');
const token = ref('');
const msg = ref('');
const success = ref(false);

const resetAll = () => {
  step.value = 1;
  account.value = '';
  question.value = '';
  asw.value = '';
  newpwd.value = '';
  userId.value = '';
  token.value = '';
  msg.value = '';
  success.value = false;
};

const handleGetQuestion = async () => {
  msg.value = '';
  success.value = false;
  try {
    const res = await getUserQuestion({ account: account.value });
    if (res.status === 0) {
      question.value = res.data;
      step.value = 2;
      msg.value = '';
    } else {
      msg.value = res.data || '未设置密保问题';
    }
  } catch (e) {
    msg.value = e.message || '获取密保问题失败';
  }
};

const handleCheckAnswer = async () => {
  msg.value = '';
  success.value = false;
  try {
    const res = await checkUserAsw({ account: account.value, question: question.value, asw: asw.value });
    if (res.status === 0) {
      // 这里假设 msg 是 token，实际接口如有 userId 需调整
      token.value = res.msg;
      // 需要用户id，通常后端应返回，这里用 account 代替（如有 userId 请调整）
      // step3 需要 userId
      // 这里假设有 userId，实际项目请根据后端返回调整
      userId.value = account.value;
      step.value = 3;
      msg.value = '';
    } else {
      msg.value = res.msg || '答案错误';
    }
  } catch (e) {
    msg.value = e.message || '验证失败';
  }
};

const handleResetPwd = async () => {
  msg.value = '';
  success.value = false;
  try {
    // 这里 userId 实际应为后端返回的 id，这里用 account 代替
    const res = await resetPassword({ newpwd: newpwd.value, userId: userId.value });
    if (res.status === 0) {
      step.value = 4;
      msg.value = '';
      success.value = true;
    } else {
      msg.value = res.msg || '重置失败';
    }
  } catch (e) {
    msg.value = e.message || '重置失败';
  }
};
</script>

<style scoped>
.forgot-container {
  min-height: 100vh;
  background: #f7f8fa;
  display: flex;
  align-items: center;
  justify-content: center;
}
.forgot-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 16px rgba(0,0,0,0.10);
  padding: 36px 32px 28px 32px;
  min-width: 340px;
  max-width: 90vw;
}
h2 {
  text-align: center;
  margin-bottom: 24px;
  color: #222;
  font-weight: 600;
}
.form-row {
  display: flex;
  align-items: center;
  margin-bottom: 18px;
}
.form-row label {
  width: 90px;
  color: #555;
  font-size: 15px;
}
.form-row input {
  flex: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 15px;
}
.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 10px;
}
.action-btn {
  background: #409EFF;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 8px 24px;
  font-size: 15px;
  cursor: pointer;
}
.action-btn:hover {
  background: #66b1ff;
}
.cancel-btn {
  background: #f56c6c;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 8px 24px;
  font-size: 15px;
  cursor: pointer;
}
.cancel-btn:hover {
  background: #ff7875;
}
.success-msg {
  color: #67c23a;
  margin-top: 12px;
  text-align: center;
}
.error-msg {
  color: #f56c6c;
  margin-top: 12px;
  text-align: center;
}
</style> 